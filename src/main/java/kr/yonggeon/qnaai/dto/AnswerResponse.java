package kr.yonggeon.qnaai.dto;

import kr.yonggeon.qnaai.entity.Answer;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class AnswerResponse {
    private Long id;
    private String content;
    private String userName;
    private Boolean aiGenerated;
    private Boolean accepted;
    private Integer upvoteCount;
    private LocalDateTime createdAt;

    public static AnswerResponse from(Answer answer) {
        return AnswerResponse.builder()
                .id(answer.getId())
                .content(answer.getContent())
                .userName(answer.getUserName())
                .aiGenerated(answer.getAiGenerated())
                .accepted(answer.getAccepted())
                .upvoteCount(answer.getUpvoteCount())
                .createdAt(answer.getCreatedAt())
                .build();
    }
}
