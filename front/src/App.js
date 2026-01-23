import './App.css';
import { BrowserRouter, NavLink, Route, Routes } from 'react-router-dom';
import LoginPage from './pages/LoginPage';
import RegisterPage from './pages/RegisterPage';
import MainPage from './pages/MainPage';

function App() {
  return (
    <BrowserRouter>
      <div className="app-root">
        <nav className="navbar navbar-expand-lg bg-white border-bottom">
          <div className="container">
            <NavLink to="/" className="navbar-brand fw-semibold">
              UMS/MSS
            </NavLink>
            <div className="navbar-nav ms-auto"></div>
          </div>
        </nav>
        <main>
          <Routes>
            <Route path="/" element={<LoginPage />} />
            <Route path="/register" element={<RegisterPage />} />
            <Route path="/app" element={<MainPage />} />
          </Routes>
        </main>
      </div>
    </BrowserRouter>
  );
}

export default App;
