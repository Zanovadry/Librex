package org.example.librex.database.users;

import org.example.librex.database.dictionaries.permission.Role;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AppUserDetailsService implements UserDetailsService {

    private final AppUserRepository userRepository;

    public AppUserDetailsService(AppUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Role role = user.getPermission().getRole();

        return User.withUsername(user.getUsername())
                .password(user.getPasswordHash())      // już zahashowane BCryptem
                .roles(role.name())                    // np. CUSTOMER / LIBRARIAN / ADMIN
                .build();
    }
}
