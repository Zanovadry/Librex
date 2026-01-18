import { apiFetch } from "./api.js";
import { showGeneralMessage } from "./uiMessages.js";

let currentCopyId = null;
let onReturnSuccess = null;

export function setReturnSuccessCallback(fn) {
  onReturnSuccess = fn;
}

export function openReturnModal(copyId, inventoryNumber) {
  currentCopyId = copyId;

  document.getElementById("return-inventory-num").textContent = inventoryNumber;
  document.getElementById("return-damaged").checked = false;
  document.getElementById("return-damage-desc").value = "";
  document.getElementById("return-damage-fee").value = "0.00";

  toggleDamageFields(false);

  document.getElementById("return-modal-backdrop")?.classList.add("active");
}

export function closeReturnModal() {
  document.getElementById("return-modal-backdrop")?.classList.remove("active");
  currentCopyId = null;
}

export function bindReturnEvents() {
  document.getElementById("return-close")?.addEventListener("click", closeReturnModal);

  document.getElementById("confirm-return-btn")?.addEventListener("click", submitReturn);

  document.getElementById("return-damaged")?.addEventListener("change", (e) => {
    toggleDamageFields(e.target.checked);
  });

  // klik w backdrop zamyka (jak w single-file modalach)
  document.getElementById("return-modal-backdrop")?.addEventListener("click", (e) => {
    if (e.target.id === "return-modal-backdrop") closeReturnModal();
  });
}

function toggleDamageFields(show) {
  const descField = document.getElementById("return-damage-details-field");
  const feeField = document.getElementById("return-damage-fee-field");
  if (!descField || !feeField) return;

  if (show) {
    descField.classList.remove("hidden");
    feeField.classList.remove("hidden");
  } else {
    descField.classList.add("hidden");
    feeField.classList.add("hidden");
  }
}

export async function submitReturn() {
  if (!currentCopyId) return;

  const damaged = document.getElementById("return-damaged").checked;
  const damageDetails = document.getElementById("return-damage-desc").value;
  const damageFee = parseFloat(document.getElementById("return-damage-fee").value) || 0;

  const payload = {
    copyId: parseInt(currentCopyId, 10),
    damaged,
    damageDetails: damaged ? damageDetails : null,
    damageFee: damaged ? damageFee : 0,
  };

  try {
    const res = await apiFetch("/api/reservations/return", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(payload),
    });

    const text = await res.text().catch(() => "");

    if (!res.ok) {
      showGeneralMessage(`Błąd zwrotu${text ? ": " + text : ""}`, "error");
      return;
    }

    showGeneralMessage(`Zwrot przyjęty.${text ? " " + text : ""}`, "success");
    closeReturnModal();

    // ====== odświeżenie panelu bibliotekarza bez F5 ======
    if (typeof onReturnSuccess === "function") {
      await onReturnSuccess();
    }
  } catch (e) {
    console.error(e);
    showGeneralMessage("Błąd połączenia.", "error");
  }
}
