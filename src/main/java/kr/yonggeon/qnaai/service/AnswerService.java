package kr.yonggeon.qnaai.service;

import kr.yonggeon.qnaai.entity.Answer;
import kr.yonggeon.qnaai.repository.AnswerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AnswerService {

    private final AnswerRepository answerRepository;

    @Transactional
    public Answer create(Long questionId, String content, String userName, Boolean aiGenerated) {
        Answer answer = Answer.builder()
                .questionId(questionId)
                .content(content)
                .userName(userName != null ? userName : "익명")
                .aiGenerated(aiGenerated != null ? aiGenerated : false)
                .build();
        return answerRepository.save(answer);
    }

    public Answer save(Answer answer) {
        return answerRepository.save(answer);
    }

    public List<Answer> findByQuestionId(Long questionId) {
        return answerRepository.findByQuestionIdOrderByCreatedAtAsc(questionId);
    }

    public Long countByQuestionId(Long questionId) {
        return answerRepository.countByQuestionId(questionId);
    }

    @Transactional
    public void incrementUpvoteCount(Long id) {
        Answer answer = answerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Answer not found with id: " + id));
        answer.setUpvoteCount(answer.getUpvoteCount() + 1);
        answerRepository.save(answer);
    }

    @Transactional
    public void markAsAccepted(Long id) {
        Answer answer = answerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Answer not found with id: " + id));
        answer.setAccepted(true);
        answerRepository.save(answer);
    }
}
