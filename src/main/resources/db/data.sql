-- 샘플 질문 1: Spring Boot JPA
INSERT INTO question (title, content, user_name, created_at, updated_at, view_count, upvote_count)
VALUES (
           'Spring Boot에서 JPA 설정 방법',
           'Spring Boot 3.x에서 JPA를 사용하려면 어떻게 설정해야 하나요? application.properties 설정과 entity 작성 방법이 궁금합니다.',
           '개발자A',
           NOW(),
           NOW(),
           5,
           2
       );

INSERT INTO answer (question_id, content, is_ai_generated, is_accepted, user_name, upvote_count, created_at, updated_at)
VALUES (
           1,
           'Spring Boot에서 JPA를 사용하려면 다음 단계를 따르세요:\n\n1. build.gradle에 의존성 추가:\nimplementation ''org.springframework.boot:spring-boot-starter-data-jpa''\nruntimeOnly ''com.mysql:mysql-connector-j''\n\n2. application.properties 설정:\nspring.datasource.url=jdbc:mysql://localhost:3306/mydb\nspring.jpa.hibernate.ddl-auto=update\n\n3. Entity 클래스 작성:\n@Entity 어노테이션과 @Id, @GeneratedValue 등을 사용하여 엔티티를 정의합니다.',
           false,
           true,
           '전문가B',
           5,
           NOW(),
           NOW()
       );

-- 샘플 질문 2: Docker Compose
INSERT INTO question (title, content, user_name, created_at, updated_at, view_count, upvote_count)
VALUES (
           'Docker Compose로 MySQL 실행하기',
           'Docker Compose를 사용하여 MySQL 컨테이너를 실행하고 싶습니다. docker-compose.yml 파일은 어떻게 작성하나요?',
           '개발자C',
           NOW(),
           NOW(),
           3,
           1
       );

INSERT INTO answer (question_id, content, is_ai_generated, is_accepted, user_name, upvote_count, created_at, updated_at)
VALUES (
           2,
           'Docker Compose로 MySQL을 실행하려면 다음과 같이 docker-compose.yml을 작성하세요:\n\nversion: ''3.8''\nservices:\n  mysql:\n    image: mysql:8.0\n    environment:\n      MYSQL_ROOT_PASSWORD: rootpassword\n      MYSQL_DATABASE: mydb\n    ports:\n      - "3306:3306"\n    volumes:\n      - mysql-data:/var/lib/mysql\n\nvolumes:\n  mysql-data:\n\n그리고 ''docker-compose up -d'' 명령으로 실행하면 됩니다.',
           false,
           true,
           '전문가D',
           3,
           NOW(),
           NOW()
       );

-- 샘플 질문 3: RAG
INSERT INTO question (title, content, user_name, created_at, updated_at, view_count, upvote_count)
VALUES (
           'RAG(Retrieval Augmented Generation)란 무엇인가요?',
           'AI 개발에서 RAG라는 용어를 자주 듣는데, 이게 정확히 무엇이고 어떻게 작동하나요?',
           '개발자E',
           NOW(),
           NOW(),
           10,
           4
       );

INSERT INTO answer (question_id, content, is_ai_generated, is_accepted, user_name, upvote_count, created_at, updated_at)
VALUES (
           3,
           'RAG(Retrieval Augmented Generation)는 대규모 언어 모델(LLM)의 응답 품질을 향상시키는 기술입니다.\n\n작동 방식:\n1. 사용자 질문을 받으면, 관련된 문서를 벡터 데이터베이스에서 검색합니다.\n2. 검색된 문서를 컨텍스트로 LLM에 함께 전달합니다.\n3. LLM은 제공된 컨텍스트를 참고하여 더 정확한 답변을 생성합니다.\n\n장점:\n- 최신 정보 반영 가능\n- 환각(hallucination) 감소\n- 도메인 특화 지식 활용',
           false,
           true,
           '전문가F',
           8,
           NOW(),
           NOW()
       );
