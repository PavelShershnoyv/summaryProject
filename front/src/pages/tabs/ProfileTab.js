import { useEffect, useState } from 'react';
import { getUserInfo } from '../../api/ums';
import { subscribe, unsubscribe, isSubscribed } from '../../api/mss';

function ProfileTab({ userId }) {
  const [user, setUser] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [info, setInfo] = useState('');
  const [subscribed, setSubscribed] = useState(false);

  useEffect(() => {
    const currentId = localStorage.getItem('userId');
    const id = userId || currentId;
    if (!id) {
      setError('Вы не авторизованы');
      setLoading(false);
      return;
    }
    getUserInfo(id)
      .then((u) => setUser(u))
      .catch(() => setError('Ошибка загрузки профиля'))
      .finally(() => setLoading(false));
  }, [userId]);

  const fullName = (u) => {
    const name = [u?.firstName, u?.lastName].filter(Boolean).join(' ');
    return name || '';
  };
  const isOwn = () => {
    const currentId = localStorage.getItem('userId');
    return String(user?.id || '') === String(currentId || '');
  };
  useEffect(() => {
    const subscriberIdStr = localStorage.getItem('userId');
    const subscriberId = subscriberIdStr ? Number(subscriberIdStr) : null;
    const producerId = user?.id;
    if (!subscriberId || !producerId || String(producerId || '') === String(subscriberId || '')) {
      setSubscribed(false);
      return;
    }
    isSubscribed({ subscriberId, producerId })
      .then((status) => setSubscribed(Boolean(status)))
      .catch(() => setSubscribed(false));
  }, [user]);
  const handleSubscribe = async () => {
    setInfo('');
    const subscriberIdStr = localStorage.getItem('userId');
    const subscriberId = subscriberIdStr ? Number(subscriberIdStr) : null;
    const producerId = user?.id;
    if (!subscriberId || !producerId) {
      setError('Вы не авторизованы');
      return;
    }
    try {
      await subscribe({ subscriberId, producerId });
      setInfo('Подписка оформлена');
      setSubscribed(true);
    } catch (e) {
      const msg = String(e?.message);
      if (msg === 'NO_AUTH_TOKEN' || msg === 'UNAUTHORIZED' || msg === 'FORBIDDEN') {
        setError('Недостаточно прав для подписки');
      } else {
        setError('Ошибка оформления подписки');
      }
    }
  };
  const handleUnsubscribe = async () => {
    setInfo('');
    const subscriberIdStr = localStorage.getItem('userId');
    const subscriberId = subscriberIdStr ? Number(subscriberIdStr) : null;
    const producerId = user?.id;
    if (!subscriberId || !producerId) {
      setError('Вы не авторизованы');
      return;
    }
    try {
      await unsubscribe({ subscriberId, producerId });
      setInfo('Подписка отменена');
      setSubscribed(false);
    } catch (e) {
      const msg = String(e?.message);
      if (msg === 'NO_AUTH_TOKEN' || msg === 'UNAUTHORIZED' || msg === 'FORBIDDEN') {
        setError('Недостаточно прав для отмены подписки');
      } else {
        setError('Ошибка отмены подписки');
      }
    }
  };

  return (
    <div className="row g-4">
      <div className="col-lg-4">
        <div className="card shadow-sm">
          <div className="card-body text-center">
            <div className="rounded-circle bg-secondary" style={{ width: 96, height: 96, margin: '0 auto' }}></div>
            {loading ? (
              <h5 className="mt-3 mb-0 text-muted">Загрузка...</h5>
            ) : error ? (
              <h5 className="mt-3 mb-0 text-danger">{error}</h5>
            ) : (
              <>
                <h5 className="mt-3 mb-0">{user?.username}</h5>
                <p className="text-muted mb-2">{fullName(user)}</p>
                <div className="d-grid gap-2">
                  {isOwn() ? (
                    <button className="btn btn-outline-primary">Редактировать профиль</button>
                  ) : (
                    subscribed ? (
                      <button className="btn btn-outline-danger" onClick={handleUnsubscribe}>Отписаться</button>
                    ) : (
                      <button className="btn btn-primary" onClick={handleSubscribe}>Подписаться</button>
                    )
                  )}
                </div>
                {info && <div className="alert alert-success mt-3 mb-0">{info}</div>}
              </>
            )}
          </div>
        </div>
      </div>
      <div className="col-lg-8">
        <div className="card shadow-sm">
          <div className="card-body">
            <h6 className="text-uppercase text-muted mb-3">Информация</h6>
            {loading && <div className="text-muted">Загрузка...</div>}
            {!loading && error && <div className="alert alert-danger mb-0">{error}</div>}
            {!loading && !error && user && (
              <>
                <div className="row mb-2">
                  <div className="col-sm-4 text-muted">Email</div>
                  <div className="col-sm-8">{user.email || '—'}</div>
                </div>
                <div className="row mb-2">
                  <div className="col-sm-4 text-muted">Роли</div>
                  <div className="col-sm-8">{(user.roles || []).join(', ') || '—'}</div>
                </div>
                {/* <div className="row mb-2">
                  <div className="col-sm-4 text-muted">Последний визит</div>
                  <div className="col-sm-8">{user.lastVisitId != null ? user.lastVisitId : '—'}</div>
                </div> */}
              </>
            )}
          </div>
        </div>
      </div>
    </div>
  );
}

export default ProfileTab;
