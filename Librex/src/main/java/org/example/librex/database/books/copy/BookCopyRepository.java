package org.example.librex.database.books.copy;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface BookCopyRepository extends JpaRepository<BookCopy, Integer> {
    Optional<BookCopy> findByInventoryNumber(String inventoryNumber);
}
