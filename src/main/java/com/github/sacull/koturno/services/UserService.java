package com.github.sacull.koturno.services;

import com.github.sacull.koturno.dtos.UserDto;
import com.github.sacull.koturno.entities.User;
import com.github.sacull.koturno.repositories.UserRepo;
import com.github.sacull.koturno.utils.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;

    @Autowired
    public UserService(UserRepo userRepo,
                       PasswordEncoder passwordEncoder,
                       ModelMapper modelMapper) {

        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.modelMapper = modelMapper;
    }

    public void registerUser(String username, String password, boolean active, String role) {
        userRepo.save(new User(username, passwordEncoder.encode(password), active, role, "dark"));
    }

    public User findByName(String name) {
        return userRepo.findByUsername(name);
    }

    public UserDto findByUsername(String name) {
        User user = userRepo.findByUsername(name);
        if (user == null) {
            return null;
        }
        return modelMapper.convert(userRepo.findByUsername(name));
    }

    public int countUsers() {
        if (userRepo.countAllByActive(true) == null) {
            return 0;
        } else {
            return userRepo.countAllByActive(true);
        }
    }

    public int countActiveAdmins() {
        if (userRepo.countAllByActiveAndRole(true, "ROLE_ADMIN") == null) {
            return 0;
        } else {
            return userRepo.countAllByActiveAndRole(true, "ROLE_ADMIN");
        }
    }

    public List<UserDto> findAllUsers() {
        List<User> users = userRepo.findAll();
        List<UserDto> result = new ArrayList<>();

        for (User user : users) {
            if (!user.isVoided()) {
                result.add(modelMapper.convert(user));
            }
        }

        return result;
    }

    public void enableUser(String username) {
        User user = userRepo.findByUsername(username);
        user.setActive(true);
        userRepo.save(user);
    }

    public void disableUser(String username) {
        User user = userRepo.findByUsername(username);
        user.setActive(false);
        userRepo.save(user);
    }

    public void removeUser(String username) {
        User user = userRepo.findByUsername(username);
        user.setVoided(true);
        userRepo.save(user);
    }

    public void updateUsername(UserDto user, String newUsername) {
        User userToUpdate = userRepo.findByUsername(user.getUsername());
        userToUpdate.setUsername(newUsername);
        userRepo.save(userToUpdate);
    }

    public void updatePassword(UserDto user, String password) {
        User userToUpdate = userRepo.findByUsername(user.getUsername());
        userToUpdate.setPassword(passwordEncoder.encode(password));
        userRepo.save(userToUpdate);
    }

    public void removeAll() {
        userRepo.deleteAll();
    }

    public void updateUser(UserDto user, String username, Boolean activity, String role) {
        User userToUpdate = userRepo.findByUsername(user.getUsername());
        userToUpdate.setUsername(username);
        userToUpdate.setActive(activity);
        userToUpdate.setRole(role);
        userRepo.save(userToUpdate);
    }
}
