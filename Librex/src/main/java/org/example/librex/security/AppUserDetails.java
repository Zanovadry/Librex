package org.example.librex.security;

import org.example.librex.database.users.AppUser;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class AppUserDetails implements UserDetails {

    private final AppUser user;

    public AppUserDetails(AppUser user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Role z permissions_dict: CUSTOMER / LIBRARIAN / ADMIN
        String roleName = "ROLE_" + user.getPermission().getRole().name();
        return List.of(new SimpleGrantedAuthority(roleName));
    }

    @Override
    public String getPassword() {
        return user.getPasswordHash();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    // Na razie wszystko true – możesz później spiąć z is_blacklisted itd.
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        // można tu użyć user.isBlacklisted()
        return !user.isBlacklisted();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        // np. !blacklisted
        return !user.isBlacklisted();
    }

    public AppUser getUser() {
        return user;
    }
}
