package kr.yonggeon.qnaai.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class VectorStoreService {

    private final VectorStore vectorStore;

    public List<Document> findSimilarQA(String query, int topK) {
        log.debug("Searching for similar Q&A with query: {} (topK={})", query, topK);

        SearchRequest searchRequest = SearchRequest.builder()
                .query(query)
                .topK(topK)
                .similarityThreshold(0.5)
                .build();

        List<Document> results = vectorStore.similaritySearch(searchRequest);
        log.debug("Found {} similar documents", results.size());

        return results;
    }

    public String buildContextFromDocuments(List<Document> documents) {
        if (documents.isEmpty()) {
            return "관련된 이전 질문과 답변이 없습니다.";
        }

        StringBuilder context = new StringBuilder();
        context.append("다음은 유사한 이전 질문들과 답변입니다:\n\n");

        for (int i = 0; i < documents.size(); i++) {
            Document doc = documents.get(i);
            context.append(String.format("### 참고 %d:\n", i + 1));
            context.append(doc.getText());
            context.append("\n\n");
        }

        return context.toString();
    }
}
