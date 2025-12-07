package org.example.librex.users;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsersService {

    private final UsersRepository usersRepository;

    public UsersService(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
        System.out.println("UserService init");
    }

    public List<Users> findAll() {
        return usersRepository.findAll();
    }

    public Users addUser(Users user) {
        usersRepository.save(user);
        return user;
    }


}
