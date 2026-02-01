# QnA AI Frontend

React 기반 질의응답 플랫폼 프론트엔드

## 기술 스택

- **React** 18
- **Vite** - 빠른 빌드 도구
- **React Router** - 페이지 라우팅
- **Axios** - API 통신
- **CSS** - 순수 CSS (심플한 스타일)

## 주요 기능

### 1. 질문 목록 (/)
- 모든 질문 조회
- 키워드 검색
- 질문 작성 버튼
- 질문 클릭 시 상세 페이지로 이동

### 2. 질문 상세 (/questions/:id)
- 질문 내용 보기
- 모든 답변(댓글) 보기
- AI 생성 답변 표시 (🤖 AI 배지)
- 채택된 답변 표시 (✅ 배지)
- 질문/답변 추천 기능
- 답변 채택 기능
- 새 답변 작성 (댓글 추가)

### 3. 질문 작성 (/questions/new)
- 제목, 내용 입력
- 작성자 이름 (선택)
- 제출 시 AI가 자동으로 첫 답변 생성

## 시작하기

### 1. 의존성 설치
```bash
npm install
```

### 2. 개발 서버 실행
```bash
npm run dev
```

프론트엔드가 `http://localhost:3000`에서 실행됩니다.

### 3. 백엔드 서버 실행
프론트엔드를 사용하기 전에 백엔드 서버가 `http://localhost:8080`에서 실행 중이어야 합니다.

```bash
# 프로젝트 루트에서
./gradlew bootRun
```

## 프로젝트 구조

```
frontend/
├── src/
│   ├── pages/
│   │   ├── QuestionListPage.jsx      # 질문 목록 페이지
│   │   ├── QuestionDetailPage.jsx    # 질문 상세 페이지
│   │   ├── QuestionFormPage.jsx      # 질문 작성 페이지
│   │   └── *.css                      # 페이지별 스타일
│   ├── services/
│   │   └── api.js                     # API 통신 서비스
│   ├── App.jsx                        # 메인 앱 컴포넌트
│   └── main.jsx                       # 엔트리 포인트
├── package.json
├── vite.config.js                     # Vite 설정 (프록시 포함)
└── README.md
```

## API 통신

Vite 프록시를 사용하여 CORS 문제 해결:
- 프론트엔드: `http://localhost:3000`
- 백엔드: `http://localhost:8080`
- `/api/*` 요청은 자동으로 백엔드로 프록시됨

## 빌드

```bash
npm run build
```

빌드된 파일은 `dist/` 폴더에 생성됩니다.
