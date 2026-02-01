package kr.yonggeon.qnaai.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDateTime;

@Entity
@Table(name = "qa_embedding_metadata")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QaEmbeddingMetadata {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "question_id", nullable = false)
    private Long questionId;

    @Column(name = "answer_id", nullable = false)
    private Long answerId;

    @Column(name = "vector_store_id", unique = true, length = 255)
    private String vectorStoreId;

    @Column(name = "embedding_created_at", nullable = false, updatable = false)
    private LocalDateTime embeddingCreatedAt;

    @PrePersist
    protected void onCreate() {
        embeddingCreatedAt = LocalDateTime.now();
    }
}
