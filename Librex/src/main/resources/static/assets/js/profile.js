export function updateProfileUI(userDetails) {
  const user = userDetails.user;

  const initials = (user.firstname?.[0] || "") + (user.surname?.[0] || "");
  const upperInitials = initials.toUpperCase() || "U";

  const avatarSmall = document.getElementById("profile-avatar-initials");
  const avatarBig = document.getElementById("profile-avatar-big");
  const buttonName = document.getElementById("profile-button-name");

  if (avatarSmall) avatarSmall.textContent = upperInitials;
  if (avatarBig) avatarBig.textContent = upperInitials;

  if (buttonName) buttonName.textContent = user.firstname + " " + user.surname;

  const nameEl = document.getElementById("profile-name");
  const usernameEl = document.getElementById("profile-username");
  const emailEl = document.getElementById("profile-email");
  const roleEl = document.getElementById("profile-role");

  if (nameEl) nameEl.textContent = user.firstname + " " + user.surname;
  if (usernameEl) usernameEl.textContent = "@" + user.username;
  if (emailEl) emailEl.textContent = user.email;
  if (roleEl) roleEl.textContent = user.role;

  const libBtn = document.getElementById("librarian-panel-btn");
  if (libBtn) {
    if (user.role === "ADMIN" || user.role === "LIBRARIAN") libBtn.classList.remove("hidden");
    else libBtn.classList.add("hidden");
  }

  const resList = document.getElementById("profile-reservations-list");
  if (resList) {
    resList.innerHTML = "";
    if (userDetails.activeReservations && userDetails.activeReservations.length > 0) {
      userDetails.activeReservations.forEach((res) => {
        const item = document.createElement("div");
        item.className = "profile-row";
        item.style.justifyContent = "flex-start";
        item.style.flexDirection = "column";
        item.style.gap = "2px";
        item.innerHTML = `
          <div style="font-weight: 600; font-size: 13px;">${res.bookTitle}</div>
          <div style="font-size: 11px; color: var(--muted);">
            Do zwrotu: ${res.expectedReturnDate}
            ${res.overdue ? '<span style="color: var(--danger)">(PO TERMINIE)</span>' : ""}
          </div>
        `;
        resList.appendChild(item);
      });
    } else {
      resList.innerHTML =
        '<div style="font-size: 12px; color: var(--muted); font-style: italic;">Brak aktywnych wypożyczeń.</div>';
    }
  }

  const waitList = document.getElementById("profile-waitlist-list");
  if (waitList) {
    waitList.innerHTML = "";
    if (userDetails.waitlistItems && userDetails.waitlistItems.length > 0) {
      userDetails.waitlistItems.forEach((item) => {
        const row = document.createElement("div");
        row.className = "profile-row";
        row.innerHTML = `
          <span>${item.bookTitle}</span>
          <span>Poz: ${item.position}</span>
        `;
        waitList.appendChild(row);
      });
    } else {
      waitList.innerHTML =
        '<div style="font-size: 12px; color: var(--muted); font-style: italic;">Brak oczekujących.</div>';
    }
  }
}
