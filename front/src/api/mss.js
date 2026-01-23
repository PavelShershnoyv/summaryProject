const MSS_BASE = 'http://localhost:8082';

export async function getAllMessages() {
  const token = localStorage.getItem('jwt');
  if (!token) {
    throw new Error('NO_AUTH_TOKEN');
  }
  const res = await fetch(`${MSS_BASE}/mss/messages`, {
    method: 'GET',
    headers: {
      'Authorization': `Bearer ${token}`,
      'Accept': 'application/json',
    },
  });
  if (!res.ok) {
    if (res.status === 401) {
      throw new Error('UNAUTHORIZED');
    }
    if (res.status === 403) {
      throw new Error('FORBIDDEN');
    }
    throw new Error('MSS_MESSAGES_FAILED');
  }
  return res.json();
}

export async function createMessage(payload) {
  const token = localStorage.getItem('jwt');
  if (!token) {
    throw new Error('NO_AUTH_TOKEN');
  }
  const res = await fetch(`${MSS_BASE}/mss/messages`, {
    method: 'POST',
    headers: {
      'Authorization': `Bearer ${token}`,
      'Content-Type': 'application/json',
      'Accept': 'application/json',
    },
    body: JSON.stringify(payload),
  });
  if (!res.ok) {
    if (res.status === 401) {
      throw new Error('UNAUTHORIZED');
    }
    if (res.status === 403) {
      throw new Error('FORBIDDEN');
    }
    throw new Error('MSS_CREATE_FAILED');
  }
  return res.json();
}

export async function deleteMessage(id) {
  const token = localStorage.getItem('jwt');
  if (!token) {
    throw new Error('NO_AUTH_TOKEN');
  }
  const res = await fetch(`${MSS_BASE}/mss/messages/${id}`, {
    method: 'DELETE',
    headers: {
      'Authorization': `Bearer ${token}`,
      'Accept': 'application/json',
    },
  });
  if (!res.ok) {
    if (res.status === 401) {
      throw new Error('UNAUTHORIZED');
    }
    if (res.status === 403) {
      throw new Error('FORBIDDEN');
    }
    throw new Error('MSS_DELETE_FAILED');
  }
  return true;
}

export async function subscribe({ subscriberId, producerId }) {
  const token = localStorage.getItem('jwt');
  if (!token) {
    throw new Error('NO_AUTH_TOKEN');
  }
  const res = await fetch(`${MSS_BASE}/mss/subscribe`, {
    method: 'POST',
    headers: {
      'Authorization': `Bearer ${token}`,
      'Content-Type': 'application/json',
      'Accept': 'application/json',
    },
    body: JSON.stringify({ subscriberId, producerId }),
  });
  if (!res.ok) {
    if (res.status === 401) {
      throw new Error('UNAUTHORIZED');
    }
    if (res.status === 403) {
      throw new Error('FORBIDDEN');
    }
    throw new Error('MSS_SUBSCRIBE_FAILED');
  }
  return true;
}

export async function unsubscribe({ subscriberId, producerId }) {
  const token = localStorage.getItem('jwt');
  if (!token) {
    throw new Error('NO_AUTH_TOKEN');
  }
  const res = await fetch(`${MSS_BASE}/mss/subscribe`, {
    method: 'DELETE',
    headers: {
      'Authorization': `Bearer ${token}`,
      'Content-Type': 'application/json',
      'Accept': 'application/json',
    },
    body: JSON.stringify({ subscriberId, producerId }),
  });
  if (!res.ok) {
    if (res.status === 401) {
      throw new Error('UNAUTHORIZED');
    }
    if (res.status === 403) {
      throw new Error('FORBIDDEN');
    }
    throw new Error('MSS_UNSUBSCRIBE_FAILED');
  }
  return true;
}

export async function getMessagesBySubscriber(subscriberId) {
  const token = localStorage.getItem('jwt');
  if (!token) {
    throw new Error('NO_AUTH_TOKEN');
  }
  const res = await fetch(`${MSS_BASE}/mss/messages/subscriber/${subscriberId}`, {
    method: 'GET',
    headers: {
      'Authorization': `Bearer ${token}`,
      'Accept': 'application/json',
    },
  });
  if (!res.ok) {
    if (res.status === 401) {
      throw new Error('UNAUTHORIZED');
    }
    if (res.status === 403) {
      throw new Error('FORBIDDEN');
    }
    throw new Error('MSS_SUBSCRIBER_MESSAGES_FAILED');
  }
  return res.json();
}

export async function isSubscribed({ subscriberId, producerId }) {
  const token = localStorage.getItem('jwt');
  if (!token) {
    throw new Error('NO_AUTH_TOKEN');
  }
  const res = await fetch(`${MSS_BASE}/mss/subscribe/status?subscriberId=${subscriberId}&producerId=${producerId}`, {
    method: 'GET',
    headers: {
      'Authorization': `Bearer ${token}`,
      'Accept': 'application/json',
    },
  });
  if (!res.ok) {
    if (res.status === 401) {
      throw new Error('UNAUTHORIZED');
    }
    if (res.status === 403) {
      throw new Error('FORBIDDEN');
    }
    throw new Error('MSS_SUBSCRIBE_STATUS_FAILED');
  }
  return res.json();
}
