package com.github.sacull.koturno.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

import com.github.sacull.koturno.entities.*;
import com.github.sacull.koturno.services.InaccessibilityService;
import com.github.sacull.koturno.services.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DashboardPageControllerTests {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;

    @Before
    public void setUp() {
        this.mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    public void test() throws Exception {
        User user = new User("user", "user", true, "ROLE_USER");
        HGroup hGroup = new HGroup("group", "");
        Host host = new Host("host", "localhost", "", hGroup, user);
        IGroup iGroup = new IGroup("group", "");
        Inaccessibility first = new Inaccessibility(host, "First inaccessibility", iGroup);
        Inaccessibility second = new Inaccessibility(host, "Second inaccessibility", iGroup);

        UserService userServiceMock = Mockito.mock(UserService.class);
        Mockito.when(userServiceMock.findByName(Mockito.anyString())).thenReturn(user);

        InaccessibilityService inaccessibilityServiceMock = Mockito.mock(InaccessibilityService.class);
        Mockito.when(inaccessibilityServiceMock.findAllByActiveIsTrueOrderByStartDesc(user))
                .thenReturn(Arrays.asList(first, second));

        mvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(forwardedUrl("/WEB-INF/views/dashboard.jsp"))
                .andExpect(model().attribute("instabilityHosts", hasSize(2)))   // TODO: 29.01.2020 Why ZERO? 
                .andExpect(model().attribute("instabilityHosts", hasItem(
                        hasProperty("description", is("First inaccessibility")))))
                .andExpect(model().attribute("instabilityHosts", hasItem(
                        hasProperty("description", is("Second inaccessibility")))));

        Mockito.verify(userServiceMock, Mockito.times(1)).findByName(Mockito.anyString());
        Mockito.verify(inaccessibilityServiceMock, Mockito.times(1))
                .findAllByActiveIsTrueOrderByStartDesc(user);
    }
}
