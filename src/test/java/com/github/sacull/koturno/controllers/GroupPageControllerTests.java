package com.github.sacull.koturno.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
import org.springframework.web.util.NestedServletException;

import javax.persistence.EntityNotFoundException;
import java.util.Arrays;
import java.util.Collections;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GroupPageControllerTests {

    @Autowired
    private WebApplicationContext context;

    @MockBean
    private HGroupService hGroupServiceMock;

    @MockBean
    private HostService hostServiceMock;

    @MockBean
    private InaccessibilityService inaccessibilityServiceMock;

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
    public void shouldReturnValidModelAttributesWithEmptyGroup() throws Exception {
        User user = new User("user", "user", true, "ROLE_USER");
        HGroup hGroup = new HGroup("group", "");

        Mockito.when(hGroupServiceMock.getGroupById(Mockito.anyLong())).thenReturn(hGroup);
        Mockito.when(userServiceMock.findByName(Mockito.anyString())).thenReturn(user);
        Mockito.when(hostServiceMock.findAllByHostGroup(Mockito.any(HGroup.class), Mockito.any(User.class)))
                .thenReturn(Collections.emptyList());
        Mockito.when(inaccessibilityServiceMock.findAllByActiveIsTrue()).thenReturn(Collections.emptyList());

        mvc.perform(get("/group?id=666"))
                .andExpect(status().isOk())
                .andExpect(forwardedUrl("/WEB-INF/views/group.jsp"))
                .andExpect(model().attribute("group", Matchers.equalTo(hGroup)))
                .andExpect(model().attribute("hosts", Matchers.hasSize(0)))
                .andExpect(model().attribute("unstableHosts", Matchers.hasSize(0)))
                .andExpect(model().attribute("offlineHosts", Matchers.hasSize(0)));

        Mockito.verify(hGroupServiceMock, Mockito.times(1)).getGroupById(Mockito.anyLong());
        Mockito.verify(userServiceMock, Mockito.times(1)).findByName(Mockito.anyString());
        Mockito.verify(hostServiceMock, Mockito.times(1))
                .findAllByHostGroup(Mockito.any(HGroup.class), Mockito.any(User.class));
        Mockito.verify(inaccessibilityServiceMock, Mockito.times(1)).findAllByActiveIsTrue();
    }

    @Test
    @WithMockUser
    public void shouldReturnValidModelAttributesWithGroupWithTwoHosts() throws Exception {
        User user = new User("user", "user", true, "ROLE_USER");
        HGroup hGroup = new HGroup("group", "");
        Host firstHost = new Host("firstHost", "localhost", "", hGroup, user);
        Host secondHost = new Host("secondHost", "localhost", "", hGroup, user);

        Mockito.when(hGroupServiceMock.getGroupById(Mockito.anyLong())).thenReturn(hGroup);
        Mockito.when(userServiceMock.findByName(Mockito.anyString())).thenReturn(user);
        Mockito.when(hostServiceMock.findAllByHostGroup(Mockito.any(HGroup.class), Mockito.any(User.class)))
                .thenReturn(Arrays.asList(firstHost, secondHost));
        Mockito.when(inaccessibilityServiceMock.findAllByActiveIsTrue()).thenReturn(Collections.emptyList());

        mvc.perform(get("/group?id=666"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("group", Matchers.equalTo(hGroup)))
                .andExpect(model().attribute("hosts", Matchers.hasSize(2)))
                .andExpect(model().attribute("hosts", Matchers.hasItem(
                        Matchers.hasProperty("name", Matchers.is("firstHost")))))
                .andExpect(model().attribute("hosts", Matchers.hasItem(
                        Matchers.hasProperty("name", Matchers.is("secondHost")))))
                .andExpect(model().attribute("unstableHosts", Matchers.hasSize(0)))
                .andExpect(model().attribute("offlineHosts", Matchers.hasSize(0)));
    }

    @Test
    @WithMockUser
    public void shouldReturnValidModelAttributesWithGroupWithTwoUnstableAndOneOfflineHosts() throws Exception {
        User user = new User("user", "user", true, "ROLE_USER");
        HGroup hGroup = new HGroup("group", "");
        Host firstHost = new Host("firstHost", "localhost", "", hGroup, user);
        Host secondHost = new Host("secondHost", "localhost", "", hGroup, user);
        Host thirdHost = new Host("thirdHost", "localhost", "", hGroup, user);
        Host fourthHost = new Host("fourthHost", "localhost", "", hGroup, user);
        IGroup iGroup = new IGroup("group", "");
        Inaccessibility firstInaccessibility = new Inaccessibility(firstHost, "firstInaccessibility", iGroup);
        Inaccessibility secondInaccessibility = new Inaccessibility(thirdHost, "secondInaccessibility", iGroup);
        Inaccessibility thirdInaccessibility = new Inaccessibility(fourthHost, "thirdInaccessibility", iGroup);
        secondInaccessibility.setOfflineStatus(true);

        Mockito.when(hGroupServiceMock.getGroupById(Mockito.anyLong())).thenReturn(hGroup);
        Mockito.when(userServiceMock.findByName(Mockito.anyString())).thenReturn(user);
        Mockito.when(hostServiceMock.findAllByHostGroup(Mockito.any(HGroup.class), Mockito.any(User.class)))
                .thenReturn(Arrays.asList(firstHost, secondHost, thirdHost, fourthHost));
        Mockito.when(inaccessibilityServiceMock.findAllByActiveIsTrue())
                .thenReturn(Arrays.asList(firstInaccessibility, secondInaccessibility, thirdInaccessibility));

        mvc.perform(get("/group?id=666"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("group", Matchers.equalTo(hGroup)))
                .andExpect(model().attribute("hosts", Matchers.hasSize(4)))
                .andExpect(model().attribute("unstableHosts", Matchers.hasSize(2)))
                .andExpect(model().attribute("offlineHosts", Matchers.hasSize(1)))
                .andExpect(model().attribute("offlineHosts", Matchers.hasItem(
                        Matchers.hasProperty("name", Matchers.is("thirdHost")))));
    }

    @Test(expected = NestedServletException.class)
    @WithMockUser
    public void shouldReturnErrorWhenInvalidGroupIdRequested() throws Exception {
        Mockito.when(hGroupServiceMock.getGroupById(Mockito.anyLong())).thenThrow(new EntityNotFoundException());

        mvc.perform(get("/group?id=666"));

        Mockito.verify(hGroupServiceMock, Mockito.times(1)).getGroupById(Mockito.anyLong());
    }

    @Test
    @WithMockUser
    public void shouldSuccessfulDeleteGroup() throws Exception {
        User user = new User("user", "user", true, "ROLE_USER");
        HGroup hGroup = new HGroup("group", "");

        Mockito.when(hGroupServiceMock.getGroupById(Mockito.anyLong())).thenReturn(hGroup);
        Mockito.when(userServiceMock.findByName(Mockito.anyString())).thenReturn(user);
        Mockito.when(hostServiceMock.findAllByHostGroup(Mockito.any(HGroup.class), Mockito.any(User.class)))
                .thenReturn(Collections.emptyList());
        Mockito.when(hostServiceMock.countAllByHostGroup(hGroup)).thenReturn(0L);

        mvc.perform(get("/group?id=666&action=remove"))
                .andExpect(flash().attribute("error", Matchers.is("10")))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    @WithMockUser
    public void shouldFailWhenDeleteDefaultGroup() throws Exception {
        User user = new User("user", "user", true, "ROLE_USER");
        HGroup hGroup = new HGroup("default", "");

        Mockito.when(hGroupServiceMock.getGroupById(Mockito.anyLong())).thenReturn(hGroup);
        Mockito.when(userServiceMock.findByName(Mockito.anyString())).thenReturn(user);
        Mockito.when(hostServiceMock.findAllByHostGroup(Mockito.any(HGroup.class), Mockito.any(User.class)))
                .thenReturn(Collections.emptyList());
        Mockito.when(hostServiceMock.countAllByHostGroup(hGroup)).thenReturn(0L);

        mvc.perform(get("/group?id=666&action=remove"))
                .andExpect(flash().attribute("error", Matchers.is("12")))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    @WithMockUser
    public void shouldFailWhenDeleteGroupWithHosts() throws Exception {
        User user = new User("user", "user", true, "ROLE_USER");
        HGroup hGroup = new HGroup("group", "");

        Mockito.when(hGroupServiceMock.getGroupById(Mockito.anyLong())).thenReturn(hGroup);
        Mockito.when(userServiceMock.findByName(Mockito.anyString())).thenReturn(user);
        Mockito.when(hostServiceMock.findAllByHostGroup(Mockito.any(HGroup.class), Mockito.any(User.class)))
                .thenReturn(Collections.emptyList());
        Mockito.when(hostServiceMock.countAllByHostGroup(hGroup)).thenReturn(666L);

        mvc.perform(get("/group?id=666&action=remove"))
                .andExpect(flash().attribute("error", Matchers.is("11")))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    @WithMockUser
    public void shouldFailWhenEditDefaultGroup() throws Exception {
        String oldName = "default";
        String newName = "test";
        String newDescription = "test";
        HGroup hGroup = new HGroup(oldName, "");

        Mockito.when(hGroupServiceMock.getGroupByName("default")).thenReturn(hGroup);

        mvc.perform(post("/group")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("originName", oldName)
                .param("name", newName)
                .param("description", newDescription)
                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attribute("error", Matchers.is("3")));
    }

    @Test
    @WithMockUser
    public void shouldFailWhenNewNameAreUsed() throws Exception {
        String oldName = "test";
        String newName = "testing";
        String newDescription = "test";
        HGroup hGroup = new HGroup(oldName, "");
        HGroup hGroupInDatabase = new HGroup(newName, "");

        Mockito.when(hGroupServiceMock.getGroupByName(oldName)).thenReturn(hGroup);
        Mockito.when(hGroupServiceMock.getGroupByName(newName)).thenReturn(hGroupInDatabase);

        mvc.perform(post("/group")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("originName", oldName)
                .param("name", newName)
                .param("description", newDescription)
                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attribute("error", Matchers.is("2")));
    }

    @Test
    @WithMockUser
    public void shouldSuccessWhenNewNameArentUsed() throws Exception {
        String oldName = "test";
        String newName = "testing";
        String newDescription = "test";
        HGroup hGroup = new HGroup(oldName, "");

        Mockito.when(hGroupServiceMock.getGroupByName(oldName)).thenReturn(hGroup);
        Mockito.when(hGroupServiceMock.getGroupByName(newName)).thenReturn(null);

        mvc.perform(post("/group")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("originName", oldName)
                .param("name", newName)
                .param("description", newDescription)
                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attribute("error", Matchers.is("0")));
    }

    @Test
    @WithMockUser
    public void shouldSuccessWhenNewAndOldNamesAreEqual() throws Exception {
        String oldName = "test";
        String newName = "test";
        String newDescription = "test";
        HGroup hGroup = new HGroup(oldName, "");

        Mockito.when(hGroupServiceMock.getGroupByName(newName)).thenReturn(hGroup);

        mvc.perform(post("/group")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("originName", oldName)
                .param("name", newName)
                .param("description", newDescription)
                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attribute("error", Matchers.is("0")));
    }
}
