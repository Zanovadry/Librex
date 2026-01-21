package org.example.librex.database.reservation.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class ProlongRequest {

    @NotNull
    private Integer reservationId;

    @NotNull
    @Min(1)
    private Integer extendDays;

    public Integer getReservationId() {
        return reservationId;
    }

    public void setReservationId(Integer reservationId) {
        this.reservationId = reservationId;
    }

    public Integer getExtendDays() {
        return extendDays;
    }

    public void setExtendDays(Integer extendDays) {
        this.extendDays = extendDays;
    }
}
