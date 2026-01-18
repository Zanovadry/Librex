import { switchAuthTab, login, registerUser, fetchCurrentUser, logout, showMainScreen, showAuthScreen } from "./auth.js";
import { fetchBooks } from "./books.js";
import { openNotificationsModal, closeNotificationsModal, refreshNotificationsUI } from "./notifications.js";
import { openLibrarianModal, closeLibrarianModal, bindLibrarianEvents } from "./librarian.js";
import { closeBookDetailsModal } from "./bookDetails.js";
import { bindReturnEvents } from "./returnModal.js";
import { setAuth, clearAuth, getCurrentUser } from "./state.js";
import { showMessage, showGeneralMessage } from "./uiMessages.js";

document.addEventListener("DOMContentLoaded", async () => {
  // Tabs
  document.querySelectorAll(".tab").forEach((tab) =>
    tab.addEventListener("click", () => switchAuthTab(tab.dataset.tab))
  );

  document.getElementById("go-register")?.addEventListener("click", () => switchAuthTab("register"));
  document.getElementById("go-login")?.addEventListener("click", () => switchAuthTab("login"));

  // Login
  document.getElementById("login-form")?.addEventListener("submit", async (e) => {
    e.preventDefault();

    const username = document.getElementById("login-username")?.value.trim();
    const password = document.getElementById("login-password")?.value;

    if (!username || !password) {
      showMessage("Podaj login i hasło.", "error");
      return;
    }

    try {
      showMessage("Logowanie...", "info");
      await login(username, password);
      showMessage("", "");
      showGeneralMessage("Zalogowano pomyślnie.", "success");
    } catch {
      showMessage("Niepoprawne dane logowania.", "error");
    }
  });

  // Register
  document.getElementById("register-form")?.addEventListener("submit", async (e) => {
    e.preventDefault();

    const data = {
      firstname: document.getElementById("reg-firstname")?.value.trim(),
      surname: document.getElementById("reg-surname")?.value.trim(),
      email: document.getElementById("reg-email")?.value.trim(),
      username: document.getElementById("reg-username")?.value.trim(),
      password: document.getElementById("reg-password")?.value,
    };

    if (Object.values(data).some((v) => !v)) {
      showMessage("Uzupełnij wszystkie pola.", "error");
      return;
    }

    const ok = await registerUser(data);
    if (ok) switchAuthTab("login");
  });

  // Profile modal (1:1 jak w single-file)
  const profileBtn = document.getElementById("profile-button");
  const profileBackdrop = document.getElementById("profile-modal-backdrop");
  const profileClose = document.getElementById("profile-close");

  profileBtn?.addEventListener("click", () => {
    if (getCurrentUser()) profileBackdrop?.classList.add("active");
    else showGeneralMessage("Zaloguj się, aby zobaczyć profil.", "error");
  });

  profileClose?.addEventListener("click", () => profileBackdrop?.classList.remove("active"));

  profileBackdrop?.addEventListener("click", (e) => {
    if (e.target.id === "profile-modal-backdrop") profileBackdrop.classList.remove("active");
  });

  document.getElementById("logout-button")?.addEventListener("click", () => {
    logout();
    profileBackdrop?.classList.remove("active");
  });

  // Notifications (1:1)
  const notifBtn = document.getElementById("notifications-button");
  const notifBackdrop = document.getElementById("notifications-modal-backdrop");

  notifBtn?.addEventListener("click", async () => {
    if (!getCurrentUser()) {
      showGeneralMessage("Zaloguj się, aby zobaczyć powiadomienia.", "error");
      return;
    }
    openNotificationsModal();
    await refreshNotificationsUI();
  });

  document.getElementById("notifications-close")?.addEventListener("click", closeNotificationsModal);

  notifBackdrop?.addEventListener("click", (e) => {
    if (e.target.id === "notifications-modal-backdrop") closeNotificationsModal();
  });

  document.getElementById("notifications-refresh")?.addEventListener("click", refreshNotificationsUI);

  // Librarian
  document.getElementById("librarian-panel-btn")?.addEventListener("click", () => {
    if (!getCurrentUser()) {
      showGeneralMessage("Zaloguj się, aby użyć panelu bibliotekarza.", "error");
      return;
    }
    openLibrarianModal();
  });

  document.getElementById("librarian-close")?.addEventListener("click", closeLibrarianModal);

  document.getElementById("librarian-modal-backdrop")?.addEventListener("click", (e) => {
    if (e.target.id === "librarian-modal-backdrop") closeLibrarianModal();
  });

  bindLibrarianEvents();

  // Book details modal close + click on backdrop (1:1)
  document.getElementById("book-details-close")?.addEventListener("click", closeBookDetailsModal);
  document.getElementById("book-details-modal-backdrop")?.addEventListener("click", (e) => {
    if (e.target.id === "book-details-modal-backdrop") closeBookDetailsModal();
  });

  // Return modal events
  bindReturnEvents();

  // Auto-login (1:1)
  const stored = localStorage.getItem("librexAuth");
  if (stored) {
    try {
      const parsed = JSON.parse(stored);
      if (parsed.username && parsed.password) {
        setAuth(parsed.username, parsed.password);
        const user = await fetchCurrentUser(false);

        if (user) {
          showMainScreen();
          await fetchBooks();
          showGeneralMessage("Zalogowano automatycznie.", "success");
        } else {
          clearAuth();
          showAuthScreen();
        }
      }
    } catch (e) {
      console.error(e);
      clearAuth();
      showAuthScreen();
    }
  }
});
