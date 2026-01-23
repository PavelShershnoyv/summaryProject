const UMS_BASE = 'http://localhost:8084';

export async function login(credentials) {
  const res = await fetch(`${UMS_BASE}/ums/authorization`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
      'Accept': 'application/json',
    },
    body: JSON.stringify(credentials),
  });
  if (!res.ok) {
    if (res.status === 403) {
      throw new Error('AUTH_FORBIDDEN');
    }
    if (res.status === 401) {
      throw new Error('AUTH_UNAUTHORIZED');
    }
    throw new Error('AUTH_FAILED');
  }
  return res.json();
}

export async function register(payload) {
  const res = await fetch(`${UMS_BASE}/ums/registration`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
      'Accept': 'application/json',
    },
    body: JSON.stringify(payload),
  });
  if (!res.ok) {
    if (res.status === 403) {
      throw new Error('REGISTER_FORBIDDEN');
    }
    if (res.status === 401) {
      throw new Error('REGISTER_UNAUTHORIZED');
    }
    throw new Error('REGISTER_FAILED');
  }
  return res.json();
}

export async function getUserInfo(id) {
  const token = localStorage.getItem('jwt');
  const res = await fetch(`${UMS_BASE}/ums/get/info_user/${id}`, {
    method: 'GET',
    headers: {
      'Authorization': `Bearer ${token}`,
      'Accept': 'application/json',
    },
  });
  if (!res.ok) {
    throw new Error('USER_INFO_FAILED');
  }
  return res.json();
}
