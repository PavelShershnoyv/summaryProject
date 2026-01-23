import { Link } from 'react-router-dom';

function HomePage() {
  return (
    <div className="container py-5">
      <div className="row align-items-center">
        <div className="col-lg-6">
          <h1 className="display-5 fw-bold mb-3">Добро пожаловать</h1>
          <p className="lead text-muted">
            UMS и MSS уже готовы к работе. Зарегистрируйтесь, войдите в систему и
            получите доступ к сервисам.
          </p>
          <div className="d-flex gap-2">
            <Link to="/login" className="btn btn-primary btn-lg">
              Войти
            </Link>
            <Link to="/register" className="btn btn-outline-secondary btn-lg">
              Регистрация
            </Link>
          </div>
        </div>
        <div className="col-lg-6 mt-4 mt-lg-0">
          <div className="card shadow-sm">
            <div className="card-body">
              <h5 className="card-title">Что дальше</h5>
              <ul className="list-group list-group-flush">
                <li className="list-group-item">Получить JWT в UMS</li>
                <li className="list-group-item">Авторизоваться в Swagger</li>
                <li className="list-group-item">Создавать сообщения и подписки</li>
              </ul>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}

export default HomePage;
