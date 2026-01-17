package org.example.librex.statistics.dto.reservation;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ReservationStatisticsResponse(
        Integer totalAmount,
        Integer damagedCount,
        Integer lateCount,
        BigDecimal damagedBooksPercentage,
        BigDecimal lateReturnsPercentage,
        LocalDate periodFrom,
        LocalDate periodTo
) {}


