package org.example.librex.statistics.dto.user;

import java.math.BigDecimal;


//TODO: new users? (need to add new collumn to appUser)
public record UserStatisticsResponse(
        Integer totalUsers,
        Integer activeUsers,
        Integer inactiveUsers,
        BigDecimal activePercentage
) {}

