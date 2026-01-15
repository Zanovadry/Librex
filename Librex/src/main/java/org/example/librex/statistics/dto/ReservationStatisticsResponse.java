package org.example.librex.statistics.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ReservationStatisticsResponse(
        Integer totalAmount,
        BigDecimal damagedBooksPercentage,
        BigDecimal lateReturnsPercentage,
        LocalDate periodFrom,
        LocalDate periodTo
) {}

