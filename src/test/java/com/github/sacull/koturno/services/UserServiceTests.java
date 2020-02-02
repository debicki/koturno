package com.github.sacull.koturno.services;

import com.github.sacull.koturno.entities.User;
import com.github.sacull.koturno.repositories.UserRepo;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class UserServiceTests {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepo userRepoMock;

    @Mock
    private PasswordEncoder passwordEncoderMock;

    @Test
    public void shouldReturnValidUser() {
        String username = "user";
        String password = "pass";
        User user = new User(username, password, true, "ROLE_USER");

        Mockito.when(userRepoMock.findByUsername(username)).thenReturn(user);

        Assert.assertEquals(user, userService.findByName(username));
    }
}
