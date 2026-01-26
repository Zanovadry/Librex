export function showMessage(message, type) {
  const container = document.getElementById("auth-messages");
  if (!container) return;

  container.innerHTML = "";
  if (!message) return;

  const div = document.createElement("div");

  div.className = type === "error" ? "error" : "success-message";
  div.textContent = message;

  container.appendChild(div);
}

export function showGeneralMessage(message, type = "info") {
  const existing = document.getElementById("general-messages-container");
  const container = existing || document.createElement("div");

  if (!existing) {
    container.id = "general-messages-container";
    container.style.position = "fixed";
    container.style.top = "20px";
    container.style.right = "20px";
    container.style.zIndex = "100";
    container.style.maxWidth = "300px";
    container.style.display = "flex";
    container.style.flexDirection = "column";
    container.style.gap = "10px";
    document.body.appendChild(container);
  }

  const div = document.createElement("div");
  div.className = `message-${type}`;
  div.style.padding = "10px 15px";
  div.style.borderRadius = "8px";
  div.style.backgroundColor =
    type === "error"
      ? "rgba(239, 68, 68, 0.8)"
      : type === "success"
      ? "rgba(34, 197, 94, 0.8)"
      : "rgba(59, 130, 246, 0.8)";
  div.style.color = "white";
  div.style.boxShadow = "0 4px 12px rgba(0,0,0,0.2)";
  div.textContent = message;

  container.appendChild(div);
  setTimeout(() => div.remove(), 5000);
}
