package org.example.librex.users;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AppUserService {

    private final AppUserRepository appUserRepository;

    public AppUserService(AppUserRepository appUserRepository) {
        this.appUserRepository = appUserRepository;
        System.out.println("UserService init");
    }

    public List<AppUser> findAll() {
        return appUserRepository.findAll();
    }

    public Optional<AppUser> findById(int id) {
            return appUserRepository.findById(id);
    }

    public AppUser addUser(AppUser user) {
        appUserRepository.save(user);
        return user;
    }


}
