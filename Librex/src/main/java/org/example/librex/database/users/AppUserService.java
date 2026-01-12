package org.example.librex.database.users;

import org.example.librex.database.dictionaries.permission.Permission;
import org.example.librex.database.dictionaries.permission.PermissionRepository;
import org.example.librex.database.dictionaries.permission.Role;
import org.example.librex.database.users.dto.RegistrationRequest;
import org.example.librex.database.users.dto.UserResponse;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AppUserService {

    private final AppUserRepository userRepository;
    private final PermissionRepository permissionRepository;
    private final PasswordEncoder passwordEncoder;
    private final org.example.librex.database.reservation.ReservationRepository reservationRepository;
    private final org.example.librex.database.waitlist.WaitlistRepository waitlistRepository;

    public AppUserService(AppUserRepository userRepository,
                          PermissionRepository permissionRepository,
                          PasswordEncoder passwordEncoder,
                          org.example.librex.database.reservation.ReservationRepository reservationRepository,
                          org.example.librex.database.waitlist.WaitlistRepository waitlistRepository) {
        this.userRepository = userRepository;
        this.permissionRepository = permissionRepository;
        this.passwordEncoder = passwordEncoder;
        this.reservationRepository = reservationRepository;
        this.waitlistRepository = waitlistRepository;
    }

    @Transactional(readOnly = true)
    public org.example.librex.database.users.dto.UserDetailsResponse getUserDetails(Integer userId) {
        AppUser user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);

        var reservations = reservationRepository.findByUser_IdAndReturnDateIsNull(userId).stream()
                .map(r -> new org.example.librex.database.users.dto.UserReservationDto(
                        r.getId(),
                        r.getCopy().getId(),
                        r.getCopy().getEdition().getTitle().getTitle(),
                        r.getCopy().getInventoryNumber(),
                        r.getExpectedReturnDate()
                ))
                .toList();

        var waitlistItems = waitlistRepository.findByAppUser_IdAndActiveTrue(userId).stream()
                .map(w -> new org.example.librex.database.users.dto.UserWaitlistDto(
                        w.getWaitlistId(),
                        w.getBookTitle().getTitle(),
                        w.getPosition(),
                        w.getCreateDate()
                ))
                .toList();

        return new org.example.librex.database.users.dto.UserDetailsResponse(toResponse(user), reservations, waitlistItems);
    }

    @Transactional
    public UserResponse registerUser(RegistrationRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new UserAlreadyExistsException("Email already in use");
        }
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new UserAlreadyExistsException("Username already in use");
        }

        Permission defaultPermission = permissionRepository.findByRole(Role.CUSTOMER)
                .orElseThrow(() -> new IllegalStateException("Default role CUSTOMER not found"));

        String hash = passwordEncoder.encode(request.getPassword());

        AppUser user = new AppUser(
                defaultPermission,
                null,
                request.getFirstname(),
                request.getSurname(),
                null,
                null,
                null,
                request.getEmail(),
                request.getUsername(),
                hash,
                false
        );

        AppUser saved = userRepository.save(user);
        return toResponse(saved);
    }

    public List<UserResponse> findAll() {
        return userRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    public UserResponse findById(Integer id) {
        AppUser user = userRepository.findById(id)
                .orElseThrow(UserNotFoundException::new);
        return toResponse(user);
    }

    public UserResponse findByUsername(String username) {
        AppUser user = userRepository.findByUsername(username)
                .orElseThrow(UserNotFoundException::new);
        return toResponse(user);
    }

    public List<UserResponse> searchUsers(String query) {
        return userRepository.searchUsers(query).stream()
                .map(this::toResponse)
                .toList();
    }


    @Transactional
    public UserResponse updateUser(Integer id, RegistrationRequest update) {
        AppUser user = userRepository.findById(id)
                .orElseThrow(UserNotFoundException::new);

        user.setFirstname(update.getFirstname());
        user.setSurname(update.getSurname());
        user.setEmail(update.getEmail());

        return toResponse(user);
    }

    @Transactional
    public void deleteUser(Integer id) {
        userRepository.deleteById(id);
    }

    private UserResponse toResponse(AppUser user) {
        return new UserResponse(
                user.getId(),
                user.getFirstname(),
                user.getSurname(),
                user.getEmail(),
                user.getUsername(),
                user.getPermission().getRole().name()
        );
    }
}
