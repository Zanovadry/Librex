package org.example.librex.statistics.dto;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

@Component
public class ReservationStatisticsMapper {

    public static ReservationStatisticsResponse toDto(
            Integer totalAmount,
            Integer damagedBooksCount,
            Integer lateReturnsCount,
            LocalDate periodFrom,
            LocalDate periodTo
    ) {
        return new ReservationStatisticsResponse(
                totalAmount != null ? totalAmount: 0,
                calculatePercentage(damagedBooksCount, totalAmount),
                calculatePercentage(lateReturnsCount, totalAmount),
                periodFrom,
                periodTo
        );
    }

    private static BigDecimal calculatePercentage(Integer count, Integer total) {
        if (count == null || total == null || total == 0) {
            return BigDecimal.ZERO;
        }
        return BigDecimal.valueOf(count)
                .divide(BigDecimal.valueOf(total), 4, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100))
                .setScale(2, RoundingMode.HALF_UP);
    }
}

