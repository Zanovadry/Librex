package org.example.librex.database.reservation;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Integer> {
    

    Optional<Reservation> findByCopy_IdAndReturnDateIsNull(Integer copyId);


    List<Reservation> findByUser_Id(Integer userId);


    List<Reservation> findByUser_IdAndReturnDateIsNull(Integer userId);
}
