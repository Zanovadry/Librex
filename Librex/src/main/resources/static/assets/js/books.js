import { apiFetch } from "./api.js";
import { showBookDetailsModal } from "./bookDetails.js";

export function renderBooks(books) {
  const grid = document.getElementById("books-grid");
  const empty = document.getElementById("books-empty");

  if (!grid || !empty) return;

  grid.innerHTML = "";

  if (!books || books.length === 0) {
    empty.style.display = "block";
    return;
  }

  empty.style.display = "none";

  books.forEach((book) => {
    const card = document.createElement("div");
    card.className = "book-card";
    card.dataset.bookId = book.id;

    const cover = document.createElement("div");
    cover.className = "book-cover";
    const coverUrl =
      book.photo || "https://dummyimage.com/300x400/020617/94a3b8.png&text=Book";
    cover.style.backgroundImage = `url('${coverUrl}')`;

    const title = document.createElement("div");
    title.className = "book-title";
    title.textContent = book.title || "Bez tytułu";

    const author = document.createElement("div");
    author.className = "book-author";
    author.textContent = book.author || "Nieznany autor";

    const meta = document.createElement("div");
    meta.className = "book-meta";

    const availableChip = document.createElement("div");
    availableChip.className = "book-chip";
    availableChip.textContent = `Dostępne: ${book.availableCopies}`;
    availableChip.style.color =
      book.availableCopies > 0 ? "var(--success)" : "var(--danger)";
    availableChip.style.borderColor =
      book.availableCopies > 0 ? "var(--success)" : "var(--danger)";

    const tag = document.createElement("div");
    tag.className = "book-tag";
    tag.textContent = book.category || "Książka";

    meta.appendChild(availableChip);
    meta.appendChild(tag);

    card.appendChild(cover);
    card.appendChild(title);
    card.appendChild(author);
    card.appendChild(meta);

    card.addEventListener("click", () => showBookDetailsModal(book));
    grid.appendChild(card);
  });
}

export async function fetchBooks() {
  try {
    const res = await apiFetch("/api/books", {
      headers: { "Content-Type": "application/json" },
    });

    if (!res.ok) {
      console.warn("GET /api/books nie zwrócił 200.");
      renderBooks([]);
      return;
    }

    const books = await res.json();
    renderBooks(books);
  } catch (e) {
    console.error(e);
    renderBooks([]);
  }
}
