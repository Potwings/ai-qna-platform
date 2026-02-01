package kr.yonggeon.qnaai.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.ai.chroma.vectorstore.ChromaApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class VectorStoreConfig {

    @Value("${spring.ai.vectorstore.chroma.client.host:localhost}")
    private String chromaHost;

    @Value("${spring.ai.vectorstore.chroma.client.port:8000}")
    private int chromaPort;

    @Bean
    public ChromaApi chromaApi(RestClient.Builder restClientBuilder, ObjectMapper objectMapper) {
        String chromaUrl = String.format("http://%s:%d", chromaHost, chromaPort);
        return new ChromaApi(chromaUrl, restClientBuilder, objectMapper);
    }
}
