package org.example.librex;

import jakarta.annotation.PostConstruct;
import org.example.librex.database.author.Author;
import org.example.librex.database.author.AuthorRepository;
import org.example.librex.database.books.copy.BookCopy;
import org.example.librex.database.books.copy.BookCopyRepository;
import org.example.librex.database.books.edition.BookEdition;
import org.example.librex.database.books.edition.BookEditionRepository;
import org.example.librex.database.books.title.BookTitle;
import org.example.librex.database.books.title.BookTitleRepository;
import org.example.librex.database.dictionaries.country.CountryName;
import org.example.librex.database.dictionaries.genre.Genre;
import org.example.librex.database.dictionaries.genre.GenreName;
import org.example.librex.database.dictionaries.genre.GenreRepository;
import org.example.librex.database.dictionaries.permission.Permission;
import org.example.librex.database.dictionaries.permission.PermissionRepository;
import org.example.librex.database.dictionaries.permission.Role;
import org.example.librex.database.users.AppUser;
import org.example.librex.database.users.AppUserRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;

@Component
public class DataInitializer {

    private final JdbcTemplate jdbcTemplate;
    private final PasswordEncoder passwordEncoder;
    private final AppUserRepository appUserRepository;
    private final PermissionRepository permissionRepository;
    private final GenreRepository genreRepository;
    private final AuthorRepository authorRepository;
    private final BookTitleRepository bookTitleRepository;
    private final BookEditionRepository bookEditionRepository;
    private final BookCopyRepository bookCopyRepository;

    public DataInitializer(JdbcTemplate jdbcTemplate,
                           PasswordEncoder passwordEncoder,
                           AppUserRepository appUserRepository,
                           PermissionRepository permissionRepository,
                           GenreRepository genreRepository,
                           AuthorRepository authorRepository,
                           BookTitleRepository bookTitleRepository,
                           BookEditionRepository bookEditionRepository,
                           BookCopyRepository bookCopyRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.passwordEncoder = passwordEncoder;
        this.appUserRepository = appUserRepository;
        this.permissionRepository = permissionRepository;
        this.genreRepository = genreRepository;
        this.authorRepository = authorRepository;
        this.bookTitleRepository = bookTitleRepository;
        this.bookEditionRepository = bookEditionRepository;
        this.bookCopyRepository = bookCopyRepository;
    }

