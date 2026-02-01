import { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { questionAPI, answerAPI } from '../services/api';
import './QuestionDetailPage.css';

function QuestionDetailPage() {
  const { id } = useParams();
  const navigate = useNavigate();
  const [question, setQuestion] = useState(null);
  const [answerContent, setAnswerContent] = useState('');
  const [answerUserName, setAnswerUserName] = useState('');
  const [loading, setLoading] = useState(true);
  const [submitting, setSubmitting] = useState(false);

  useEffect(() => {
    fetchQuestion();
  }, [id]);

  const fetchQuestion = async () => {
    try {
      setLoading(true);
      const response = await questionAPI.getQuestion(id);
      setQuestion(response.data);
    } catch (error) {
      console.error('질문 조회 실패:', error);
      alert('질문을 불러오는데 실패했습니다.');
      navigate('/');
    } finally {
      setLoading(false);
    }
  };

  const handleUpvoteQuestion = async () => {
    try {
      await questionAPI.upvoteQuestion(id);
      fetchQuestion(); // 다시 불러와서 업데이트
    } catch (error) {
      console.error('질문 추천 실패:', error);
      alert('추천에 실패했습니다.');
    }
  };

  const handleUpvoteAnswer = async (answerId) => {
    try {
      await answerAPI.upvoteAnswer(answerId);
      fetchQuestion(); // 다시 불러와서 업데이트
    } catch (error) {
      console.error('답변 추천 실패:', error);
      alert('추천에 실패했습니다.');
    }
  };

  const handleAcceptAnswer = async (answerId) => {
    try {
      await answerAPI.acceptAnswer(answerId);
      fetchQuestion(); // 다시 불러와서 업데이트
    } catch (error) {
      console.error('답변 채택 실패:', error);
      alert('채택에 실패했습니다.');
    }
  };

  const handleSubmitAnswer = async (e) => {
    e.preventDefault();

    if (!answerContent.trim()) {
      alert('답변 내용을 입력해주세요.');
      return;
    }

    try {
      setSubmitting(true);
      await answerAPI.addAnswer(id, {
        content: answerContent,
        userName: answerUserName || '익명',
      });

      setAnswerContent('');
      setAnswerUserName('');
      fetchQuestion(); // 답변 목록 다시 불러오기
      alert('답변이 등록되었습니다.');
    } catch (error) {
      console.error('답변 등록 실패:', error);
      alert('답변 등록에 실패했습니다.');
    } finally {
      setSubmitting(false);
    }
  };

  const formatDate = (dateString) => {
    const date = new Date(dateString);
    return date.toLocaleString('ko-KR');
  };

  if (loading) {
    return <div className="loading">로딩 중...</div>;
  }

  if (!question) {
    return <div className="error">질문을 찾을 수 없습니다.</div>;
  }

  return (
    <div className="question-detail-page">
      <button className="back-button" onClick={() => navigate('/')}>
        ← 목록으로
      </button>

      {/* 질문 영역 */}
      <div className="question-section">
        <h1 className="question-title">{question.title}</h1>
        <div className="question-meta">
          <span className="author">{question.userName}</span>
          <span className="date">{formatDate(question.createdAt)}</span>
          <span className="stats">
            👁️ {question.viewCount} ·
            👍 {question.upvoteCount}
          </span>
        </div>
        <div className="question-content">
          {question.content}
        </div>
        <button className="upvote-button" onClick={handleUpvoteQuestion}>
          👍 추천 ({question.upvoteCount})
        </button>
      </div>

      {/* 답변 영역 */}
      <div className="answers-section">
        <h2 className="answers-title">
          답변 {question.answers?.length || 0}개
        </h2>

        {question.answers && question.answers.length > 0 ? (
          question.answers.map((answer) => (
            <div key={answer.id} className={`answer-item ${answer.aiGenerated ? 'ai-answer' : ''}`}>
              <div className="answer-header">
                <span className="answer-author">
                  {answer.userName}
                  {answer.aiGenerated && <span className="ai-badge">🤖 AI</span>}
                  {answer.accepted && <span className="accepted-badge">✅ 채택됨</span>}
                </span>
                <span className="answer-date">{formatDate(answer.createdAt)}</span>
              </div>
              <div className="answer-content">
                {answer.content.split('\n').map((line, idx) => (
                  <p key={idx}>{line}</p>
                ))}
              </div>
              <div className="answer-actions">
                <button
                  className="upvote-button small"
                  onClick={() => handleUpvoteAnswer(answer.id)}
                >
                  👍 추천 ({answer.upvoteCount})
                </button>
                {!answer.accepted && (
                  <button
                    className="accept-button small"
                    onClick={() => handleAcceptAnswer(answer.id)}
                  >
                    ✅ 채택하기
                  </button>
                )}
              </div>
            </div>
          ))
        ) : (
          <p className="no-answers">아직 답변이 없습니다.</p>
        )}
      </div>

      {/* 답변 작성 폼 */}
      <div className="answer-form-section">
        <h3>답변 작성</h3>
        <form onSubmit={handleSubmitAnswer}>
          <input
            type="text"
            placeholder="이름 (선택)"
            value={answerUserName}
            onChange={(e) => setAnswerUserName(e.target.value)}
            className="answer-input"
          />
          <textarea
            placeholder="답변을 입력하세요..."
            value={answerContent}
            onChange={(e) => setAnswerContent(e.target.value)}
            className="answer-textarea"
            rows="6"
            required
          />
          <button
            type="submit"
            className="submit-button"
            disabled={submitting}
          >
            {submitting ? '등록 중...' : '답변 등록'}
          </button>
        </form>
      </div>
    </div>
  );
}

export default QuestionDetailPage;
