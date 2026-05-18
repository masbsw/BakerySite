export const ADMIN_TOKEN_KEY = 'adminToken';
export const ADMIN_USERNAME_KEY = 'adminUsername';
export const ADMIN_ROLE_KEY = 'adminRole';

export function saveAdminSession(authResponse) {
  localStorage.setItem(ADMIN_TOKEN_KEY, authResponse.token);
  localStorage.setItem(ADMIN_USERNAME_KEY, authResponse.username);
  localStorage.setItem(ADMIN_ROLE_KEY, authResponse.role);
}

export function clearAdminSession() {
  localStorage.removeItem(ADMIN_TOKEN_KEY);
  localStorage.removeItem(ADMIN_USERNAME_KEY);
  localStorage.removeItem(ADMIN_ROLE_KEY);
}

export function getAdminToken() {
  return localStorage.getItem(ADMIN_TOKEN_KEY);
}

export function getAdminRole() {
  return localStorage.getItem(ADMIN_ROLE_KEY);
}

export function getAdminAuthHeaders() {
  const token = getAdminToken();
  return token ? { Authorization: `Bearer ${token}` } : {};
}