    @PostConstruct
    public void init() {
        System.out.println("Initializing data...");

        try {

            try {
                jdbcTemplate.execute("ALTER TABLE book_copy DROP COLUMN IF EXISTS inventory_number");
            } catch (Exception ignored) {

            }


            Integer permCount = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM permissions_dict", Integer.class);
            if (permCount != null && permCount == 0) {
                System.out.println("Seeding permissions...");
                jdbcTemplate.execute("""
                            INSERT INTO permissions_dict (role) VALUES
                            ('%s'), ('%s'), ('%s')
                            """.formatted(
                        Role.ADMIN.name(),
                        Role.CUSTOMER.name(),
                        Role.LIBRARIAN.name()
                ));
            }


            Integer countryCount = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM countries_dict", Integer.class);
            if (countryCount != null && countryCount == 0) {
                System.out.println("Seeding countries...");
                jdbcTemplate.execute("""
                            INSERT INTO countries_dict (country_name) VALUES
                            ('%s'), ('%s'), ('%s')
                            """.formatted(
                        CountryName.POLAND.name(),
                        CountryName.FINLAND.name(),
                        CountryName.UNITED_STATES.name()
                ));
            }


            Genre fantasyGenre = genreRepository.findByName(GenreName.FANTASY)
                    .orElseGet(() -> genreRepository.save(new Genre(GenreName.FANTASY)));
            Genre scienceFictionGenre = genreRepository.findByName(GenreName.SCIENCE_FICTION)
                    .orElseGet(() -> genreRepository.save(new Genre(GenreName.SCIENCE_FICTION)));


            System.out.println("Seeding users if missing...");
            Permission adminPerm = permissionRepository.findByRole(Role.ADMIN).orElseThrow();
            Permission librarianPerm = permissionRepository.findByRole(Role.LIBRARIAN).orElseThrow();
            Permission customerPerm = permissionRepository.findByRole(Role.CUSTOMER).orElseThrow();

            if (!appUserRepository.existsByUsername("admin")) {
                appUserRepository.save(new AppUser(adminPerm, null, "Admin", "User", null, null, null, "admin@librex.com", "admin", passwordEncoder.encode("admin"), false));
            }
            if (!appUserRepository.existsByUsername("librarian")) {
                appUserRepository.save(new AppUser(librarianPerm, null, "Librarian", "User", null, null, null, "librarian@librex.com", "librarian", passwordEncoder.encode("librarian"), false));
            }
            if (!appUserRepository.existsByUsername("customer")) {
                appUserRepository.save(new AppUser(customerPerm, null, "Customer", "User", null, null, null, "customer@librex.com", "customer", passwordEncoder.encode("customer"), false));
            }


            System.out.println("Seeding books if missing...");
            

            Author author1;

            var king = authorRepository.findAll().stream().filter(a -> "King".equals(a.getSurname())).findFirst();
            if (king.isPresent()) {
                author1 = king.get();
            } else {
                author1 = authorRepository.save(new Author("Stephen", "King", null, "USA", LocalDate.of(1947, 9, 21), null, fantasyGenre));
            }
            

            var herbert = authorRepository.findAll().stream().filter(a -> "Herbert".equals(a.getSurname())).findFirst();
            Author author2;
            if (herbert.isPresent()) {
                author2 = herbert.get();
            } else {
                author2 = authorRepository.save(new Author("Frank", "Herbert", null, "USA", LocalDate.of(1920, 10, 8), LocalDate.of(1986, 2, 11), scienceFictionGenre));
            }



            if (bookTitleRepository.findAll().stream().noneMatch(b -> "The Hobbit".equals(b.getTitle()))) {
                 BookTitle availableTitle = bookTitleRepository.save(new BookTitle("The Hobbit", author1, "A great adventure story.", "https://images-na.ssl-images-amazon.com/images/I/91b0C2YNSrL.jpg"));
                 BookEdition availableEdition1 = bookEditionRepository.save(new BookEdition(availableTitle, null, null, "978-0345339683", 300, 1990, true, new BigDecimal("15.99"), "First paperback edition"));
                 bookCopyRepository.save(new BookCopy("HB-001", availableEdition1, "Good", null, true));
                 bookCopyRepository.save(new BookCopy("HB-002", availableEdition1, "New", null, true));
                 bookCopyRepository.save(new BookCopy("HB-003", availableEdition1, "Worn", null, true));
            }


            if (bookTitleRepository.findAll().stream().noneMatch(b -> "Dune".equals(b.getTitle()))) {
                 BookTitle unavailableTitle = bookTitleRepository.save(new BookTitle("Dune", author2, "Epic science fiction saga.", "https://images-na.ssl-images-amazon.com/images/I/41-A87A-VNL._SX332_BO1,204,203,200_.jpg"));
                 BookEdition unavailableEdition1 = bookEditionRepository.save(new BookEdition(unavailableTitle, null, null, "978-0441172719", 600, 1965, false, new BigDecimal("12.50"), "Classic edition"));
                 bookCopyRepository.save(new BookCopy("DU-001", unavailableEdition1, "Good", null, false));
                 bookCopyRepository.save(new BookCopy("DU-002", unavailableEdition1, "Damaged", null, false));
            }


            String testBookTitle = "Effective Java - TEST";
            if (bookTitleRepository.findAll().stream().noneMatch(b -> testBookTitle.equals(b.getTitle()))) {
                 System.out.println("Adding NEW TEST BOOK: " + testBookTitle);

                 BookTitle t = bookTitleRepository.save(new BookTitle(testBookTitle, author2, "Programming classic", null));
                 
                 BookEdition e = bookEditionRepository.save(new BookEdition(t, null, null, "123-TEST", 400, 2018, true, new BigDecimal("50.00"), "3rd Edition"));
                 
                 bookCopyRepository.save(new BookCopy("EJ-001", e, "New", null, true));
                 bookCopyRepository.save(new BookCopy("EJ-002", e, "New", null, true));
            }

        } catch (Exception e) {
            System.err.println("DataInitializer warning: " + e.getMessage());
            e.printStackTrace();
        }
    }
}