package org.example.librex.users;

import org.springframework.stereotype.Service;

import java.util.List;

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

    public AppUser addUser(AppUser user) {
        appUserRepository.save(user);
        return user;
    }


}
