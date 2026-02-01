package kr.yonggeon.qnaai.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDateTime;

@Entity
@Table(name = "answer")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Answer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "question_id", nullable = false)
    private Long questionId;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @Column(name = "is_ai_generated", nullable = false)
    @Builder.Default
    private Boolean aiGenerated = false;

    @Column(name = "is_accepted", nullable = false)
    @Builder.Default
    private Boolean accepted = false;

    @Column(name = "similarity_score")
    private Double similarityScore;

    @Column(name = "user_name", length = 100)
    private String userName;

    @Column(name = "upvote_count", nullable = false)
    @Builder.Default
    private Integer upvoteCount = 0;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (aiGenerated == null) {
            aiGenerated = false;
        }
        if (accepted == null) {
            accepted = false;
        }
        if (upvoteCount == null) {
            upvoteCount = 0;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
