package org.example.librex.statistics;

import org.example.librex.database.reservation.ReservationRepository;
import org.example.librex.database.users.AppUserRepository;
import org.example.librex.statistics.dto.reservation.ReservationStatisticsMapper;
import org.example.librex.statistics.dto.reservation.ReservationStatisticsResponse;
import org.example.librex.statistics.dto.user.UserStatisticsMapper;
import org.example.librex.statistics.dto.user.UserStatisticsResponse;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service

public class StatisticsService {

    private final ReservationRepository reservationRepository;
    private final AppUserRepository appUserRepository;

    public StatisticsService(ReservationRepository reservationRepository, AppUserRepository appUserRepository) {
        this.reservationRepository = reservationRepository;
        this.appUserRepository = appUserRepository;
    }

    //--- USERS ---
    public UserStatisticsResponse getUsersStatistics() {
        Integer userCount = ((int) appUserRepository.count());
        Integer activeUsersCount = reservationRepository.getActiveUsersCount();

        return UserStatisticsMapper.toDto(
                userCount,
                activeUsersCount
        );
    }

    // --- RESERVATIONS ---
    public ReservationStatisticsResponse getReservationsStatisticsByDate(LocalDate dateFrom, LocalDate dateTo) {
        ReservationRepository.ReservationStatsRow stats = reservationRepository.getStatsBetweenDates(dateFrom, dateTo);

        return ReservationStatisticsMapper.toDto(
                stats.getTotal(),
                stats.getDamaged(),
                stats.getLate(),
                dateFrom,
                dateTo
        );
    }
}
