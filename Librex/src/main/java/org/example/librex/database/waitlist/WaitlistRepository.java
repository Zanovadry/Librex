package org.example.librex.database.waitlist;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface WaitlistRepository extends JpaRepository<Waitlist, Integer> {


    List<Waitlist> findByBookTitle_IdAndActiveTrueOrderByPositionAsc(Integer titleId);


    Optional<Waitlist> findTopByBookTitle_IdOrderByPositionDesc(Integer titleId);
    
    boolean existsByAppUser_IdAndBookTitle_IdAndActiveTrue(Integer userId, Integer titleId);

    List<Waitlist> findByAppUser_IdAndActiveTrue(Integer userId);
}
