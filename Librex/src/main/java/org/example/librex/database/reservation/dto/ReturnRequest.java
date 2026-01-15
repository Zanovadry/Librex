package org.example.librex.database.reservation.dto;

import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public class ReturnRequest {

    @NotNull
    private Integer copyId;

    private String damageDetails;

    // Kwota kary za zniszczenie (opcjonalna, np. 0 jeśli brak zniszczeń)
    private BigDecimal damageFee;

    public Integer getCopyId() {
        return copyId;
    }

    public void setCopyId(Integer copyId) {
        this.copyId = copyId;
    }

    public String getDamageDetails() {
        return damageDetails;
    }

    public void setDamageDetails(String damageDetails) {
        this.damageDetails = damageDetails;
    }

    public BigDecimal getDamageFee() {
        return damageFee;
    }

    public void setDamageFee(BigDecimal damageFee) {
        this.damageFee = damageFee;
    }
}
