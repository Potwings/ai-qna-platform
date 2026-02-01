package kr.yonggeon.qnaai.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AnswerRequest {

    @NotBlank(message = "답변 내용은 필수입니다")
    private String content;

    private String userName;
}
