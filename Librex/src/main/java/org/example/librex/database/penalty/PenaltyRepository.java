package org.example.librex.database.penalty;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PenaltyRepository extends JpaRepository<Penalty, Integer> {
    

    List<Penalty> findByReservation_User_IdAndPaidFalse(Integer userId);
}
