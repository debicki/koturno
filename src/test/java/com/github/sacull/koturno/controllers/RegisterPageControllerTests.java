package com.github.sacull.koturno.controllers;

import com.github.sacull.koturno.entities.User;
import com.github.sacull.koturno.services.UserService;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RegisterPageControllerTests {

    @Autowired
    private WebApplicationContext context;

    @MockBean
    private UserService userServiceMock;

    private MockMvc mvc;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(RegisterPageController.class);
        this.mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();
    }

    @Test
    public void shouldAnonymousUserGetRegisterPage() throws Exception {
        mvc.perform(get("/register"))
                .andExpect(status().isOk())
                .andExpect(forwardedUrl("/WEB-INF/views/register.jsp"));
    }

    @Test
    @WithMockUser
    public void shouldLoggedUserGetError() throws Exception {
        mvc.perform(get("/register"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void shouldReturnErrorWhenUserWantUseUsedUsername() throws Exception {
        String username = "user";
        String password = "pass";
        String password2 = "pass";
        User user = new User(username, password, true, "ROLE_USER");
        Mockito.when(userServiceMock.findByName(Mockito.anyString())).thenReturn(user);

        mvc.perform(post("/register")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("username", username)
                .param("password", password)
                .param("password2", password2)
                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/register"))
                .andExpect(flash().attribute("error", Matchers.is("31")));
    }

    @Test
    public void shouldReturnErrorWhenPasswordsAreNotEqual() throws Exception {
        String username = "user";
        String password = "pass";
        String password2 = "password";
        Mockito.when(userServiceMock.findByName(Mockito.anyString())).thenReturn(null);

        mvc.perform(post("/register")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("username", username)
                .param("password", password)
                .param("password2", password2)
                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/register"))
                .andExpect(flash().attribute("error", Matchers.is("32")));
    }

    @Test
    public void shouldRedirectToLoginPagePastSuccessRegister() throws Exception {
        String username = "user";
        String password = "pass";
        String password2 = "pass";
        Mockito.when(userServiceMock.findByName(Mockito.anyString())).thenReturn(null);

        mvc.perform(post("/register")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("username", username)
                .param("password", password)
                .param("password2", password2)
                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/login"))
                .andExpect(flash().attribute("error", Matchers.nullValue()));
    }
}
