package kr.yonggeon.qnaai.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class QuestionRequest {

    @NotBlank(message = "제목은 필수입니다")
    @Size(max = 255, message = "제목은 255자 이하여야 합니다")
    private String title;

    @NotBlank(message = "내용은 필수입니다")
    private String content;

    private String userName;
}
