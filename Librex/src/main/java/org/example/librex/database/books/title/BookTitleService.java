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

    @Transactional(readOnly = true)
    public List<BookResponse> findAll() {
        return bookTitleRepository.findAllWithDetails().stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
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
        bookTitleRepository.delete(bookTitleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Book not found")));
    }

    @Transactional(readOnly = true)
    public java.util.List<org.example.librex.database.books.copy.dto.BookCopyResponse> findCopiesByTitleId(Integer titleId) {
        BookTitle bookTitle = bookTitleRepository.findById(titleId)
                .orElseThrow(() -> new IllegalArgumentException("Book not found"));

        return bookTitle.getBookEditions().stream()
                .flatMap(edition -> edition.getCopies().stream())
                .map(this::toCopyResponse)
                .toList();
    }

    private org.example.librex.database.books.copy.dto.BookCopyResponse toCopyResponse(org.example.librex.database.books.copy.BookCopy copy) {
        return new org.example.librex.database.books.copy.dto.BookCopyResponse(
                copy.getId(),
                copy.getInventoryNumber(),
                copy.isAvailable(),
                copy.getCondition()
        );
    }

    private BookResponse toResponse(BookTitle entity) {
        String authorName = entity.getAuthor().getFirstname() + " " + entity.getAuthor().getSurname();

        java.util.Set<org.example.librex.database.books.edition.BookEdition> editions = entity.getBookEditions();
        if (editions == null) {
            System.out.println("DEBUG: Title " + entity.getTitle() + " has NULL editions list");
            editions = java.util.Set.of();
        } else {
            System.out.println("DEBUG: Title " + entity.getTitle() + " has " + editions.size() + " editions");
            for (var ed : editions) {
                 var copies = ed.getCopies();
                 if (copies == null) {
                     System.out.println("  - Edition " + ed.getId() + " has NULL copies");
                 } else {
                     System.out.println("  - Edition " + ed.getId() + " has " + copies.size() + " copies");
                 }
            }
        }

        long availableCopiesCount = editions.stream()
                .flatMap(edition -> (edition.getCopies() != null ? edition.getCopies().stream() : java.util.stream.Stream.empty()))
                .filter(org.example.librex.database.books.copy.BookCopy::isAvailable)
                .count();


        return new BookResponse(
                entity.getId(),
                entity.getTitle(),
                authorName,
                entity.getDescription(),
                entity.getPhoto(),
                null,      // language
                null,      // category
                availableCopiesCount
        );
    }
}
