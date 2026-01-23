import { useEffect, useState } from 'react';
import { getAllMessages, deleteMessage } from '../../api/mss';
import { getUserInfo } from '../../api/ums';

function ViewPostsTab({ onOpenProfile }) {
  const [messages, setMessages] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [query, setQuery] = useState('');
  const [authors, setAuthors] = useState({});
  const [roles, setRoles] = useState([]);
  const [currentUserId, setCurrentUserId] = useState(null);

  useEffect(() => {
    getAllMessages()
      .then((data) => setMessages(Array.isArray(data) ? data : []))
      .catch((e) => {
        const msg = String(e?.message);
        if (msg === 'NO_AUTH_TOKEN' || msg === 'UNAUTHORIZED' || msg === 'FORBIDDEN') {
          setError('Вы не авторизованы. Войдите, чтобы видеть посты.');
        } else {
          setError('Ошибка загрузки сообщений');
        }
      })
      .finally(() => setLoading(false));
  }, []);

  useEffect(() => {
    const uid = localStorage.getItem('userId');
    setCurrentUserId(uid ? Number(uid) : null);
    if (uid) {
      getUserInfo(uid)
        .then((u) => setRoles(u.roles || []))
        .catch(() => {});
    }
  }, []);

  useEffect(() => {
    const ids = [...new Set(messages.map((m) => m.producerId).filter(Boolean))];
    const missing = ids.filter((id) => typeof authors[id] === 'undefined');
    if (missing.length === 0) return;
    Promise.all(
      missing.map((id) =>
        getUserInfo(id)
          .then((u) => ({ id, u }))
          .catch(() => ({ id, u: null }))
      )
    ).then((results) => {
      const next = { ...authors };
      results.forEach(({ id, u }) => {
        if (u) {
          next[id] = { firstName: u.firstName, lastName: u.lastName, username: u.username };
        } else {
          next[id] = null;
        }
      });
      setAuthors(next);
    });
  }, [messages, authors]);

  const filtered = messages.filter((m) =>
    (m.content || '').toLowerCase().includes(query.toLowerCase())
  );

  const fmt = (epoch) => {
    if (!epoch) return '';
    const d = new Date(epoch * 1000);
    return d.toLocaleString();
  };
  const authorName = (m) => {
    const a = authors[m.producerId];
    if (!a) {
      return `user_${m.producerId}`;
    }
    const name = [a.firstName, a.lastName].filter(Boolean).join(' ');
    return name || a.username || `user_${m.producerId}`;
  };

  const canDelete = (m) => {
    if (!currentUserId) return false;
    return currentUserId === m.producerId || roles.includes('ADMIN');
  };

  const handleDelete = async (m) => {
    try {
      await deleteMessage(m.id);
      setMessages((prev) => prev.filter((x) => x.id !== m.id));
    } catch (e) {
      const msg = String(e?.message);
      if (msg === 'NO_AUTH_TOKEN' || msg === 'UNAUTHORIZED' || msg === 'FORBIDDEN') {
        setError('Недостаточно прав для удаления');
      } else {
        setError('Ошибка удаления сообщения');
      }
    }
  };

  return (
    <div>
      <div className="d-flex justify-content-between align-items-center mb-3">
        <h5 className="mb-0">Посты</h5>
        <div className="input-group" style={{ maxWidth: 340 }}>
          <span className="input-group-text">Поиск</span>
          <input
            className="form-control"
            placeholder="Введите запрос"
            value={query}
            onChange={(e) => setQuery(e.target.value)}
          />
        </div>
      </div>
      {loading && <div className="text-muted">Загрузка...</div>}
      {error && <div className="alert alert-danger">{error}</div>}
      {!loading && !error && (
        <div className="row g-3">
          {filtered.length === 0 && (
            <div className="col-12">
              <div className="alert alert-secondary">Сообщения отсутствуют</div>
            </div>
          )}
          {filtered.map((m) => (
            <div className="col-md-6" key={m.id}>
              <div
                className="card shadow-sm"
                role="button"
                onClick={() => onOpenProfile && onOpenProfile(m.producerId)}
              >
                <div className="card-body">
                  <div className="d-flex justify-content-between">
                    <h6 className="card-subtitle text-muted">Автор: {authorName(m)}</h6>
                    <span className="text-muted">{fmt(m.created)}</span>
                  </div>
                  <p className="card-text mt-2">{m.content}</p>
                  <div className="text-end">
                    {canDelete(m) && (
                      <button
                        className="btn btn-outline-danger btn-sm"
                        onClick={(e) => { e.stopPropagation(); handleDelete(m); }}
                      >
                        Удалить
                      </button>
                    )}
                  </div>
                </div>
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  );
}

export default ViewPostsTab;
