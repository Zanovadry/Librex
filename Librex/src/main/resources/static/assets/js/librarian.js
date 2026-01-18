import { apiFetch } from "./api.js";
import { openReturnModal, setReturnSuccessCallback } from "./returnModal.js";
import { showGeneralMessage } from "./uiMessages.js";

let selectedUser = { id: null, email: "" };

export function openLibrarianModal() {
  document.getElementById("librarian-modal-backdrop")?.classList.add("active");
  document.getElementById("lib-search-input")?.focus();
}

export function closeLibrarianModal() {
  document.getElementById("librarian-modal-backdrop")?.classList.remove("active");
}

export function bindLibrarianEvents() {
  const searchBtn = document.getElementById("lib-search-btn");
  const searchInput = document.getElementById("lib-search-input");

  if (searchBtn) {
    searchBtn.onclick = () => {
      const q = (searchInput?.value || "").trim();
      searchUsers(q);
    };
  }

  // po udanym zwrocie odświeżamy aktualnie wybranego usera (bez F5)
  setReturnSuccessCallback(async () => {
    if (selectedUser.id) {
      await loadUser(selectedUser.id, selectedUser.email, true);
    }
  });
}

async function searchUsers(query) {
  if (!query || query.length < 2) return;

  try {
    const res = await apiFetch(`/api/librarian/users/search?query=${encodeURIComponent(query)}`);
    if (!res.ok) return;

    const users = await res.json();
    renderUsers(Array.isArray(users) ? users : []);
  } catch (e) {
    console.error(e);
  }
}

function renderUsers(users) {
  const container = document.getElementById("lib-search-results");
  if (!container) return;

  container.innerHTML = "";

  if (!users.length) {
    container.innerHTML =
      '<div style="font-size: 13px; color: var(--muted);">Brak wyników.</div>';
    return;
  }

  users.forEach((u) => {
    const div = document.createElement("div");
    div.className = "profile-row";
    div.style.cursor = "pointer";
    div.style.padding = "8px";
    div.style.background = "var(--bg-soft)";
    div.style.borderRadius = "8px";
    div.innerHTML = `
      <span style="font-weight: 600;">${u.email}</span>
      <span style="font-size: 12px; color: var(--muted);">@${u.username}</span>
    `;
    div.onclick = () => loadUser(u.id, u.email, true);
    container.appendChild(div);
  });
}

async function loadUser(id, email, showSection) {
  selectedUser = { id, email };

  document.getElementById("lib-selected-user-name").textContent = email;

  if (showSection) {
    document.getElementById("lib-user-details")?.classList.remove("hidden");
  }

  const container = document.getElementById("lib-user-reservations");
  if (container) container.innerHTML = '<div style="font-size: 12px;">Ładowanie...</div>';

  try {
    const res = await apiFetch(`/api/librarian/users/${id}`);
    if (!res.ok) {
      const txt = await res.text().catch(() => "");
      if (container)
        container.innerHTML =
          '<div style="color: var(--danger);">Błąd pobierania danych.</div>';
      if (txt) showGeneralMessage(txt, "error");
      return;
    }

    const data = await res.json();
    renderReservations(data?.activeReservations || []);
  } catch (e) {
    if (container)
      container.innerHTML =
        '<div style="color: var(--danger);">Błąd pobierania danych.</div>';
  }
}

function renderReservations(reservations) {
  const container = document.getElementById("lib-user-reservations");
  if (!container) return;

  container.innerHTML = "";

  if (!reservations || reservations.length === 0) {
    container.innerHTML =
      '<div style="font-style: italic; color: var(--muted);">Brak aktywnych wypożyczeń.</div>';
    return;
  }

  reservations.forEach((res) => {
    const div = document.createElement("div");
    div.className = "profile-row";
    div.style.background = "rgba(15, 23, 42, 0.5)";
    div.style.padding = "8px";
    div.style.borderRadius = "8px";
    div.innerHTML = `
      <div style="display: flex; flex-direction: column;">
        <span style="font-weight: 600;">${res.inventoryNumber} - ${res.bookTitle}</span>
        <span style="font-size: 11px; color: var(--muted);">Termin: ${res.expectedReturnDate}</span>
      </div>
      <button class="btn btn-primary btn-small">Zwróć</button>
    `;

    div.querySelector("button").onclick = () => {
      openReturnModal(res.copyId, res.inventoryNumber);
    };

    container.appendChild(div);
  });
}
