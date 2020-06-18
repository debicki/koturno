package com.github.sacull.koturno.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserDto {

    private final String username;
    private final Boolean active;
    private final String role;

    public boolean isActive() {
        return active;
    }
}
