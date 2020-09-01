package com.github.sacull.koturno.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserDto {

    private final Long id;
    private final String username;
    private final String password;
    private final Boolean active;
    private final String role;
    private final String theme;

    public boolean isActive() {
        return active;
    }
}
