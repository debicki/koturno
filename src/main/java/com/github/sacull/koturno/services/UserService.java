package com.github.sacull.koturno.services;

import com.github.sacull.koturno.entities.User;
import com.github.sacull.koturno.repositories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private UserRepo userRepo;

    @Autowired
    public UserService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    public void registerUser(String username, String password, boolean active, String role) {
        userRepo.save(new User(username, "{noop}" + password, active, role));
    }

    public User findByName(String name) {
        return userRepo.findByUsername(name);
    }
}
