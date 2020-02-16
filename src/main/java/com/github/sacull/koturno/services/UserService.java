package com.github.sacull.koturno.services;

import com.github.sacull.koturno.entities.User;
import com.github.sacull.koturno.repositories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private UserRepo userRepo;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepo userRepo,
                       PasswordEncoder passwordEncoder) {

        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

    public void registerUser(String username, String password, boolean active, String role) {
        userRepo.save(new User(username, passwordEncoder.encode(password), active, role));
    }

    public User findByName(String name) {
        return userRepo.findByUsername(name);
    }
}
