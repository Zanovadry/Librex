package org.example.librex.database.reservation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ReservationRepository extends JpaRepository<Reservation, Integer> {

    interface ReservationStatsRow {
        int getTotal();
        int getDamaged();
        int getLate();
    }

    Optional<Reservation> findByCopy_IdAndReturnDateIsNull(Integer copyId);

    List<Reservation> findByUser_Id(Integer userId);

    @Query(value = """
        SELECT
          COUNT(*) AS total,
          COUNT(CASE WHEN damage_details is not null THEN 1 END) AS damaged,
          COUNT(CASE WHEN return_date > expected_return_date THEN 1 END) AS late
        FROM reservations
        WHERE create_date >= :start AND create_date < :end
      """, nativeQuery = true)
    ReservationStatsRow getStatsBetweenDates(@Param("start") LocalDate start, @Param("end") LocalDate end);

    List<Reservation> findByUser_IdAndReturnDateIsNull(Integer userId);

}
