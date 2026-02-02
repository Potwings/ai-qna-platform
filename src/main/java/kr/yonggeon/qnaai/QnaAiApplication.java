package kr.yonggeon.qnaai;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class QnaAiApplication {

  public static void main(String[] args) {
    SpringApplication.run(QnaAiApplication.class, args);
  }

}
