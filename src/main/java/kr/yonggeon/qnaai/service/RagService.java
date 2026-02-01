package kr.yonggeon.qnaai.service;

import kr.yonggeon.qnaai.entity.Answer;
import kr.yonggeon.qnaai.entity.Question;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.document.Document;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class RagService {

    private final ChatClient.Builder chatClientBuilder;
    private final VectorStoreService vectorStoreService;
    private final AnswerService answerService;
    private final EmbeddingService embeddingService;

    @Transactional
    public Answer generateAnswer(Question question) {
        log.info("Generating AI answer for Question ID: {}", question.getId());

        // 1. 유사한 Q&A 검색 (상위 3개)
        String searchQuery = question.getTitle() + " " + question.getContent();
        List<Document> similarDocs = vectorStoreService.findSimilarQA(searchQuery, 3);

        // 2. 컨텍스트 구성
        String context = vectorStoreService.buildContextFromDocuments(similarDocs);

        // 3. 프롬프트 생성
        String systemPrompt = """
                당신은 친절한 기술 지원 전문가입니다.
                이전에 비슷한 질문들이 있었다면 그 답변들을 참고하여 새로운 질문에 답변해주세요.
                답변은 한국어로 작성하며, 명확하고 구체적으로 작성해주세요.
                가능한 한 코드 예제나 구체적인 단계를 포함해주세요.
                """;

        String userPrompt = String.format("""
                ### 참고 자료:
                %s

                ### 새로운 질문:
                제목: %s
                내용: %s

                위 참고 자료를 활용하여 새로운 질문에 답변해주세요.
                """, context, question.getTitle(), question.getContent());

        // 4. AI 답변 생성
        log.debug("Calling Ollama LLM for answer generation");
        ChatClient chatClient = chatClientBuilder.build();
        String aiResponse = chatClient.prompt()
                .system(systemPrompt)
                .user(userPrompt)
                .call()
                .content();

        // 5. 답변 저장
        Answer answer = Answer.builder()
                .questionId(question.getId())
                .content(aiResponse)
                .aiGenerated(true)
                .similarityScore(calculateAverageSimilarity(similarDocs))
                .userName("AI 인턴")
                .build();

        Answer savedAnswer = answerService.save(answer);
        log.info("AI answer created with ID: {}", savedAnswer.getId());

        // 6. 새로운 Q&A 임베딩 생성
        try {
            embeddingService.createQaEmbedding(question, savedAnswer);
        } catch (Exception e) {
            log.error("Failed to create embedding for Q&A pair", e);
            // 임베딩 실패해도 답변은 저장된 상태로 유지
        }

        return savedAnswer;
    }

    private Double calculateAverageSimilarity(List<Document> documents) {
        if (documents.isEmpty()) {
            return 0.0;
        }
        // Document에서 similarity score를 추출 (메타데이터에 포함될 수 있음)
        // 현재는 기본값 반환
        return 0.75;
    }
}
