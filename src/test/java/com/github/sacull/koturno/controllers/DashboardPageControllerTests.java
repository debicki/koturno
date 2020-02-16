package com.github.sacull.koturno.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.github.sacull.koturno.entities.*;
import com.github.sacull.koturno.services.InaccessibilityService;
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

    @MockBean
    private UserService userServiceMock;

    @MockBean
    private InaccessibilityService inaccessibilityServiceMock;

    private MockMvc mvc;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(DashboardPageController.class);
        this.mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    public void shouldPrepareDashboardPageForLoggedUser() throws Exception {
        User user = new User("user", "user", true, "ROLE_USER");
        HGroup hGroup = new HGroup("group", "");
        Host host = new Host("host", "localhost", "", hGroup);
        IGroup iGroup = new IGroup("group", "");
        Inaccessibility first = new Inaccessibility(host, "First inaccessibility", iGroup);
        Inaccessibility second = new Inaccessibility(host, "Second inaccessibility", iGroup);

        Mockito.when(userServiceMock.findByName(Mockito.anyString())).thenReturn(user);

        Mockito.when(inaccessibilityServiceMock.findAllByActiveIsTrueOrderByStartDesc())
                .thenReturn(Arrays.asList(first, second));

        mvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(forwardedUrl("/WEB-INF/views/dashboard.jsp"))
                .andExpect(model().attribute("instabilityHosts", Matchers.hasSize(2)))
                .andExpect(model().attribute("instabilityHosts", Matchers.hasItem(
                        Matchers.hasProperty("description", Matchers.is("First inaccessibility")))))
                .andExpect(model().attribute("instabilityHosts", Matchers.hasItem(
                        Matchers.hasProperty("description", Matchers.is("Second inaccessibility")))));

        Mockito.verify(userServiceMock, Mockito.times(1)).findByName(Mockito.anyString());
        Mockito.verify(inaccessibilityServiceMock, Mockito.times(1))
                .findAllByActiveIsTrueOrderByStartDesc();
    }

    @Test
    public void shouldPrepareDashboardPageForAnonymousUser() throws Exception {
        User user = new User("user", "user", true, "ROLE_USER");
        HGroup hGroup = new HGroup("group", "");
        Host host = new Host("host", "localhost", "", hGroup);
        IGroup iGroup = new IGroup("group", "");
        Inaccessibility first = new Inaccessibility(host, "First inaccessibility", iGroup);
        Inaccessibility second = new Inaccessibility(host, "Second inaccessibility", iGroup);

        Mockito.when(userServiceMock.findByName(Mockito.anyString())).thenReturn(user);

        Mockito.when(inaccessibilityServiceMock.findAllByActiveIsTrueOrderByStartDesc())
                .thenReturn(Arrays.asList(first, second));

        mvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(forwardedUrl("/WEB-INF/views/dashboard.jsp"))
                .andExpect(model().attribute("instabilityHosts", Matchers.hasSize(0)));

        Mockito.verify(userServiceMock, Mockito.times(0)).findByName(Mockito.anyString());
        Mockito.verify(inaccessibilityServiceMock, Mockito.times(0))
                .findAllByActiveIsTrueOrderByStartDesc();
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    public void shouldSetActiveMenuItemForLoggedUser() throws Exception {
        mvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(forwardedUrl("/WEB-INF/views/dashboard.jsp"))
                .andExpect(model().attribute("disabledMenuItem", Matchers.equalTo("dashboard")));
    }

    @Test
    public void shouldSetActiveMenuItemForAnonymousUser() throws Exception {
        mvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(forwardedUrl("/WEB-INF/views/dashboard.jsp"))
                .andExpect(model().attribute("disabledMenuItem", Matchers.equalTo("dashboard")));
    }
}
