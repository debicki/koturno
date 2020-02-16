package com.github.sacull.koturno.repositories;

import com.github.sacull.koturno.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<User, Long> {

    User findByUsername(String username);

    Integer countAllByActive(Boolean active);
}
