import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { questionAPI } from '../services/api';
import './QuestionFormPage.css';

function QuestionFormPage() {
  const navigate = useNavigate();
  const [formData, setFormData] = useState({
    title: '',
    content: '',
    userName: '',
  });
  const [submitting, setSubmitting] = useState(false);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData((prev) => ({
      ...prev,
      [name]: value,
    }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    if (!formData.title.trim() || !formData.content.trim()) {
      alert('제목과 내용을 모두 입력해주세요.');
      return;
    }

    try {
      setSubmitting(true);
      const response = await questionAPI.createQuestion({
        title: formData.title,
        content: formData.content,
        userName: formData.userName || '익명',
      });

      alert('질문이 등록되었습니다! AI가 자동으로 답변을 생성했습니다.');
      navigate(`/questions/${response.data.id}`);
    } catch (error) {
      console.error('질문 등록 실패:', error);
      alert('질문 등록에 실패했습니다: ' + (error.response?.data?.message || error.message));
    } finally {
      setSubmitting(false);
    }
  };

  return (
    <div className="question-form-page">
      <button className="back-button" onClick={() => navigate('/')}>
        ← 목록으로
      </button>

      <div className="form-container">
        <h1>질문 작성</h1>
        <p className="form-description">
          질문을 등록하시면 AI가 자동으로 첫 답변을 생성해드립니다.
        </p>

        <form onSubmit={handleSubmit}>
          <div className="form-group">
            <label htmlFor="userName">이름 (선택)</label>
            <input
              type="text"
              id="userName"
              name="userName"
              value={formData.userName}
              onChange={handleChange}
              placeholder="익명"
              className="form-input"
            />
          </div>

          <div className="form-group">
            <label htmlFor="title">제목 *</label>
            <input
              type="text"
              id="title"
              name="title"
              value={formData.title}
              onChange={handleChange}
              placeholder="질문 제목을 입력하세요"
              className="form-input"
              required
            />
          </div>

          <div className="form-group">
            <label htmlFor="content">내용 *</label>
            <textarea
              id="content"
              name="content"
              value={formData.content}
              onChange={handleChange}
              placeholder="질문 내용을 자세히 입력하세요"
              className="form-textarea"
              rows="10"
              required
            />
          </div>

          <div className="form-actions">
            <button
              type="button"
              className="cancel-button"
              onClick={() => navigate('/')}
            >
              취소
            </button>
            <button
              type="submit"
              className="submit-button"
              disabled={submitting}
            >
              {submitting ? '등록 중...' : '질문 등록'}
            </button>
          </div>
        </form>
      </div>
    </div>
  );
}

export default QuestionFormPage;
