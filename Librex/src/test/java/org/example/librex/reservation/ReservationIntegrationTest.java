package org.example.librex.reservation;

import org.example.librex.database.author.Author;
import org.example.librex.database.author.AuthorRepository;
import org.example.librex.database.books.copy.BookCopy;
import org.example.librex.database.books.copy.BookCopyRepository;
import org.example.librex.database.books.edition.BookEdition;
import org.example.librex.database.books.edition.BookEditionRepository;
import org.example.librex.database.books.title.BookTitle;
import org.example.librex.database.books.title.BookTitleRepository;
import org.example.librex.database.dictionaries.permission.Permission;
import org.example.librex.database.dictionaries.permission.PermissionRepository;
import org.example.librex.database.dictionaries.permission.Role;
import org.example.librex.database.reservation.Reservation;
import org.example.librex.database.reservation.ReservationRepository;
import org.example.librex.database.reservation.ReservationService;
import org.example.librex.database.reservation.dto.BorrowRequest;
import org.example.librex.database.reservation.dto.ReturnRequest;
import org.example.librex.database.users.AppUser;
import org.example.librex.database.users.AppUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class ReservationIntegrationTest {

    @Autowired private ReservationRepository reservationRepository;
    @Autowired private BookCopyRepository bookCopyRepository;
    @Autowired private AppUserRepository appUserRepository;
    @Autowired private BookTitleRepository bookTitleRepository;
    @Autowired private BookEditionRepository bookEditionRepository;
    @Autowired private AuthorRepository authorRepository;
    @Autowired private PermissionRepository permissionRepository;
    @Autowired private org.example.librex.database.dictionaries.genre.GenreRepository genreRepository;
    @Autowired private ReservationService reservationService;

    private AppUser testUser;
    private BookCopy testCopy;

    @BeforeEach
    void setup() {

        var genre = genreRepository.save(new org.example.librex.database.dictionaries.genre.Genre(org.example.librex.database.dictionaries.genre.GenreName.FANTASY));


        Author author = authorRepository.save(new Author("Test", "Author", null, "PL", null, null, genre));


        BookTitle title = bookTitleRepository.save(new BookTitle("Test Book", author, "Desc", null));


        BookEdition edition = new BookEdition(title, null, null, "123", 100, 2020, false, BigDecimal.TEN, null);
        edition = bookEditionRepository.save(edition);


        testCopy = new BookCopy("INV-001", edition, "Good", null, true);
        testCopy = bookCopyRepository.save(testCopy);


        Permission customerPerm = permissionRepository.findByRole(Role.CUSTOMER)
                .orElseThrow(() -> new IllegalStateException("Role not found"));
        testUser = new AppUser(customerPerm, null, "John", "Doe", null, null, null, "john.doe@test.com", "johndoe", "pass", false);
        testUser = appUserRepository.save(testUser);
    }

    @Test
    void testBorrowBook() {
        BorrowRequest request = new BorrowRequest();
        request.setUserId(testUser.getId());
        request.setCopyId(testCopy.getId());
        request.setDays(14);

        var response = reservationService.borrowBook(request);

        assertNotNull(response.getId());
        assertEquals("INV-001", response.getInventoryNumber());
        assertTrue(response.isActive());


        BookCopy updatedCopy = bookCopyRepository.findById(testCopy.getId()).orElseThrow();
        assertFalse(updatedCopy.isAvailable());
    }

    @Test
    void testReturnBookWithPenalty() {

        LocalDate past = LocalDate.now().minusDays(20);
        LocalDate expected = LocalDate.now().minusDays(5); 

        Reservation res = new Reservation(testUser, testCopy, past, expected, null, null);
        testCopy.setAvailable(false);
        bookCopyRepository.save(testCopy);
        reservationRepository.save(res);


        ReturnRequest request = new ReturnRequest();
        request.setCopyId(testCopy.getId());
        request.setDamageDetails("Torn page");
        request.setDamageFee(new BigDecimal("10.00"));

        String message = reservationService.returnBook(request);


        System.out.println("Return Message: " + message);
        assertTrue(message.contains("Penalty charged"));

        Reservation updatedRes = reservationRepository.findById(res.getId()).orElseThrow();
        assertNotNull(updatedRes.getReturnDate());
        assertTrue(updatedRes.isDamaged());

        BookCopy updatedCopy = bookCopyRepository.findById(testCopy.getId()).orElseThrow();
        assertTrue(updatedCopy.isAvailable());
        assertTrue(updatedCopy.getCondition().contains("Torn page"));
    }
}
