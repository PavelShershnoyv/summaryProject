import { useEffect, useState } from 'react';
import { getUserInfo } from '../api/ums';
import CreatePostTab from './tabs/CreatePostTab';
import ViewPostsTab from './tabs/ViewPostsTab';
import ProfileTab from './tabs/ProfileTab';
import FollowingTab from './tabs/FollowingTab';

function MainPage() {
  const [active, setActive] = useState('create');
  const [roles, setRoles] = useState([]);
  const [loading, setLoading] = useState(true);
  const [selectedUserId, setSelectedUserId] = useState(null);

  useEffect(() => {
    const userId = localStorage.getItem('userId');
    if (!userId) {
      setLoading(false);
      return;
    }
    getUserInfo(userId)
      .then((u) => {
        setRoles(u.roles || []);
        setLoading(false);
      })
      .catch(() => setLoading(false));
  }, []);

  const openProfileById = (id) => {
    setSelectedUserId(id);
    setActive('profile');
  };

  return (
    <div className="container py-4">
      <ul className="nav nav-tabs">
        <li className="nav-item">
          <button
            className={`nav-link ${active === 'create' ? 'active' : ''}`}
            onClick={() => setActive('create')}
          >
            Создать пост
          </button>
        </li>
        <li className="nav-item">
          <button
            className={`nav-link ${active === 'view' ? 'active' : ''}`}
            onClick={() => setActive('view')}
          >
            Посты
          </button>
        </li>
        <li className="nav-item">
          <button
            className={`nav-link ${active === 'following' ? 'active' : ''}`}
            onClick={() => setActive('following')}
          >
            Отслеживаемое
          </button>
        </li>
        <li className="nav-item">
          <button
            className={`nav-link ${active === 'profile' ? 'active' : ''}`}
            onClick={() => { setSelectedUserId(null); setActive('profile'); }}
          >
            Профиль
          </button>
        </li>
      </ul>
      <div className="tab-content pt-4">
        {active === 'create' && (
          loading ? (
            <div className="text-muted">Загрузка...</div>
          ) : roles.includes('PRODUCER') ? (
            <CreatePostTab />
          ) : (
            <div className="alert alert-warning">
              у вас нет роля для публикации постов
            </div>
          )
        )}
        {active === 'view' && <ViewPostsTab onOpenProfile={openProfileById} />}
        {active === 'following' && <FollowingTab onOpenProfile={openProfileById} />}
        {active === 'profile' && <ProfileTab userId={selectedUserId} />}
      </div>
    </div>
  );
}

export default MainPage;
