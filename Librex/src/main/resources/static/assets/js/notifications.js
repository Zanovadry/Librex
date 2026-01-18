import { apiFetch } from "./api.js";
import { showGeneralMessage } from "./uiMessages.js";
import { getCurrentUser } from "./state.js";

export function openNotificationsModal() {
  document.getElementById("notifications-modal-backdrop")?.classList.add("active");
}

export function closeNotificationsModal() {
  document.getElementById("notifications-modal-backdrop")?.classList.remove("active");
}

function formatDate(dt) {
  if (!dt) return "";
  return dt.replace("T", " ").slice(0, 16);
}

export async function refreshNotificationsUI() {
  // zgodnie ze single-file: jak nie ma usera, to nic
  if (!getCurrentUser()) return;

  const listEl = document.getElementById("notifications-list");
  if (!listEl) return;

  listEl.innerHTML = '<div style="font-size: 12px; color: var(--muted);">Ładowanie...</div>';

  try {
    const res = await apiFetch("/api/notifications");
    if (!res.ok) {
      const txt = await res.text().catch(() => "");
      listEl.innerHTML =
        '<div style="font-size: 12px; color: var(--danger);">Nie udało się pobrać powiadomień.</div>';
      if (txt) showGeneralMessage("Błąd: " + txt, "error");
      setBadge(0);
      return;
    }

    const list = await res.json();
    render(list);
    setBadge(Array.isArray(list) ? list.length : 0);
  } catch (e) {
    console.error(e);
    listEl.innerHTML =
      '<div style="font-size: 12px; color: var(--danger);">Nie udało się pobrać powiadomień.</div>';
  }
}

function setBadge(count) {
  const badge = document.getElementById("notifications-badge");
  if (!badge) return;

  if (count > 0) {
    badge.textContent = String(count);
    badge.classList.remove("hidden");
  } else {
    badge.textContent = "0";
    badge.classList.add("hidden");
  }
}

function render(list) {
  const container = document.getElementById("notifications-list");
  if (!container) return;

  container.innerHTML = "";

  if (!Array.isArray(list) || list.length === 0) {
    container.innerHTML =
      '<div style="font-size: 12px; color: var(--muted); font-style: italic;">Brak nieprzeczytanych powiadomień.</div>';
    return;
  }

  list.forEach((n) => {
    const card = document.createElement("div");
    card.style.background = "rgba(15, 23, 42, 0.5)";
    card.style.border = "1px solid rgba(148, 163, 184, 0.25)";
    card.style.borderRadius = "12px";
    card.style.padding = "10px";
    card.style.display = "flex";
    card.style.flexDirection = "column";
    card.style.gap = "6px";

    card.innerHTML = `
      <div style="display:flex; justify-content: space-between; gap: 12px; align-items: baseline;">
        <div style="font-weight: 700; color: var(--text);">${n.title}</div>
        <div style="font-size: 11px; color: var(--muted); white-space: nowrap;">
          ${formatDate(n.createdAt)}
        </div>
      </div>

      <div style="font-size: 12px; color: var(--muted); line-height: 1.45;">
        ${n.content}
      </div>

      <div style="display:flex; justify-content:flex-end; margin-top: 6px;">
        <button class="btn btn-primary btn-small notif-read-btn" data-id="${n.id}" type="button">
          Oznacz jako przeczytane
        </button>
      </div>
    `;

    card.querySelector(".notif-read-btn").onclick = async () => {
      try {
        const res = await apiFetch(`/api/notifications/${n.id}`, { method: "PATCH" });
        if (!res.ok) {
          const txt = await res.text().catch(() => "");
          showGeneralMessage(`Błąd: ${txt || "Nie udało się oznaczyć jako przeczytane."}`, "error");
          return;
        }
        showGeneralMessage("Oznaczono jako przeczytane.", "success");
        await refreshNotificationsUI();
      } catch (e) {
        showGeneralMessage("Błąd: " + e.message, "error");
      }
    };

    container.appendChild(card);
  });
}
