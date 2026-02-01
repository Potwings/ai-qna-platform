import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import QuestionListPage from './pages/QuestionListPage';
import QuestionDetailPage from './pages/QuestionDetailPage';
import QuestionFormPage from './pages/QuestionFormPage';
import './App.css';

function App() {
  return (
    <Router>
      <div className="App">
        <Routes>
          <Route path="/" element={<QuestionListPage />} />
          <Route path="/questions/new" element={<QuestionFormPage />} />
          <Route path="/questions/:id" element={<QuestionDetailPage />} />
        </Routes>
      </div>
    </Router>
  );
}

export default App;
