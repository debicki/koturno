package com.github.sacull.koturno.configurations;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SecurityConfigTests {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;

    @Before
    public void applySecurity() {
        this.mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();
    }

    @Test
    public void shouldReturnRedirectionWhenAnonymousUserOpenApplicationPages() throws Exception {
        mvc.perform(get("/groups")).andExpect(status().is3xxRedirection());
        mvc.perform(get("/history")).andExpect(status().is3xxRedirection());
        mvc.perform(get("/hosts")).andExpect(status().is3xxRedirection());
        mvc.perform(get("/ping")).andExpect(status().is3xxRedirection());
    }

    @Test
    public void shouldReturnOkWhenAnonymousUserOpenRegisterPage() throws Exception {
        mvc.perform(get("/register")).andExpect(status().is2xxSuccessful());
    }

    @Test
    public void shouldReturnOkWhenAnonymousUserOpenLoginPage() throws Exception {
        mvc.perform(get("/login")).andExpect(status().is2xxSuccessful());
    }

    @Test
    public void shouldReturnOkWhenAnonymousUserOpenIndexPage() throws Exception {
        mvc.perform(get("/")).andExpect(status().is2xxSuccessful());
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    public void shouldReturnOkWhenLoggedUserOpenApplicationPages() throws Exception {
        mvc.perform(get("/groups")).andExpect(status().is2xxSuccessful());
        mvc.perform(get("/history")).andExpect(status().is2xxSuccessful());
        mvc.perform(get("/hosts")).andExpect(status().is2xxSuccessful());
        mvc.perform(get("/ping")).andExpect(status().is2xxSuccessful());
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    public void shouldReturnErrorWhenLoggedUserOpenLoginPage() throws Exception {
        mvc.perform(get("/login")).andExpect(status().is4xxClientError());
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    public void shouldReturnErrorWhenLoggedUserOpenRegisterPage() throws Exception {
        mvc.perform(get("/register")).andExpect(status().is4xxClientError());
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    public void shouldReturnOkWhenLoggerUserOpenIndexPage() throws Exception {
        mvc.perform(get("/")).andExpect(status().is2xxSuccessful());
    }
}
