package org.example.librex.users;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("users")
public class AppUserController {

    private final AppUserService appUserService;

    public AppUserController(AppUserService appUserService) {
        this.appUserService = appUserService;
    }

    @GetMapping
    public List<AppUser> findAll() {
        return appUserService.findAll();
    }

    @GetMapping("/{id}")
    public Optional<AppUser> findById(@PathVariable int id){
        return appUserService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AppUser addUser(@RequestBody AppUser user) {
        System.out.println("Firstname: " + user.getFirstname());
        return appUserService.addUser(user);
    }
}
