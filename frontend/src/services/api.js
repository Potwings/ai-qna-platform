import axios from 'axios';

// Vite 프록시 사용을 위해 상대 경로 사용
const API_BASE_URL = '/api';

const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

// 질문 관련 API
export const questionAPI = {
  // 모든 질문 조회
  getAllQuestions: () => api.get('/questions'),

  // 특정 질문 조회 (답변 포함)
  getQuestion: (id) => api.get(`/questions/${id}`),

  // 질문 생성 (AI 답변 자동 생성)
  createQuestion: (questionData) => api.post('/questions', questionData),

  // 키워드 검색
  searchQuestions: (keyword) => api.get('/questions/search', { params: { keyword } }),

  // 질문 추천
  upvoteQuestion: (id) => api.post(`/questions/${id}/upvote`),
};

// 답변 관련 API
export const answerAPI = {
  // 답변 추가 (댓글)
  addAnswer: (questionId, answerData) => api.post(`/questions/${questionId}/answers`, answerData),

  // 답변 추천
  upvoteAnswer: (answerId) => api.post(`/questions/answers/${answerId}/upvote`),

  // 답변 채택
  acceptAnswer: (answerId) => api.post(`/questions/answers/${answerId}/accept`),
};

export default api;
