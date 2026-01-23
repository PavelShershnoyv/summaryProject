import { useState } from 'react';
import { createMessage } from '../../api/mss';

function CreatePostTab() {
  const [text, setText] = useState('');
  const [status, setStatus] = useState('');
  const [loading, setLoading] = useState(false);

  const handlePublish = async () => {
    const content = (text || '').trim();
    if (!content) {
      setStatus('Введите текст сообщения');
      return;
    }
    const producerIdStr = localStorage.getItem('userId');
    const producerId = producerIdStr ? Number(producerIdStr) : null;
    if (!producerId) {
      setStatus('Вы не авторизованы');
      return;
    }
    setLoading(true);
    setStatus('');
    try {
      await createMessage({ content, producerId });
      setText('');
      setStatus('Сообщение опубликовано');
    } catch (e) {
      const msg = String(e?.message);
      if (msg === 'NO_AUTH_TOKEN' || msg === 'UNAUTHORIZED' || msg === 'FORBIDDEN') {
        setStatus('Недостаточно прав для публикации');
      } else {
        setStatus('Ошибка публикации');
      }
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="card shadow-sm">
      <div className="card-body">
        <h5 className="card-title mb-3">Создать пост</h5>
        <div className="mb-3">
          <label className="form-label">Текст сообщения</label>
          <textarea
            className="form-control"
            rows="4"
            placeholder="Введите текст поста"
            value={text}
            onChange={(e) => setText(e.target.value)}
          />
        </div>
        {status && <div className="alert alert-info">{status}</div>}
        <div className="d-flex justify-content-end">
          <button type="button" className="btn btn-primary" onClick={handlePublish} disabled={loading}>
            {loading ? 'Публикация...' : 'Опубликовать'}
          </button>
        </div>
      </div>
    </div>
  );
}

export default CreatePostTab;
