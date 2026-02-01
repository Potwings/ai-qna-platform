package kr.yonggeon.qnaai.repository;

import kr.yonggeon.qnaai.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {

    List<Question> findByUserName(String userName);

    List<Question> findAllByOrderByCreatedAtDesc();

    @Query("SELECT q FROM Question q WHERE q.title LIKE %:keyword% OR q.content LIKE %:keyword%")
    List<Question> searchByKeyword(@Param("keyword") String keyword);
}
