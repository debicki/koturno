package com.github.sacull.koturno.controllers;

import com.github.sacull.koturno.entities.HGroup;
import com.github.sacull.koturno.entities.Host;
import com.github.sacull.koturno.entities.User;
import com.github.sacull.koturno.services.HGroupService;
import com.github.sacull.koturno.services.HostService;
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
public class GroupsPageControllerTests {

    @Autowired
    private WebApplicationContext context;

    @MockBean
    private HGroupService hGroupServiceMock;

    @MockBean
    private HostService hostServiceMock;

    @MockBean
    private UserService userServiceMock;

    private MockMvc mvc;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(GroupsPageController.class);
        this.mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();
    }

    @Test
    public void shouldAnonymousUserGetRedirectResponse() throws Exception {
        mvc.perform(get("/groups"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    @WithMockUser
    public void shouldModelHaveListWithOneEmptyGroup() throws Exception {
        User user = new User("user", "user", true, "ROLE_USER");
        HGroup hGroup = new HGroup("default", "");

        Mockito.when(userServiceMock.findByName(Mockito.anyString())).thenReturn(user);
        Mockito.when(hGroupServiceMock.getAllGroups()).thenReturn(Arrays.asList(hGroup));
        Mockito.when(hostServiceMock.getAllHosts()).thenReturn(Collections.emptyList());

        mvc.perform(get("/groups"))
                .andExpect(model().attribute("groups", Matchers.hasSize(1)))
                .andExpect(model().attribute("groupMembersCounter", Matchers.hasEntry("default", 0)))
                .andExpect(model().attribute("disabledMenuItem", Matchers.is("groups")))
                .andExpect(forwardedUrl("/WEB-INF/views/groups.jsp"));
    }

    @Test
    @WithMockUser
    public void shouldModelHaveListWithDefaultGroupWithTwoHosts() throws Exception {
        User user = new User("user", "user", true, "ROLE_USER");
        HGroup hGroup = new HGroup("default", "");
        Host firstHost = new Host("firstHost", "localhost", "", hGroup);
        Host secondHost = new Host("secondHost", "localhost", "", hGroup);

        Mockito.when(userServiceMock.findByName(Mockito.anyString())).thenReturn(user);
        Mockito.when(hGroupServiceMock.getAllGroups()).thenReturn(Arrays.asList(hGroup));
        Mockito.when(hostServiceMock.getAllHosts())
                .thenReturn(Arrays.asList(firstHost, secondHost));

        mvc.perform(get("/groups"))
                .andExpect(model().attribute("groups", Matchers.hasSize(1)))
                .andExpect(model().attribute("groupMembersCounter", Matchers.hasEntry("default", 2)));
    }

    @Test
    @WithMockUser
    public void shouldModelHaveListWithGroupWithTwoHostsAndEmptyDefaultGroup() throws Exception {
        User user = new User("user", "user", true, "ROLE_USER");
        HGroup firstGroup = new HGroup("default", "");
        HGroup secondGroup = new HGroup("test", "");
        Host firstHost = new Host("firstHost", "localhost", "", secondGroup);
        Host secondHost = new Host("secondHost", "localhost", "", secondGroup);

        Mockito.when(userServiceMock.findByName(Mockito.anyString())).thenReturn(user);
        Mockito.when(hGroupServiceMock.getAllGroups()).thenReturn(Arrays.asList(firstGroup, secondGroup));
        Mockito.when(hostServiceMock.getAllHosts())
                .thenReturn(Arrays.asList(firstHost, secondHost));

        mvc.perform(get("/groups"))
                .andExpect(model().attribute("groups", Matchers.hasSize(2)))
                .andExpect(model().attribute("groupMembersCounter", Matchers.hasEntry("default", 0)))
                .andExpect(model().attribute("groupMembersCounter", Matchers.hasEntry("test", 2)));
    }

    @Test
    @WithMockUser
    public void shouldReturnNoErrorsWhenCreateGroup() throws Exception {
        String name = "name";
        String description = "description";

        Mockito.when(hGroupServiceMock.getGroupByName(Mockito.anyString())).thenReturn(null);

        mvc.perform(post("/groups")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("name", name)
                .param("description", description)
                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(flash().attribute("error", Matchers.is("0")))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    @WithMockUser
    public void shouldReturnErrorsWhenCreateGroupWithUsedName() throws Exception {
        String name = "name";
        String description = "description";
        HGroup hGroup = new HGroup(name, description);

        Mockito.when(hGroupServiceMock.getGroupByName(Mockito.anyString())).thenReturn(hGroup);

        mvc.perform(post("/groups")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("name", name)
                .param("description", description)
                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(flash().attribute("error", Matchers.is("2")))
                .andExpect(status().is3xxRedirection());
    }
}
