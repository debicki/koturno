package com.github.sacull.koturno.controllers;

import com.github.sacull.koturno.entities.*;
import com.github.sacull.koturno.services.HGroupService;
import com.github.sacull.koturno.services.HostService;
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
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;
import java.util.Collections;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class HostPageControllerTests {

    @Autowired
    private WebApplicationContext context;

    @MockBean
    private InaccessibilityService inaccessibilityServiceMock;

    @MockBean
    private HostService hostServiceMock;

    @MockBean
    private HGroupService hGroupServiceMock;

    @MockBean
    private UserService userServiceMock;

    private MockMvc mvc;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(GroupPageController.class);
        this.mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();
    }

    @Test
    public void shouldAnonymousUserGetRedirectResponse() throws Exception {
        mvc.perform(get("/group"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    @WithMockUser
    public void shouldReturnHostWithoutEvents() throws Exception {
        User user = new User("user", "user", true, "ROLE_USER");
        HGroup firstGroup = new HGroup("default", "");
        HGroup secondGroup = new HGroup("group", "");
        Host host = new Host("firstHost", "localhost", "", "", secondGroup);

        Mockito.when(hostServiceMock.getHostById(Mockito.anyLong())).thenReturn(host);
        Mockito.when(inaccessibilityServiceMock.findAllByHostOrderByStartDesc(Mockito.any(Host.class)))
                .thenReturn(Collections.emptyList());
        Mockito.when(hGroupServiceMock.getAllGroups()).thenReturn(Arrays.asList(firstGroup, secondGroup));

        mvc.perform(get("/host?id=666"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("host",
                        Matchers.hasProperty("name", Matchers.is("firstHost"))))
                .andExpect(model().attribute("inaccessibilityList", Matchers.hasSize(0)))
                .andExpect(model().attribute("hostGroupList", Matchers.hasSize(2)))
                .andExpect(forwardedUrl("/WEB-INF/views/host.jsp"));
    }

    @Test
    @WithMockUser
    public void shouldReturnHostWithTwoEvents() throws Exception {
        User user = new User("user", "user", true, "ROLE_USER");
        HGroup firstGroup = new HGroup("default", "");
        HGroup secondGroup = new HGroup("group", "");
        Host host = new Host("firstHost", "localhost", "", "", secondGroup);
        IGroup iGroup = new IGroup("group", "");
        Inaccessibility firstInaccessibility = new Inaccessibility(host, "firstInaccessibility", iGroup);
        Inaccessibility secondInaccessibility = new Inaccessibility(host, "secondInaccessibility", iGroup);


        Mockito.when(hostServiceMock.getHostById(Mockito.anyLong())).thenReturn(host);
        Mockito.when(inaccessibilityServiceMock.findAllByHostOrderByStartDesc(Mockito.any(Host.class)))
                .thenReturn(Arrays.asList(firstInaccessibility, secondInaccessibility));
        Mockito.when(hGroupServiceMock.getAllGroups()).thenReturn(Arrays.asList(firstGroup, secondGroup));

        mvc.perform(get("/host?id=666"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("host",
                        Matchers.hasProperty("name", Matchers.is("firstHost"))))
                .andExpect(model().attribute("inaccessibilityList", Matchers.hasSize(2)))
                .andExpect(model().attribute("inaccessibilityList", Matchers.hasItem(
                        Matchers.hasProperty("description", Matchers.is("firstInaccessibility")))))
                .andExpect(model().attribute("inaccessibilityList", Matchers.hasItem(
                        Matchers.hasProperty("description", Matchers.is("secondInaccessibility")))))
                .andExpect(model().attribute("hostGroupList", Matchers.hasSize(2)));
    }

    @Test
    @WithMockUser
    public void shouldRedirectAfterHostDelete() throws Exception {
        User user = new User("user", "user", true, "ROLE_USER");
        HGroup hGroup = new HGroup("group", "");
        Host host = new Host("firstHost", "localhost", "", "", hGroup);
        IGroup iGroup = new IGroup("group", "");
        Inaccessibility firstInaccessibility = new Inaccessibility(host, "firstInaccessibility", iGroup);
        Inaccessibility secondInaccessibility = new Inaccessibility(host, "secondInaccessibility", iGroup);


        Mockito.when(hostServiceMock.getHostById(Mockito.anyLong())).thenReturn(host);
        Mockito.when(inaccessibilityServiceMock.findAllByHost(Mockito.any(Host.class)))
                .thenReturn(Arrays.asList(firstInaccessibility, secondInaccessibility));

        mvc.perform(get("/host?id=666&action=remove"))
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attribute("error", Matchers.is("10")));
    }

    @Test
    @WithMockUser
    public void shouldReturnErrorWhenEditedHostGotUsedAddress() throws Exception {
        String originAddress = "localhost";
        String address = "remotehost";
        String activity = "";
        String name = "";
        String description = "";
        String hostGroupName = "";
        User user = new User("user", "user", true, "ROLE_USER");
        HGroup hGroup = new HGroup("default", "");
        Host host = new Host("host", originAddress, "", "", hGroup);
        Host hostInDatabase = new Host("host", address, "", "", hGroup);

        Mockito.when(userServiceMock.findByName(Mockito.anyString())).thenReturn(user);
        Mockito.when(hostServiceMock.getHostByAddress(Mockito.eq(originAddress))).thenReturn(host);
        Mockito.when(hostServiceMock.getHostByAddress(Mockito.eq(address))).thenReturn(hostInDatabase);

        mvc.perform(post("/host?id=666")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("originAddress", originAddress)
                .param("address", address)
                .param("activity", activity)
                .param("name", name)
                .param("description", description)
                .param("hostGroupName", hostGroupName)
                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(flash().attribute("error", Matchers.is("1")))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    @WithMockUser
    public void shouldReturnSuccessWhenEditedHostGotUnusedAddress() throws Exception {
        String originAddress = "localhost";
        String address = "remotehost";
        String activity = "";
        String name = "";
        String description = "";
        String hostGroupName = "";
        User user = new User("user", "user", true, "ROLE_USER");
        HGroup hGroup = new HGroup("default", "");
        Host host = new Host("host", originAddress, "", "", hGroup);

        Mockito.when(userServiceMock.findByName(Mockito.anyString())).thenReturn(user);
        Mockito.when(hostServiceMock.getHostByAddress(Mockito.eq(originAddress))).thenReturn(host);
        Mockito.when(hostServiceMock.getHostByAddress(Mockito.eq(address))).thenReturn(null);

        mvc.perform(post("/host?id=666")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("originAddress", originAddress)
                .param("address", address)
                .param("activity", activity)
                .param("name", name)
                .param("description", description)
                .param("hostGroupName", hostGroupName)
                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(flash().attribute("error", Matchers.is("0")))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    @WithMockUser
    public void shouldReturnSuccessWhenEditedHostDontGetNewAddress() throws Exception {
        String originAddress = "localhost";
        String address = originAddress;
        String activity = "";
        String name = "";
        String description = "";
        String hostGroupName = "";
        User user = new User("user", "user", true, "ROLE_USER");
        HGroup hGroup = new HGroup("default", "");
        Host host = new Host("host", originAddress, "", "", hGroup);
        Host hostInDatabase = new Host("host", address, "", "", hGroup);

        Mockito.when(userServiceMock.findByName(Mockito.anyString())).thenReturn(user);
        Mockito.when(hostServiceMock.getHostByAddress(Mockito.eq(originAddress))).thenReturn(host);
        Mockito.when(hostServiceMock.getHostByAddress(Mockito.eq(address))).thenReturn(hostInDatabase);

        mvc.perform(post("/host?id=666")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("originAddress", originAddress)
                .param("address", address)
                .param("activity", activity)
                .param("name", name)
                .param("description", description)
                .param("hostGroupName", hostGroupName)
                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(flash().attribute("error", Matchers.is("0")))
                .andExpect(status().is3xxRedirection());
    }
}
