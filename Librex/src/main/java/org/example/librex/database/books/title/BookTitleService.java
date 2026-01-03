package org.example.librex.database.books.title;

import org.example.librex.database.author.Author;
import org.example.librex.database.author.AuthorRepository;
import org.example.librex.database.books.title.dto.BookRequest;
import org.example.librex.database.books.title.dto.BookResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BookTitleService {

    private final BookTitleRepository bookTitleRepository;
    private final AuthorRepository authorRepository;

    public BookTitleService(BookTitleRepository bookTitleRepository,
                            AuthorRepository authorRepository) {
        this.bookTitleRepository = bookTitleRepository;
        this.authorRepository = authorRepository;
    }

    @Transactional
    public BookResponse create(BookRequest request) {
        Author author = authorRepository.findById(request.getAuthorId())
                .orElseThrow(() -> new IllegalArgumentException("Author not found"));

        BookTitle title = new BookTitle(
                request.getTitle(),
                author,
                request.getDescription(),
                request.getPhoto()
        );

        BookTitle saved = bookTitleRepository.save(title);
        return toResponse(saved);
    }

    public List<BookResponse> findAll() {
        return bookTitleRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    public BookResponse findById(Integer id) {
        BookTitle bookTitle = bookTitleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Book not found"));
        return toResponse(bookTitle);
    }

    @Transactional
    public BookResponse update(Integer id, BookRequest request) {
        BookTitle bookTitle = bookTitleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Book not found"));

        Author author = authorRepository.findById(request.getAuthorId())
                .orElseThrow(() -> new IllegalArgumentException("Author not found"));

        bookTitle.setTitle(request.getTitle());
        bookTitle.setAuthor(author);
        bookTitle.setDescription(request.getDescription());
        bookTitle.setPhoto(request.getPhoto());

        return toResponse(bookTitle);
    }

    @Transactional
    public void delete(Integer id) {
        bookTitleRepository.deleteById(id);
    }

    private BookResponse toResponse(BookTitle entity) {
        String authorName = entity.getAuthor().getFirstname() + " " + entity.getAuthor().getSurname();

        // na razie nie masz okładki, języka, kategorii na poziomie tytułu;
        // zostawiamy null / placeholder, żeby pasowało do frontu
        return new BookResponse(
                entity.getId(),
                entity.getTitle(),
                authorName,
                entity.getDescription(),
                entity.getPhoto(),
                null,      // language
                null       // category
        );
    }
}
