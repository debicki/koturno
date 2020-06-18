package com.github.sacull.koturno.utils;

import com.github.sacull.koturno.dtos.UserDto;
import com.github.sacull.koturno.entities.User;
import org.springframework.stereotype.Component;

@Component
public class ModelMapper {

    public UserDto convert(User user) {
        return new UserDto(user.getUsername(), user.getActive(), user.getRole());
    }
}
