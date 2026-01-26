import { apiFetch } from "./api.js";
import { showGeneralMessage } from "./uiMessages.js";

function byId(id) {
  return document.getElementById(id);
}

export async function loadReviewsForBook(titleId) {
  const summary = byId("reviews-summary");
  const listEl = byId("reviews-list");
  if (!summary || !listEl) return;

  summary.textContent = "Ładowanie opinii...";
  listEl.innerHTML = "";

  try {
    const res = await apiFetch(`/api/reviews/title/${titleId}`);

    if (!res.ok) {
      const txt = await res.text().catch(() => "");
      summary.textContent = `Opinie: błąd (${res.status})${txt ? " – " + txt : ""}`;
      return;
    }

    const list = await res.json();

    if (!Array.isArray(list) || list.length === 0) {
      summary.textContent = "Brak opinii";
      return;
    }

    const avg = list.reduce((s, r) => s + (Number(r.stars) || 0), 0) / list.length;
    summary.textContent = `Średnia: ${avg.toFixed(1)} (${list.length})`;

    list.forEach((r) => {
      const d = document.createElement("div");
      d.style.padding = "10px";
      d.style.border = "1px solid rgba(148, 163, 184, 0.25)";
      d.style.borderRadius = "12px";
      d.style.background = "rgba(15, 23, 42, 0.5)";

      const created = r.createdAt ? String(r.createdAt).replace("T", " ").slice(0, 16) : "";

      d.innerHTML = `
        <div style="display:flex; justify-content:space-between; gap:10px; margin-bottom:6px;">
          <div style="font-weight:700; color: var(--text);">Opinia</div>
          <div style="font-size:11px; color: var(--muted); white-space:nowrap;">${created}</div>
        </div>
        <div style="font-size:12px; color: var(--muted); margin-bottom:6px;">
          Ocena: <b style="color: var(--text);">${Number(r.stars) || 0}/5</b>
        </div>
        <div style="font-size:13px; color: var(--text); line-height:1.45;">
          ${r.content ?? ""}
        </div>
      `;
      listEl.appendChild(d);
    });
  } catch (e) {
    console.error(e);
    summary.textContent = "Opinie: błąd połączenia.";
  }
}

let boundTitleId = null;

export function bindReviewSubmit(titleId) {
  if (boundTitleId === titleId) return;
  boundTitleId = titleId;

  const btn = byId("submit-review-btn");
  const starsEl = byId("review-stars");
  const contentEl = byId("review-content");
  if (!btn || !starsEl || !contentEl) return;

  btn.onclick = async () => {
    const stars = parseInt(starsEl.value, 10);
    const content = contentEl.value.trim();

    if (!content) {
      showGeneralMessage("Wpisz treść opinii.", "error");
      return;
    }

    const payload = {
      titleId: titleId,
      stars: Number.isFinite(stars) ? stars : 5,
      content: content,
    };

    try {
      const res = await apiFetch("/api/reviews", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(payload),
      });

      if (res.status === 201 || res.ok) {
        showGeneralMessage("Dodano opinię", "success");
        contentEl.value = "";
        await loadReviewsForBook(titleId);
        return;
      }

      const txt = await res.text().catch(() => "");
      showGeneralMessage(`Błąd dodawania opinii: ${txt || res.status}`, "error");
    } catch (e) {
      console.error(e);
      showGeneralMessage("Błąd połączenia przy dodawaniu opinii.", "error");
    }
  };
}
