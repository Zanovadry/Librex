import { apiFetch } from "./api.js";
import { setAuth, clearAuth, setCurrentUser, getCurrentUser } from "./state.js";
import { showMessage, showGeneralMessage } from "./uiMessages.js";
import { fetchBooks } from "./books.js";
import { refreshNotificationsUI } from "./notifications.js";
import { updateProfileUI } from "./profile.js";

export function switchAuthTab(tab) {
  const loginForm = document.getElementById("login-form");
  const registerForm = document.getElementById("register-form");
  const tabs = document.querySelectorAll(".tab");

  tabs.forEach((t) => t.classList.toggle("active", t.dataset.tab === tab));

  if (tab === "login") {
    loginForm?.classList.remove("hidden");
    registerForm?.classList.add("hidden");
  } else {
    loginForm?.classList.add("hidden");
    registerForm?.classList.remove("hidden");
  }
  showMessage("", "");
}

export function showMainScreen() {
  document.getElementById("auth-screen")?.classList.add("hidden");
  document.getElementById("main-screen")?.classList.remove("hidden");
}

export function showAuthScreen() {
  document.getElementById("main-screen")?.classList.add("hidden");
  document.getElementById("auth-screen")?.classList.remove("hidden");
}

export async function fetchCurrentUser(showErrors = true) {
  try {
    const res = await apiFetch("/api/auth/me");

    if (!res.ok) {
      if (showErrors) showMessage("Nie udało się pobrać danych użytkownika.", "error");
      return null;
    }

    const userDetails = await res.json();
    setCurrentUser(userDetails.user);
    updateProfileUI(userDetails);
    return userDetails.user;
  } catch (e) {
    console.error(e);
    if (showErrors) showMessage("Błąd połączenia z backendem.", "error");
    return null;
  }
}

export async function login(username, password) {
  setAuth(username, password);

  const user = await fetchCurrentUser(true);
  if (!user) {
    clearAuth();
    throw new Error("Login failed");
  }

  showMainScreen();
  await fetchBooks();
  await refreshNotificationsUI();
}

export async function registerUser(data) {
  try {
    const res = await apiFetch("/api/auth/register", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(data),
    });

    if (!res.ok) {
      const text = await res.text();
      console.error("Register error:", text);

      let displayMsg = "Rejestracja nie powiodła się.";
      try {
        if (text.trim().startsWith("{")) {
          const errors = JSON.parse(text);
          const messages = Object.values(errors).join(", ");
          if (messages) displayMsg = messages;
        } else if (text && text.length < 200 && !text.startsWith("<")) {
          displayMsg = text;
        }
      } catch {}

      showMessage(displayMsg, "error");
      return false;
    }

    await res.json().catch(() => null);
    showMessage("Konto utworzone! Teraz zaloguj się danymi, które podałeś.", "success");
    return true;
  } catch (e) {
    console.error(e);
    showMessage("Błąd połączenia przy rejestracji.", "error");
    return false;
  }
}

export function logout() {
  clearAuth();
  showAuthScreen();
  showGeneralMessage("Wylogowano pomyślnie.", "info");
}

export function getCurrentUserSafe() {
  return getCurrentUser();
}
