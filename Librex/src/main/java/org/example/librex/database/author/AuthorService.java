package org.example.librex.database.author;

import org.example.librex.database.author.dto.AuthorRequest;
import org.example.librex.database.author.dto.AuthorResponse;
import org.example.librex.database.dictionaries.genre.Genre;
import org.example.librex.database.dictionaries.genre.GenreRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AuthorService {

    private final AuthorRepository authorRepository;
    private final GenreRepository genreRepository;

    public AuthorService(AuthorRepository authorRepository,
                         GenreRepository genreRepository) {
        this.authorRepository = authorRepository;
        this.genreRepository = genreRepository;
    }

    @Transactional
    public AuthorResponse create(AuthorRequest request) {
        Genre genre = genreRepository.findById(request.getGenreId())
                .orElseThrow(() -> new IllegalArgumentException("Genre not found"));

        Author author = new Author(
                request.getFirstname(),
                request.getSurname(),
                request.getNickname(),
                request.getNationality(),
                request.getBirthdate(),
                request.getDeathdate(),
                genre
        );

        Author saved = authorRepository.save(author);
        return toResponse(saved);
    }

    public List<AuthorResponse> findAll() {
        return authorRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    public AuthorResponse findById(Integer id) {
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Author not found"));
        return toResponse(author);
    }

    @Transactional
    public AuthorResponse update(Integer id, AuthorRequest request) {
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Author not found"));

        if (request.getGenreId() != null) {
            Genre genre = genreRepository.findById(request.getGenreId())
                    .orElseThrow(() -> new IllegalArgumentException("Genre not found"));
            author.setGenre(genre);
        }

        author.setFirstname(request.getFirstname());
        author.setSurname(request.getSurname());
        author.setNickname(request.getNickname());
        author.setNationality(request.getNationality());
        author.setBirthdate(request.getBirthdate());
        author.setDeathdate(request.getDeathdate());

        return toResponse(author);
    }

    @Transactional
    public void delete(Integer id) {
        authorRepository.deleteById(id);
    }

    private AuthorResponse toResponse(Author author) {
        return new AuthorResponse(
                author.getId(),
                author.getFirstname(),
                author.getSurname(),
                author.getNickname(),
                author.getNationality(),
                author.getBirthdate(),
                author.getDeathdate(),
                author.getGenre() != null ? author.getGenre().getName().name() : null
        );
    }
}
