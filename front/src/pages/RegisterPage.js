import { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { register } from '../api/ums';

function RegisterPage() {
  const [form, setForm] = useState({
    username: '',
    email: '',
    firstName: '',
    lastName: '',
    password: '',
  });
  const [error, setError] = useState('');
  const navigate = useNavigate();

  const handleChange = (event) => {
    const { name, value } = event.target;
    setForm((prev) => ({ ...prev, [name]: value }));
  };

  const handleSubmit = async (event) => {
    event.preventDefault();
    setError('');
    try {
      await register({
        username: (form.username || '').trim(),
        email: form.email,
        firstName: (form.firstName || '').trim(),
        lastName: (form.lastName || '').trim(),
        password: (form.password || '').trim(),
      });
      navigate('/');
    } catch (e) {
      const msg = String(e?.message);
      if (msg === 'REGISTER_FORBIDDEN' || msg === 'REGISTER_UNAUTHORIZED') {
        setError('Нет доступа для регистрации');
      } else {
        setError('Ошибка регистрации');
      }
    }
  };

  return (
    <div className="container py-5">
      <div className="row justify-content-center">
        <div className="col-lg-7">
          <div className="card shadow-sm">
            <div className="card-body p-4">
              <h2 className="h4 mb-3">Регистрация</h2>
              <form onSubmit={handleSubmit}>
                <div className="row">
                  <div className="col-md-6 mb-3">
                    <label className="form-label">Имя</label>
                    <input
                      type="text"
                      name="firstName"
                      className="form-control"
                      value={form.firstName}
                      onChange={handleChange}
                      placeholder="Иван"
                    />
                  </div>
                  <div className="col-md-6 mb-3">
                    <label className="form-label">Фамилия</label>
                    <input
                      type="text"
                      name="lastName"
                      className="form-control"
                      value={form.lastName}
                      onChange={handleChange}
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
                    value={form.username}
                    onChange={handleChange}
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
                    value={form.email}
                    onChange={handleChange}
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
                    value={form.password}
                    onChange={handleChange}
                    placeholder="••••••••"
                    required
                  />
                </div>
                {error && <div className="alert alert-danger">{error}</div>}
                <button type="submit" className="btn btn-success w-100">
                  Создать аккаунт
                </button>
              </form>
              <div className="mt-3 text-center">
                <span className="text-muted">Уже есть аккаунт? </span>
                <Link to="/">Войти</Link>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}

export default RegisterPage;
