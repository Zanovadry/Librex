package org.example.librex.database.waitlist;

import org.example.librex.database.books.copy.BookCopy;
import org.example.librex.database.books.copy.BookCopyRepository;
import org.example.librex.database.books.title.BookTitle;
import org.example.librex.database.books.title.BookTitleRepository;
import org.example.librex.database.users.AppUser;
import org.example.librex.database.users.AppUserRepository;
import org.example.librex.database.waitlist.dto.JoinWaitlistRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class WaitlistService {

    private final WaitlistRepository waitlistRepository;
    private final AppUserRepository appUserRepository;
    private final BookTitleRepository bookTitleRepository;
    private final BookCopyRepository bookCopyRepository;

    public WaitlistService(WaitlistRepository waitlistRepository,
                           AppUserRepository appUserRepository,
                           BookTitleRepository bookTitleRepository,
                           BookCopyRepository bookCopyRepository) {
        this.waitlistRepository = waitlistRepository;
        this.appUserRepository = appUserRepository;
        this.bookTitleRepository = bookTitleRepository;
        this.bookCopyRepository = bookCopyRepository;
    }

    @Transactional
    public void joinWaitlist(JoinWaitlistRequest request) {

        AppUser user = appUserRepository.findById(request.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        BookTitle title = bookTitleRepository.findById(request.getBookTitleId())
                .orElseThrow(() -> new IllegalArgumentException("Book Title not found"));


        if (waitlistRepository.existsByAppUser_IdAndBookTitle_IdAndActiveTrue(user.getId(), title.getId())) {
            throw new IllegalStateException("User is already in the waitlist for this title.");
        }


        boolean anyAvailable = title.getBookEditions().stream()
                .flatMap(edition -> edition.getCopies().stream())
                .anyMatch(BookCopy::isAvailable);
        

        Optional<Waitlist> last = waitlistRepository.findTopByBookTitle_IdOrderByPositionDesc(title.getId());
        int nextPosition = last.map(w -> w.getPosition() + 1).orElse(1);


        Waitlist waitlist = new Waitlist(
                nextPosition,
                LocalDateTime.now(),
                true,
                title,
                user
        );
        waitlistRepository.save(waitlist);
    }
}
