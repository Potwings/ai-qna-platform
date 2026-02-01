package kr.yonggeon.qnaai.repository;

import kr.yonggeon.qnaai.entity.QaEmbeddingMetadata;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface QaEmbeddingMetadataRepository extends JpaRepository<QaEmbeddingMetadata, Long> {

    Optional<QaEmbeddingMetadata> findByVectorStoreId(String vectorStoreId);

    Optional<QaEmbeddingMetadata> findByQuestionIdAndAnswerId(Long questionId, Long answerId);

    void deleteByQuestionId(Long questionId);
}
