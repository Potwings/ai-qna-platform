import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { questionAPI } from '../services/api';
import './QuestionListPage.css';

function QuestionListPage() {
  const [questions, setQuestions] = useState([]);
  const [searchKeyword, setSearchKeyword] = useState('');
  const [loading, setLoading] = useState(true);
  const navigate = useNavigate();

  useEffect(() => {
    fetchQuestions();
  }, []);

  const fetchQuestions = async () => {
    try {
      setLoading(true);
      const response = await questionAPI.getAllQuestions();
      setQuestions(response.data);
    } catch (error) {
      console.error('질문 목록 조회 실패:', error);
      alert('질문 목록을 불러오는데 실패했습니다.');
    } finally {
      setLoading(false);
    }
  };

  const handleSearch = async (e) => {
    e.preventDefault();
    if (!searchKeyword.trim()) {
      fetchQuestions();
      return;
    }

    try {
      setLoading(true);
      const response = await questionAPI.searchQuestions(searchKeyword);
      setQuestions(response.data);
    } catch (error) {
      console.error('검색 실패:', error);
      alert('검색에 실패했습니다.');
    } finally {
      setLoading(false);
    }
  };

  const formatDate = (dateString) => {
    const date = new Date(dateString);
    const now = new Date();
    const diffMs = now - date;
    const diffMins = Math.floor(diffMs / 60000);
    const diffHours = Math.floor(diffMs / 3600000);
    const diffDays = Math.floor(diffMs / 86400000);

    if (diffMins < 1) return '방금 전';
    if (diffMins < 60) return `${diffMins}분 전`;
    if (diffHours < 24) return `${diffHours}시간 전`;
    if (diffDays < 7) return `${diffDays}일 전`;

    return date.toLocaleDateString('ko-KR');
  };

  if (loading) {
    return <div className="loading">로딩 중...</div>;
  }

  return (
    <div className="question-list-page">
      <header className="page-header">
        <h1>QnA 커뮤니티</h1>
        <p>궁금한 것을 질문하고 AI와 다른 사용자들의 답변을 받아보세요</p>
      </header>

      <div className="search-and-write">
        <form className="search-form" onSubmit={handleSearch}>
          <input
            type="text"
            placeholder="검색어를 입력하세요..."
            value={searchKeyword}
            onChange={(e) => setSearchKeyword(e.target.value)}
            className="search-input"
          />
          <button type="submit" className="search-button">검색</button>
        </form>
        <button
          className="write-button"
          onClick={() => navigate('/questions/new')}
        >
          질문 작성
        </button>
      </div>

      <div className="question-list">
        {questions.length === 0 ? (
          <div className="empty-state">
            <p>질문이 없습니다. 첫 번째 질문을 작성해보세요!</p>
          </div>
        ) : (
          questions.map((question) => (
            <div
              key={question.id}
              className="question-item"
              onClick={() => navigate(`/questions/${question.id}`)}
            >
              <div className="question-main">
                <h3 className="question-title">{question.title}</h3>
                <p className="question-content">{question.content}</p>
                <div className="question-meta">
                  <span className="author">{question.userName}</span>
                  <span className="date">{formatDate(question.createdAt)}</span>
                  <span className="stats">
                    💬 답변 {question.answerCount || 0} ·
                    👁️ 조회 {question.viewCount} ·
                    👍 추천 {question.upvoteCount}
                  </span>
                </div>
              </div>
            </div>
          ))
        )}
      </div>
    </div>
  );
}

export default QuestionListPage;
