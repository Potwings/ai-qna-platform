# QnA AI - RAG 기반 질의응답 플랫폼

## 프로젝트 소개
기존 질의응답 데이터를 활용하여 AI가 자동으로 답변하는 지식 공유 플랫폼입니다.
인프런 커뮤니티와 같은 댓글 형식의 질의응답 구조를 채택했습니다.

## 주요 기능
- **질문 등록**: 사용자가 질문을 등록하면 AI가 자동으로 첫 답변 생성
- **댓글식 답변**: 하나의 질문에 여러 답변(댓글) 작성 가능
- **RAG 시스템**: 유사한 기존 Q&A를 검색하여 컨텍스트로 활용
- **추천 기능**: 질문과 답변에 추천(upvote) 가능
- **답변 채택**: 질문자가 도움이 된 답변 채택 가능
- **키워드 검색**: 제목과 내용으로 질문 검색

## 기술 스택
- **Backend**: Spring Boot 3.4.1, Java 17
- **Database**: MySQL (메인 데이터), ChromaDB (벡터 검색)
- **AI**: Spring AI + Ollama (gemma3:4b, all-minilm embedding)
- **Infrastructure**: Docker Compose

## 시작하기

### 1. Docker 컨테이너 실행
```bash
docker-compose up -d
```

MySQL과 ChromaDB가 자동으로 실행됩니다.

### 2. 백엔드 서버 실행
```bash
./gradlew bootRun
```

또는 Windows:
```bash
gradlew.bat bootRun
```

백엔드 서버가 `http://localhost:8080`에서 실행됩니다.

### 3. 프론트엔드 서버 실행
```bash
cd frontend
npm install
npm run dev
```

프론트엔드가 `http://localhost:3000`에서 실행됩니다.

### 4. 웹 브라우저로 접속
```
http://localhost:3000
```

샘플 데이터(`data.sql`)가 자동으로 로드되어 3개의 질문과 답변을 볼 수 있습니다.

### 5. API 직접 테스트 (선택)

#### 질문 생성 (AI 답변 자동 생성)
```bash
curl -X POST http://localhost:8080/api/questions \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Spring Security 설정 방법",
    "content": "Spring Boot 3.x에서 Spring Security를 설정하려면 어떻게 해야 하나요?",
    "userName": "테스터"
  }'
```

#### 질문 목록 조회
```bash
curl http://localhost:8080/api/questions
```

#### 특정 질문 조회 (답변 포함)
```bash
curl http://localhost:8080/api/questions/1
```

#### 답변 추가 (댓글 형식)
```bash
curl -X POST http://localhost:8080/api/questions/1/answers \
  -H "Content-Type: application/json" \
  -d '{
    "content": "추가 설명입니다...",
    "userName": "댓글작성자"
  }'
```

#### 키워드 검색
```bash
curl "http://localhost:8080/api/questions/search?keyword=Spring"
```

#### 질문 추천
```bash
curl -X POST http://localhost:8080/api/questions/1/upvote
```

#### 답변 추천
```bash
curl -X POST http://localhost:8080/api/questions/answers/1/upvote
```

#### 답변 채택
```bash
curl -X POST http://localhost:8080/api/questions/answers/1/accept
```

## API 엔드포인트

| Method | Endpoint | 설명 |
|--------|----------|------|
| POST | /api/questions | 질문 생성 + AI 자동 답변 |
| GET | /api/questions | 질문 목록 조회 |
| GET | /api/questions/{id} | 특정 질문 조회 (답변 포함) |
| GET | /api/questions/search?keyword= | 키워드 검색 |
| POST | /api/questions/{id}/answers | 답변 추가 (댓글) |
| POST | /api/questions/{id}/upvote | 질문 추천 |
| POST | /api/questions/answers/{answerId}/upvote | 답변 추천 |
| POST | /api/questions/answers/{answerId}/accept | 답변 채택 |

## RAG 워크플로우

```
1. 사용자 질문 입력
   ↓
2. 질문을 MySQL에 저장
   ↓
3. 유사한 Q&A 검색 (ChromaDB 벡터 검색)
   ↓
4. 검색 결과를 컨텍스트로 구성
   ↓
5. Ollama LLM에 컨텍스트 + 질문 전달
   ↓
6. AI 답변 생성 및 저장
   ↓
7. 새 Q&A 쌍을 벡터로 임베딩하여 ChromaDB에 저장
```

## 데이터베이스 스키마

### Question (질문)
- id: 질문 ID
- title: 제목
- content: 내용
- user_name: 작성자
- view_count: 조회수
- upvote_count: 추천수
- created_at, updated_at: 생성/수정 시간

### Answer (답변/댓글)
- id: 답변 ID
- question_id: 질문 ID (FK)
- content: 답변 내용
- is_ai_generated: AI 생성 여부
- is_accepted: 채택 여부
- user_name: 작성자
- upvote_count: 추천수
- created_at, updated_at: 생성/수정 시간

### QaEmbeddingMetadata (임베딩 메타데이터)
- id: 메타데이터 ID
- question_id: 질문 ID
- answer_id: 답변 ID
- vector_store_id: ChromaDB 문서 ID
- embedding_created_at: 임베딩 생성 시간

## 설정

### application.properties
```properties
# MySQL
spring.datasource.url=jdbc:mysql://localhost:3306/qna_db
spring.datasource.username=root
spring.datasource.password=qna_password

# Ollama
spring.ai.ollama.base-url=https://5vosd1iqsrzrch-11434.proxy.runpod.net/
spring.ai.ollama.chat.model=gemma3:4b
spring.ai.ollama.embedding.model=all-minilm

# ChromaDB
spring.ai.vectorstore.chroma.client.host=localhost
spring.ai.vectorstore.chroma.client.port=8000
spring.ai.vectorstore.chroma.collection-name=qna_embeddings
```

## 디렉토리 구조
```
src/main/java/kr/yonggeon/qnaai/
├── config/
│   └── DataInitializer.java
├── controller/
│   └── QuestionController.java
├── dto/
│   ├── QuestionRequest.java
│   ├── QuestionResponse.java
│   ├── AnswerRequest.java
│   └── AnswerResponse.java
├── entity/
│   ├── Question.java
│   ├── Answer.java
│   └── QaEmbeddingMetadata.java
├── exception/
│   ├── GlobalExceptionHandler.java
│   └── ResourceNotFoundException.java
├── repository/
│   ├── QuestionRepository.java
│   ├── AnswerRepository.java
│   └── QaEmbeddingMetadataRepository.java
├── service/
│   ├── QuestionService.java
│   ├── AnswerService.java
│   ├── VectorStoreService.java
│   ├── EmbeddingService.java
│   └── RagService.java ⭐ 핵심
└── QnaAiApplication.java
```

## 라이센스
MIT License
