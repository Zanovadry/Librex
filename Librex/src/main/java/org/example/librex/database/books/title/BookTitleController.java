package org.example.librex.database.books.title;

import jakarta.validation.Valid;
import org.example.librex.database.books.title.dto.BookRequest;
import org.example.librex.database.books.title.dto.BookResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BookTitleController {

    private final BookTitleService bookTitleService;

    public BookTitleController(BookTitleService bookTitleService) {
        this.bookTitleService = bookTitleService;
    }


    @GetMapping
    public List<BookResponse> findAll() {
        return bookTitleService.findAll();
    }


    @GetMapping("/{id}")
    public BookResponse findById(@PathVariable Integer id) {
        return bookTitleService.findById(id);
    }


    @PreAuthorize("hasRole('ADMIN') or hasRole('LIBRARIAN')")
    @PostMapping
    public BookResponse create(@Valid @RequestBody BookRequest request) {
        return bookTitleService.create(request);
    }


    @PreAuthorize("hasRole('ADMIN') or hasRole('LIBRARIAN')")
    @PutMapping("/{id}")
    public BookResponse update(@PathVariable Integer id,
                               @Valid @RequestBody BookRequest request) {
        return bookTitleService.update(id, request);
    }


    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        bookTitleService.delete(id);
    }


    @GetMapping("/{titleId}/copies")
    public java.util.List<org.example.librex.database.books.copy.dto.BookCopyResponse> getCopiesForTitle(@PathVariable Integer titleId) {
        return bookTitleService.findCopiesByTitleId(titleId);
    }
}
