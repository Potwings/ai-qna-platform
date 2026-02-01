package kr.yonggeon.qnaai.dto;

import kr.yonggeon.qnaai.entity.Answer;
import kr.yonggeon.qnaai.entity.Question;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@Builder
public class QuestionResponse {
    private Long id;
    private String title;
    private String content;
    private String userName;
    private Integer viewCount;
    private Integer upvoteCount;
    private LocalDateTime createdAt;
    private Long answerCount;

    @Builder.Default
    private List<AnswerResponse> answers = new ArrayList<>();

    public static QuestionResponse from(Question question) {
        return QuestionResponse.builder()
                .id(question.getId())
                .title(question.getTitle())
                .content(question.getContent())
                .userName(question.getUserName())
                .viewCount(question.getViewCount())
                .upvoteCount(question.getUpvoteCount())
                .createdAt(question.getCreatedAt())
                .build();
    }

    public static QuestionResponse from(Question question, List<Answer> answers) {
        QuestionResponse response = from(question);
        response.setAnswers(
                answers.stream()
                        .map(AnswerResponse::from)
                        .collect(Collectors.toList())
        );
        response.setAnswerCount((long) answers.size());
        return response;
    }

    public static QuestionResponse from(Question question, Answer aiAnswer) {
        QuestionResponse response = from(question);
        response.setAnswers(List.of(AnswerResponse.from(aiAnswer)));
        response.setAnswerCount(1L);
        return response;
    }
}
