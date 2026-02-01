package kr.yonggeon.qnaai.service;

import kr.yonggeon.qnaai.entity.Answer;
import kr.yonggeon.qnaai.entity.QaEmbeddingMetadata;
import kr.yonggeon.qnaai.entity.Question;
import kr.yonggeon.qnaai.repository.QaEmbeddingMetadataRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmbeddingService {

    private final VectorStore vectorStore;
    private final QaEmbeddingMetadataRepository metadataRepository;

    @Transactional
    public void createQaEmbedding(Question question, Answer answer) {
        log.info("Creating embedding for Question ID: {}, Answer ID: {}", question.getId(), answer.getId());

        // 벡터 스토어 ID 생성
        String vectorStoreId = "qa_" + UUID.randomUUID().toString();

        // 문서 텍스트 생성 (Q&A 페어)
        String documentText = String.format(
                "질문 제목: %s\n질문 내용: %s\n답변: %s",
                question.getTitle(),
                question.getContent(),
                answer.getContent()
        );

        // 메타데이터 생성
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("questionId", question.getId());
        metadata.put("answerId", answer.getId());
        metadata.put("questionTitle", question.getTitle());
        metadata.put("questionContent", question.getContent());
        metadata.put("answerContent", answer.getContent());
        metadata.put("createdAt", question.getCreatedAt().toString());

        // Document 생성 및 벡터 스토어에 저장
        Document document = new Document(vectorStoreId, documentText, metadata);
        vectorStore.add(List.of(document));

        // 메타데이터 DB에 저장
        QaEmbeddingMetadata embeddingMetadata = QaEmbeddingMetadata.builder()
                .questionId(question.getId())
                .answerId(answer.getId())
                .vectorStoreId(vectorStoreId)
                .build();
        metadataRepository.save(embeddingMetadata);

        log.info("Successfully created embedding with ID: {}", vectorStoreId);
    }

    @Transactional
    public void deleteQaEmbedding(Long questionId, Long answerId) {
        log.info("Deleting embedding for Question ID: {}, Answer ID: {}", questionId, answerId);

        metadataRepository.findByQuestionIdAndAnswerId(questionId, answerId)
                .ifPresent(metadata -> {
                    vectorStore.delete(List.of(metadata.getVectorStoreId()));
                    metadataRepository.delete(metadata);
                    log.info("Successfully deleted embedding with ID: {}", metadata.getVectorStoreId());
                });
    }
}
