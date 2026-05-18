export const USER_TOKEN_KEY = 'userToken';
export const USER_ROLE_KEY = 'userRole';
export const USER_USERNAME_KEY = 'userUsername';
const USER_AUTH_EVENT = 'user-auth-changed';

function notifyUserAuthChanged() {
  window.dispatchEvent(new Event(USER_AUTH_EVENT));
}

export function saveUserSession(authResponse) {
  localStorage.setItem(USER_TOKEN_KEY, authResponse.token);
  localStorage.setItem(USER_ROLE_KEY, authResponse.role);
  localStorage.setItem(USER_USERNAME_KEY, authResponse.username);
  notifyUserAuthChanged();
}

export function clearUserSession() {
  localStorage.removeItem(USER_TOKEN_KEY);
  localStorage.removeItem(USER_ROLE_KEY);
  localStorage.removeItem(USER_USERNAME_KEY);
  notifyUserAuthChanged();
}

export function getUserToken() {
  return localStorage.getItem(USER_TOKEN_KEY);
}

export function getUserRole() {
  return localStorage.getItem(USER_ROLE_KEY);
}

export function getUserUsername() {
  return localStorage.getItem(USER_USERNAME_KEY);
}

export function getUserAuthHeaders() {
  const token = getUserToken();
  return token ? { Authorization: `Bearer ${token}` } : {};
}

export function isUserAuthenticated() {
  return Boolean(getUserToken());
}

export function getUserAuthEventName() {
  return USER_AUTH_EVENT;
}
