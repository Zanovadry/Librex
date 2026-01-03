package org.example.librex.database.author;

import jakarta.validation.Valid;
import org.example.librex.database.author.dto.AuthorRequest;
import org.example.librex.database.author.dto.AuthorResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/authors")
public class AuthorController {

    private final AuthorService authorService;

    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    // GET /api/authors - lista autorów
    @GetMapping
    public List<AuthorResponse> findAll() {
        return authorService.findAll();
    }

    // GET /api/authors/{id} - szczegóły autora
    @GetMapping("/{id}")
    public AuthorResponse findById(@PathVariable Integer id) {
        return authorService.findById(id);
    }

    // POST /api/authors - utworzenie autora
    @PreAuthorize("hasRole('ADMIN') or hasRole('LIBRARIAN')")
    @PostMapping
    public AuthorResponse create(@Valid @RequestBody AuthorRequest request) {
        return authorService.create(request);
    }

    // PUT /api/authors/{id} - aktualizacja autora
    @PreAuthorize("hasRole('ADMIN') or hasRole('LIBRARIAN')")
    @PutMapping("/{id}")
    public AuthorResponse update(@PathVariable Integer id,
                                 @Valid @RequestBody AuthorRequest request) {
        return authorService.update(id, request);
    }

    // DELETE /api/authors/{id} - usunięcie autora
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        authorService.delete(id);
    }
}
