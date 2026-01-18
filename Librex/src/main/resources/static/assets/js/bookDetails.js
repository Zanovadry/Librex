import { apiFetch } from "./api.js";
import { showGeneralMessage } from "./uiMessages.js";
import { getCurrentUser } from "./state.js";
import { fetchBooks } from "./books.js";
import { fetchCurrentUser } from "./auth.js";
import { loadReviewsForBook, bindReviewSubmit } from "./reviews.js";

export function closeBookDetailsModal() {
  document.getElementById("book-details-modal-backdrop").classList.remove("active");
}

export async function showBookDetailsModal(book) {
  const currentUser = getCurrentUser();
  if (!currentUser) {
    showGeneralMessage("Zaloguj się, aby zobaczyć szczegóły książki.", "error");
    return;
  }

  const backdrop = document.getElementById("book-details-modal-backdrop");
  const modalTitle = document.getElementById("book-details-modal-title");
  const modalCover = document.getElementById("book-details-modal-cover");
  const modalDescription = document.getElementById("book-details-modal-description");
  const availableSection = document.getElementById("book-details-available-section");
  const unavailableSection = document.getElementById("book-details-unavailable-section");
  const copyList = document.getElementById("book-details-copy-list");
  const joinWaitlistBtn = document.getElementById("join-waitlist-btn");

  modalTitle.textContent = `${book.title} by ${book.author}`;
  modalCover.style.backgroundImage = `url('${book.photo || "https://dummyimage.com/300x400/020617/94a3b8.png&text=Book"}')`;
  modalDescription.textContent = book.description || "";

  copyList.innerHTML = "";

  try {
    const res = await apiFetch(`/api/books/${book.id}/copies`);
    if (!res.ok) {
      const errorText = await res.text().catch(() => "");
      showGeneralMessage(`Błąd pobierania egzemplarzy: ${errorText || res.status}`, "error");
      return;
    }

    const copies = await res.json();
    const availableCopies = copies.filter((c) => c.available);

    if (availableCopies.length > 0) {
      availableSection.classList.remove("hidden");
      unavailableSection.classList.add("hidden");

      availableCopies.forEach((copy) => {
        const copyItem = document.createElement("div");
        copyItem.className = "copy-item";
        copyItem.innerHTML = `
          <div>
            <div class="inventory-number">Nr inwentarzowy: ${copy.inventoryNumber}</div>
            <div class="status">Stan: ${copy.condition} (Dostępny)</div>
          </div>
          <button class="btn btn-primary btn-small borrow-btn" data-copy-id="${copy.id}">
            Wypożycz
          </button>
        `;
        copyList.appendChild(copyItem);

        copyItem.querySelector(".borrow-btn").onclick = async () => {
          const ok = await borrowBook(copy.id);
          if (ok) closeBookDetailsModal();
        };
      });
    } else {
      availableSection.classList.add("hidden");
      unavailableSection.classList.remove("hidden");
      joinWaitlistBtn.onclick = async () => {
        const ok = await joinWaitlist(book.id);
        if (ok) closeBookDetailsModal();
      };
    }

    await loadReviewsForBook(book.id);
    bindReviewSubmit(book.id);

    backdrop.classList.add("active");
  } catch (e) {
    console.error(e);
    showGeneralMessage("Błąd połączenia przy pobieraniu egzemplarzy.", "error");
  }
}

export async function borrowBook(copyId) {
  const currentUser = getCurrentUser();
  if (!currentUser) return false;

  try {
    const res = await apiFetch("/api/reservations/borrow", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ userId: currentUser.id, copyId, days: 14 }),
    });

    if (!res.ok) {
      const text = await res.text().catch(() => "");
      showGeneralMessage(`Błąd wypożyczenia: ${text || res.status}`, "error");
      return false;
    }

    showGeneralMessage("Książka wypożyczona!", "success");

    // odświeżenia bez F5
    await fetchBooks();
    await fetchCurrentUser(false);

    return true;
  } catch (e) {
    console.error(e);
    showGeneralMessage("Błąd połączenia przy wypożyczaniu.", "error");
    return false;
  }
}

export async function joinWaitlist(bookTitleId) {
  const currentUser = getCurrentUser();
  if (!currentUser) return false;

  try {
    const res = await apiFetch("/api/waitlist/join", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ userId: currentUser.id, bookTitleId }),
    });

    if (!res.ok) {
      const text = await res.text().catch(() => "");
      showGeneralMessage(`Błąd zapisu do kolejki: ${text || res.status}`, "error");
      return false;
    }

    showGeneralMessage("Zapisano do listy oczekujących!", "success");
    await fetchCurrentUser(false);
    return true;
  } catch (e) {
    console.error(e);
    showGeneralMessage("Błąd połączenia przy zapisie do kolejki.", "error");
    return false;
  }
}
