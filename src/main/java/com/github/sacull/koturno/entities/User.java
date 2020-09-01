package com.github.sacull.koturno.entities;

import lombok.*;

import javax.persistence.*;

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
    private String theme;
    @Getter(value = AccessLevel.NONE)
    private Boolean voided;

    public User(String username, String password, Boolean active, String role, String theme) {
        this.username = username;
        this.password = password;
        this.active = active;
        this.role = role;
        this.theme = theme;
        this.voided = false;
    }

    public Boolean isVoided() {
        return this.voided;
    }
}
