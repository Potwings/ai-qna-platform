package kr.yonggeon.qnaai.controller;

import jakarta.validation.Valid;
import kr.yonggeon.qnaai.dto.AnswerRequest;
import kr.yonggeon.qnaai.dto.AnswerResponse;
import kr.yonggeon.qnaai.dto.QuestionRequest;
import kr.yonggeon.qnaai.dto.QuestionResponse;
import kr.yonggeon.qnaai.entity.Answer;
import kr.yonggeon.qnaai.entity.Question;
import kr.yonggeon.qnaai.service.AnswerService;
import kr.yonggeon.qnaai.service.QuestionService;
import kr.yonggeon.qnaai.service.RagService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/questions")
@RequiredArgsConstructor
@Slf4j
public class QuestionController {

    private final QuestionService questionService;
    private final AnswerService answerService;
    private final RagService ragService;

    /**
     * 질문 생성 + AI 자동 답변 (비동기)
     * 질문은 즉시 반환되고, AI 답변은 백그라운드에서 생성됩니다.
     */
    @PostMapping
    public ResponseEntity<QuestionResponse> createQuestion(@Valid @RequestBody QuestionRequest request) {
        log.info("Creating new question: {}", request.getTitle());

        // 질문 생성
        Question question = questionService.create(
                request.getTitle(),
                request.getContent(),
                request.getUserName()
        );

        // AI 자동 답변 비동기 생성 (백그라운드에서 처리)
        ragService.generateAnswerAsync(question);
        log.info("Question created with ID: {}, AI answer generation started in background", question.getId());

        // 응답 반환 (질문만 반환, 답변은 나중에 생성됨)
        QuestionResponse response = QuestionResponse.from(question);
        response.setAnswerCount(0L);

        return ResponseEntity.ok(response);
    }

    /**
     * 질문 목록 조회
     */
    @GetMapping
    public ResponseEntity<List<QuestionResponse>> getAllQuestions() {
        log.info("Fetching all questions");
        List<Question> questions = questionService.findAll();
        List<QuestionResponse> responses = questions.stream()
                .map(question -> {
                    List<Answer> answers = answerService.findByQuestionId(question.getId());
                    return QuestionResponse.from(question, answers);
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(responses);
    }

    /**
     * 특정 질문 조회 (답변 포함)
     */
    @GetMapping("/{id}")
    public ResponseEntity<QuestionResponse> getQuestion(@PathVariable Long id) {
        log.info("Fetching question with ID: {}", id);

        // 조회수 증가
        questionService.incrementViewCount(id);

        Question question = questionService.findById(id);
        List<Answer> answers = answerService.findByQuestionId(id);

        QuestionResponse response = QuestionResponse.from(question, answers);
        return ResponseEntity.ok(response);
    }

    /**
     * 키워드 검색
     */
    @GetMapping("/search")
    public ResponseEntity<List<QuestionResponse>> searchQuestions(@RequestParam String keyword) {
        log.info("Searching questions with keyword: {}", keyword);

        List<Question> questions = questionService.searchByKeyword(keyword);
        List<QuestionResponse> responses = questions.stream()
                .map(question -> {
                    List<Answer> answers = answerService.findByQuestionId(question.getId());
                    return QuestionResponse.from(question, answers);
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(responses);
    }

    /**
     * 질문에 답변 추가 (댓글 형식)
     */
    @PostMapping("/{id}/answers")
    public ResponseEntity<AnswerResponse> addAnswer(
            @PathVariable Long id,
            @Valid @RequestBody AnswerRequest request
    ) {
        log.info("Adding answer to question ID: {}", id);

        // 질문 존재 확인
        questionService.findById(id);

        // 답변 생성
        Answer answer = answerService.create(
                id,
                request.getContent(),
                request.getUserName(),
                false  // 사용자가 작성한 답변
        );

        AnswerResponse response = AnswerResponse.from(answer);
        log.info("Answer created with ID: {}", answer.getId());

        return ResponseEntity.ok(response);
    }

    /**
     * 질문 추천
     */
    @PostMapping("/{id}/upvote")
    public ResponseEntity<Void> upvoteQuestion(@PathVariable Long id) {
        log.info("Upvoting question ID: {}", id);
        questionService.incrementUpvoteCount(id);
        return ResponseEntity.ok().build();
    }

    /**
     * 답변 추천
     */
    @PostMapping("/answers/{answerId}/upvote")
    public ResponseEntity<Void> upvoteAnswer(@PathVariable Long answerId) {
        log.info("Upvoting answer ID: {}", answerId);
        answerService.incrementUpvoteCount(answerId);
        return ResponseEntity.ok().build();
    }

    /**
     * 답변 채택
     */
    @PostMapping("/answers/{answerId}/accept")
    public ResponseEntity<Void> acceptAnswer(@PathVariable Long answerId) {
        log.info("Accepting answer ID: {}", answerId);
        answerService.markAsAccepted(answerId);
        return ResponseEntity.ok().build();
    }
}
