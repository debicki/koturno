package com.github.sacull.koturno.controllers;

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
import java.util.Collections;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class HistoryPageControllerTests {

    @Autowired
    private WebApplicationContext context;

    @MockBean
    private InaccessibilityService inaccessibilityServiceMock;

    @MockBean
    private UserService userServiceMock;

    private MockMvc mvc;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(HistoryPageController.class);
        this.mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();
    }

    @Test
    public void shouldAnonymousUserGetRedirectResponse() throws Exception {
        mvc.perform(get("/history"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    @WithMockUser
    public void shouldReturnEmptyHistoryWithDefaultFilter() throws Exception {
        User user = new User("user", "user", true, "ROLE_USER");

        Mockito.when(userServiceMock.findByName(Mockito.anyString())).thenReturn(user);
        Mockito.when(inaccessibilityServiceMock.findAllByOrderByStartDesc())
                .thenReturn(Collections.emptyList());

        mvc.perform(get("/history"))
                .andExpect(model().attribute("limitedInaccessibilityList", Matchers.hasSize(0)))
                .andExpect(model().attribute("disabledMenuItem", Matchers.is("history")))
                .andExpect(status().isOk())
                .andExpect(forwardedUrl("/WEB-INF/views/history.jsp"));
    }

    @Test
    @WithMockUser
    public void shouldReturnHistoryWithOneEventWithFilterAll() throws Exception {
        User user = new User("user", "user", true, "ROLE_USER");
        HGroup hGroup = new HGroup("group", "");
        Host firstHost = new Host("firstHost", "localhost", "", "", hGroup);
        IGroup iGroup = new IGroup("group", "");
        Inaccessibility firstInaccessibility = new Inaccessibility(firstHost, "firstInaccessibility", iGroup);

        Mockito.when(userServiceMock.findByName(Mockito.anyString())).thenReturn(user);
        Mockito.when(inaccessibilityServiceMock.findAllByOrderByStartDesc())
                .thenReturn(Arrays.asList(firstInaccessibility));

        mvc.perform(get("/history?filter=all"))
                .andExpect(model().attribute("limitedInaccessibilityList", Matchers.hasSize(1)))
                .andExpect(model().attribute("limitedInaccessibilityList", Matchers.hasItem(
                        Matchers.hasProperty("description", Matchers.is("firstInaccessibility")))))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    public void shouldReturnEmptyHistoryWithFilterOnlyOffline() throws Exception {
        User user = new User("user", "user", true, "ROLE_USER");
        HGroup hGroup = new HGroup("group", "");
        Host firstHost = new Host("firstHost", "localhost", "", "", hGroup);
        IGroup iGroup = new IGroup("group", "");
        Inaccessibility firstInaccessibility = new Inaccessibility(firstHost, "firstInaccessibility", iGroup);
        firstInaccessibility.setActive(false);

        Mockito.when(userServiceMock.findByName(Mockito.anyString())).thenReturn(user);
        Mockito.when(inaccessibilityServiceMock.findAllByOrderByStartDesc())
                .thenReturn(Arrays.asList(firstInaccessibility));

        mvc.perform(get("/history?filter=only-offline"))
                .andExpect(model().attribute("limitedInaccessibilityList", Matchers.hasSize(0)))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    public void shouldReturnHistoryWithOneActiveAndTOneOldEventsWithFilterOnlyOffline() throws Exception {
        User user = new User("user", "user", true, "ROLE_USER");
        HGroup hGroup = new HGroup("group", "");
        Host firstHost = new Host("firstHost", "localhost", "", "", hGroup);
        Host secondHost = new Host("secondHost", "localhost", "", "", hGroup);
        IGroup iGroup = new IGroup("group", "");
        Inaccessibility firstInaccessibility = new Inaccessibility(firstHost, "firstInaccessibility", iGroup);
        Inaccessibility secondInaccessibility = new Inaccessibility(secondHost, "secondInaccessibility", iGroup);
        Inaccessibility thirdInaccessibility = new Inaccessibility(secondHost, "thirdInaccessibility", iGroup);
        firstInaccessibility.setOfflineStatus(true);
        secondInaccessibility.setActive(false);
        thirdInaccessibility.setOfflineStatus(true);
        thirdInaccessibility.setActive(false);

        Mockito.when(inaccessibilityServiceMock.findAllByOrderByStartDesc())
                .thenReturn(Arrays.asList(firstInaccessibility, secondInaccessibility, thirdInaccessibility));

        mvc.perform(get("/history?filter=only-offline"))
                .andExpect(model().attribute("limitedInaccessibilityList", Matchers.hasSize(1)))
                .andExpect(model().attribute("limitedInaccessibilityList", Matchers.hasItem(
                        Matchers.hasProperty("description", Matchers.is("firstInaccessibility")))))
                .andExpect(status().isOk());
    }
}
