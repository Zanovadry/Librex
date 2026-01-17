package org.example.librex.statistics.dto.user;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class UserStatisticsMapper {

    public static UserStatisticsResponse toDto(
            Integer totalUsersCount,
            Integer activeUsersCount
    ) {
        Integer inactiveUsersCount = totalUsersCount - activeUsersCount;
        BigDecimal activePct = totalUsersCount > 0
                ? BigDecimal.valueOf(activeUsersCount)
                .divide(BigDecimal.valueOf(totalUsersCount), 4, RoundingMode.HALF_UP)
                : BigDecimal.ZERO;

        return new UserStatisticsResponse(
                totalUsersCount,
                activeUsersCount,
                inactiveUsersCount,
                activePct.multiply(BigDecimal.valueOf(100)).setScale(2, RoundingMode.HALF_UP)
        );
    }
}

