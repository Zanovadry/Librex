let auth = null; // { username, password }
let currentUser = null; // { id, username, email, role, firstname, surname }

export function setAuth(username, password) {
  auth = { username, password };
  localStorage.setItem("librexAuth", JSON.stringify(auth));
}

export function clearAuth() {
  auth = null;
  currentUser = null;
  localStorage.removeItem("librexAuth");
}

export function getAuth() {
  return auth;
}

export function setCurrentUser(u) {
  currentUser = u;
}

export function getCurrentUser() {
  return currentUser;
}
