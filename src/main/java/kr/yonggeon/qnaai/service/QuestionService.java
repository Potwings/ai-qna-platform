package kr.yonggeon.qnaai.service;

import kr.yonggeon.qnaai.entity.Question;
import kr.yonggeon.qnaai.exception.ResourceNotFoundException;
import kr.yonggeon.qnaai.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class QuestionService {

    private final QuestionRepository questionRepository;

    @Transactional
    public Question create(String title, String content, String userName) {
        Question question = Question.builder()
                .title(title)
                .content(content)
                .userName(userName != null ? userName : "익명")
                .build();
        return questionRepository.save(question);
    }

    public Question findById(Long id) {
        return questionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Question not found with id: " + id));
    }

    public List<Question> findAll() {
        return questionRepository.findAllByOrderByCreatedAtDesc();
    }

    public List<Question> searchByKeyword(String keyword) {
        return questionRepository.searchByKeyword(keyword);
    }

    @Transactional
    public void incrementViewCount(Long id) {
        Question question = findById(id);
        question.setViewCount(question.getViewCount() + 1);
        questionRepository.save(question);
    }

    @Transactional
    public void incrementUpvoteCount(Long id) {
        Question question = findById(id);
        question.setUpvoteCount(question.getUpvoteCount() + 1);
        questionRepository.save(question);
    }
}
