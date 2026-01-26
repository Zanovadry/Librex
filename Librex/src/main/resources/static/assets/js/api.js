import { API_BASE } from "./config.js";
import { getAuth } from "./state.js";

export function getAuthHeader() {
  const auth = getAuth();
  if (!auth) return {};
  return {
    Authorization: "Basic " + btoa(auth.username + ":" + auth.password),
  };
}

export async function apiFetch(path, options = {}) {
  const res = await fetch(API_BASE + path, {
    ...options,
    headers: {
      ...(options.headers || {}),
      ...getAuthHeader(),
    },
  });
  return res;
}
