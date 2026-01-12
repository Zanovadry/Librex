package org.example.librex.database.books.title;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BookTitleRepository extends JpaRepository<BookTitle, Integer> {

    @Query("SELECT DISTINCT t FROM BookTitle t LEFT JOIN FETCH t.bookEditions e LEFT JOIN FETCH e.copies")
    List<BookTitle> findAllWithDetails();
}
