import { useState } from 'react';
import { useNavigate } from 'react-router-dom';

function AuthPage() {
  const navigate = useNavigate();
  const [loginForm, setLoginForm] = useState({ username: '', password: '' });
  const [regForm, setRegForm] = useState({ username: '', email: '', firstName: '', lastName: '', password: '' });

  const onLoginChange = (e) => {
    const { name, value } = e.target;
    setLoginForm((prev) => ({ ...prev, [name]: value }));
  };
  const onRegChange = (e) => {
    const { name, value } = e.target;
    setRegForm((prev) => ({ ...prev, [name]: value }));
  };

  const onLoginSubmit = (e) => {
    e.preventDefault();
    navigate('/app');
  };

  const onRegSubmit = (e) => {
    e.preventDefault();
  };

  return (
    <div className="container py-5">
      <div className="row g-4">
        <div className="col-lg-5">
          <div className="card shadow-sm">
            <div className="card-body p-4">
              <h2 className="h4 mb-3">Авторизация</h2>
              <form onSubmit={onLoginSubmit}>
                <div className="mb-3">
                  <label className="form-label">Логин</label>
                  <input
                    type="text"
                    name="username"
                    className="form-control"
                    value={loginForm.username}
                    onChange={onLoginChange}
                    placeholder="ivan.petrov"
                    required
                  />
                </div>
                <div className="mb-3">
                  <label className="form-label">Пароль</label>
                  <input
                    type="password"
                    name="password"
                    className="form-control"
                    value={loginForm.password}
                    onChange={onLoginChange}
                    placeholder="••••••••"
                    required
                  />
                </div>
                <button type="submit" className="btn btn-primary w-100">
                  Войти
                </button>
              </form>
            </div>
          </div>
        </div>
        <div className="col-lg-7">
          <div className="card shadow-sm">
            <div className="card-body p-4">
              <h2 className="h4 mb-3">Регистрация</h2>
              <form onSubmit={onRegSubmit}>
                <div className="row">
                  <div className="col-md-6 mb-3">
                    <label className="form-label">Имя</label>
                    <input
                      type="text"
                      name="firstName"
                      className="form-control"
                      value={regForm.firstName}
                      onChange={onRegChange}
                      placeholder="Иван"
                    />
                  </div>
                  <div className="col-md-6 mb-3">
                    <label className="form-label">Фамилия</label>
                    <input
                      type="text"
                      name="lastName"
                      className="form-control"
                      value={regForm.lastName}
                      onChange={onRegChange}
                      placeholder="Петров"
                    />
                  </div>
                </div>
                <div className="mb-3">
                  <label className="form-label">Логин</label>
                  <input
                    type="text"
                    name="username"
                    className="form-control"
                    value={regForm.username}
                    onChange={onRegChange}
                    placeholder="ivan.petrov"
                    required
                  />
                </div>
                <div className="mb-3">
                  <label className="form-label">Email</label>
                  <input
                    type="email"
                    name="email"
                    className="form-control"
                    value={regForm.email}
                    onChange={onRegChange}
                    placeholder="ivan.petrov@example.com"
                    required
                  />
                </div>
                <div className="mb-4">
                  <label className="form-label">Пароль</label>
                  <input
                    type="password"
                    name="password"
                    className="form-control"
                    value={regForm.password}
                    onChange={onRegChange}
                    placeholder="••••••••"
                    required
                  />
                </div>
                <button type="submit" className="btn btn-success w-100">
                  Создать аккаунт
                </button>
              </form>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}

export default AuthPage;
