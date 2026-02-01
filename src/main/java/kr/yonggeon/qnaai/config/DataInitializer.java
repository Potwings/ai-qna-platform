package kr.yonggeon.qnaai.config;

import kr.yonggeon.qnaai.entity.Answer;
import kr.yonggeon.qnaai.entity.Question;
import kr.yonggeon.qnaai.repository.AnswerRepository;
import kr.yonggeon.qnaai.repository.QuestionRepository;
import kr.yonggeon.qnaai.service.EmbeddingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class DataInitializer {

    @Bean
    CommandLineRunner initializeEmbeddings(
            QuestionRepository questionRepository,
            AnswerRepository answerRepository,
            EmbeddingService embeddingService
    ) {
        return args -> {
            log.info("Starting to initialize embeddings for existing Q&A pairs...");

            try {
                List<Question> questions = questionRepository.findAll();

                if (questions.isEmpty()) {
                    log.info("No questions found. Skipping embedding initialization.");
                    return;
                }

                int embeddingCount = 0;
                for (Question question : questions) {
                    List<Answer> answers = answerRepository.findByQuestionId(question.getId());

                    for (Answer answer : answers) {
                        try {
                            embeddingService.createQaEmbedding(question, answer);
                            embeddingCount++;
                            log.info("Created embedding for Question ID: {}, Answer ID: {}",
                                    question.getId(), answer.getId());
                        } catch (Exception e) {
                            log.error("Failed to create embedding for Question ID: {}, Answer ID: {}",
                                    question.getId(), answer.getId(), e);
                        }
                    }
                }

                log.info("Successfully initialized {} embeddings", embeddingCount);
            } catch (Exception e) {
                log.error("Error during embedding initialization", e);
            }
        };
    }
}
