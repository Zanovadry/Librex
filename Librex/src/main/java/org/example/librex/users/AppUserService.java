package org.example.librex.users;

import org.example.librex.dictionaries.permissions.Permission;
import org.example.librex.dictionaries.permissions.PermissionRepository;
import org.example.librex.dictionaries.permissions.Role;
import org.example.librex.users.dto.RegistrationRequest;
import org.example.librex.users.dto.UserResponse;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AppUserService {

    private final AppUserRepository userRepository;
    private final PermissionRepository permissionRepository;
    private final PasswordEncoder passwordEncoder;

    public AppUserService(AppUserRepository userRepository,
                          PermissionRepository permissionRepository,
                          PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.permissionRepository = permissionRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public UserResponse registerUser(RegistrationRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email already in use");
        }
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException("Username already in use");
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
                .orElseThrow(() -> new RuntimeException("User not found"));
        return toResponse(user);
    }

    @Transactional
    public UserResponse updateUser(Integer id, RegistrationRequest update) {
        AppUser user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

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
