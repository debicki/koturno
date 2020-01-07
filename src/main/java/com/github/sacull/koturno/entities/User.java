package com.github.sacull.koturno.entities;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;
    private Boolean active;
    private String role;

    public User(String username, String password, Boolean active, String role) {
        this.username = username;
        this.password = password;
        this.active = active;
        this.role = role;
    }
}
