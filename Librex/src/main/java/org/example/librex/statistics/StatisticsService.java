package org.example.librex.statistics;

import org.example.librex.database.reservation.ReservationRepository;
import org.example.librex.statistics.dto.ReservationStatisticsMapper;
import org.example.librex.statistics.dto.ReservationStatisticsResponse;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service

public class StatisticsService {

    private final ReservationRepository reservationRepository;

    public StatisticsService(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    public ReservationStatisticsResponse getReservationStatisticsByDate(LocalDate dateFrom, LocalDate dateTo) {
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
