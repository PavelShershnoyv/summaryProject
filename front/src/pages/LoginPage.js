import { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { login } from '../api/ums';

function LoginPage() {
  const [form, setForm] = useState({ username: '', password: '' });
  const [error, setError] = useState('');
  const navigate = useNavigate();

  const decodeJwtPayload = (token) => {
    if (!token || typeof token !== 'string') return null;
    const parts = token.split('.');
    if (parts.length < 2) return null;
    let base64 = parts[1].replace(/-/g, '+').replace(/_/g, '/');
    const pad = base64.length % 4;
    if (pad) {
      base64 += '='.repeat(4 - pad);
    }
    try {
      const json = atob(base64);
      return JSON.parse(json);
    } catch {
      return null;
    }
  };

  const handleChange = (event) => {
    const { name, value } = event.target;
    setForm((prev) => ({ ...prev, [name]: value }));
  };

  const handleSubmit = async (event) => {
    event.preventDefault();
    setError('');
    try {
      const { token, userId } = await login({
        username: (form.username || '').trim(),
        password: (form.password || '').trim(),
      });
      localStorage.setItem('jwt', token);
      const payload = decodeJwtPayload(token);
      const idFromToken = payload && (payload.id != null ? payload.id : null);
      const finalId = idFromToken != null ? idFromToken : userId;
      if (finalId != null) {
        localStorage.setItem('userId', String(finalId));
      }
      navigate('/app');
    } catch (e) {
      const msg = String(e?.message);
      if (msg === 'AUTH_FORBIDDEN' || msg === 'AUTH_UNAUTHORIZED') {
        setError('Неверные учетные данные');
      } else {
        setError('Ошибка авторизации');
      }
    }
  };

  return (
    <div className="container py-5">
      <div className="row justify-content-center">
        <div className="col-md-6 col-lg-5">
          <div className="card shadow-sm">
            <div className="card-body p-4">
              <h2 className="h4 mb-3">Авторизация</h2>
              <form onSubmit={handleSubmit}>
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
                <button type="submit" className="btn btn-primary w-100">
                  Войти
                </button>
              </form>
              <div className="mt-3 text-center">
                <span className="text-muted">Нет аккаунта? </span>
                <Link to="/register">Регистрация</Link>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}

export default LoginPage;
