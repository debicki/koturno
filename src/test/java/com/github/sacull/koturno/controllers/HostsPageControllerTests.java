package com.github.sacull.koturno.controllers;

import com.github.sacull.koturno.entities.*;
import com.github.sacull.koturno.services.*;
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
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class HostsPageControllerTests {

    @Autowired
    private WebApplicationContext context;

    @MockBean
    private HostService hostServiceMock;

    @MockBean
    private InaccessibilityService inaccessibilityServiceMock;

    @MockBean
    private HGroupService hGroupServiceMock;

    @MockBean
    private UserService userServiceMock;

    @MockBean
    private FileService fileServiceMock;

    private MockMvc mvc;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(HostsPageController.class);
        this.mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();
    }

    @Test
    public void shouldAnonymousUserGetRedirectResponse() throws Exception {
        mvc.perform(get("/hosts"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    @WithMockUser
    public void shouldReturnEmptyHostsListAndOneGroup() throws Exception {
        User user = new User("user", "user", true, "ROLE_USER");
        HGroup hGroup = new HGroup("default", "");

        Mockito.when(userServiceMock.findByName(Mockito.anyString())).thenReturn(user);
        Mockito.when(hostServiceMock.findAllByName())
                .thenReturn(Collections.emptyList());
        Mockito.when(hGroupServiceMock.getAllGroups()).thenReturn(Arrays.asList(hGroup));
        Mockito.when(inaccessibilityServiceMock.findAllByActiveIsTrue()).thenReturn(Collections.emptyList());

        mvc.perform(get("/hosts"))
                .andExpect(status().isOk())
                .andExpect(forwardedUrl("/WEB-INF/views/hosts.jsp"))
                .andExpect(model().attribute("hosts", Matchers.hasSize(0)))
                .andExpect(model().attribute("hostGroupList", Matchers.hasSize(1)))
                .andExpect(model().attribute("hostGroupList", Matchers.hasItem(
                        Matchers.hasProperty("name", Matchers.is("default")))))
                .andExpect(model().attribute("unstableHosts", Matchers.hasSize(0)))
                .andExpect(model().attribute("offlineHosts", Matchers.hasSize(0)))
                .andExpect(model().attribute("disabledMenuItem", Matchers.is("hosts")));
    }

    @Test
    @WithMockUser
    public void shouldReturnOneEmptyGroupAndOneGroupWithTwoHostsWithEventsAndOneWitout() throws Exception {
        User user = new User("user", "user", true, "ROLE_USER");
        HGroup firstGroup = new HGroup("default", "");
        HGroup secondGroup = new HGroup("test", "");
        Host firstHost = new Host("firstHost", "localhost", "", secondGroup);
        Host secondHost = new Host("secondHost", "localhost", "", secondGroup);
        Host thirdHost = new Host("thirdHost", "localhost", "", secondGroup);
        IGroup iGroup = new IGroup("group", "");
        Inaccessibility firstInaccessibility = new Inaccessibility(firstHost, "firstInaccessibility", iGroup);
        Inaccessibility secondInaccessibility = new Inaccessibility(secondHost, "secondInaccessibility", iGroup);
        secondInaccessibility.setOfflineStatus(true);

        Mockito.when(userServiceMock.findByName(Mockito.anyString())).thenReturn(user);
        Mockito.when(hostServiceMock.findAllByName())
                .thenReturn(Arrays.asList(firstHost, secondHost, thirdHost));
        Mockito.when(hGroupServiceMock.getAllGroups()).thenReturn(Arrays.asList(firstGroup, secondGroup));
        Mockito.when(inaccessibilityServiceMock.findAllByActiveIsTrue())
                .thenReturn(Arrays.asList(firstInaccessibility, secondInaccessibility));

        mvc.perform(get("/hosts"))
                .andExpect(status().isOk())
                .andExpect(forwardedUrl("/WEB-INF/views/hosts.jsp"))
                .andExpect(model().attribute("hosts", Matchers.hasSize(3)))
                .andExpect(model().attribute("hosts", Matchers.hasItem(
                        Matchers.hasProperty("name", Matchers.is("firstHost")))))
                .andExpect(model().attribute("hosts", Matchers.hasItem(
                        Matchers.hasProperty("name", Matchers.is("secondHost")))))
                .andExpect(model().attribute("hosts", Matchers.hasItem(
                        Matchers.hasProperty("name", Matchers.is("thirdHost")))))
                .andExpect(model().attribute("hostGroupList", Matchers.hasSize(2)))
                .andExpect(model().attribute("hostGroupList", Matchers.hasItem(
                        Matchers.hasProperty("name", Matchers.is("default")))))
                .andExpect(model().attribute("hostGroupList", Matchers.hasItem(
                        Matchers.hasProperty("name", Matchers.is("test")))))
                .andExpect(model().attribute("unstableHosts", Matchers.hasSize(1)))
                .andExpect(model().attribute("unstableHosts", Matchers.hasItem(
                        Matchers.hasProperty("name", Matchers.is("firstHost")))))
                .andExpect(model().attribute("offlineHosts", Matchers.hasSize(1)))
                .andExpect(model().attribute("offlineHosts", Matchers.hasItem(
                        Matchers.hasProperty("name", Matchers.is("secondHost")))))
                .andExpect(model().attribute("disabledMenuItem", Matchers.is("hosts")));
    }

    @Test
    @WithMockUser
    public void shouldReturnErrorWhenTrySaveHostWithUsedAddress() throws Exception {
        String address = "localHost";
        String activity = "Nieaktywny";
        String name = "name";
        String description = "";
        String hostGroupName = "default";
        User user = new User("user", "user", true, "ROLE_USER");
        HGroup hGroup = new HGroup(hostGroupName, "");
        Host host = new Host(name, address, description, hGroup);

        Mockito.when(userServiceMock.findByName(Mockito.anyString())).thenReturn(user);
        Mockito.when(hostServiceMock.getHostByAddress(Mockito.eq(address)))
                .thenReturn(host);

        mvc.perform(post("/hosts")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("address", address)
                .param("activity", activity)
                .param("name", name)
                .param("description", description)
                .param("hostGroupName", hostGroupName)
                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attribute("error", Matchers.is("1")));
    }

    @Test
    @WithMockUser
    public void shouldReturnSuccessWhenHostIsAdded() throws Exception {
        String address = "localHost";
        String activity = "Nieaktywny";
        String name = "name";
        String description = "";
        String hostGroupName = "default";
        User user = new User("user", "user", true, "ROLE_USER");
        HGroup hGroup = new HGroup(hostGroupName, "");

        Mockito.when(userServiceMock.findByName(Mockito.anyString())).thenReturn(user);
        Mockito.when(hostServiceMock.getHostByAddress(Mockito.eq(address)))
                .thenReturn(null);
        Mockito.when(hGroupServiceMock.getGroupByName(Mockito.anyString())).thenReturn(hGroup);
        Mockito.when(fileServiceMock.isValidAddress(Mockito.anyString())).thenReturn(true);

        mvc.perform(post("/hosts")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("address", address)
                .param("activity", activity)
                .param("name", name)
                .param("description", description)
                .param("hostGroupName", hostGroupName)
                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attribute("error", Matchers.is("0")));
    }

    @Test
    @WithMockUser
    public void shouldReturnWarningWhenHostIsAddedAndNotRecognized() throws Exception {
        String address = "localHost";
        String activity = "Nieaktywny";
        String name = "name";
        String description = "";
        String hostGroupName = "default";
        User user = new User("user", "user", true, "ROLE_USER");
        HGroup hGroup = new HGroup(hostGroupName, "");

        Mockito.when(userServiceMock.findByName(Mockito.anyString())).thenReturn(user);
        Mockito.when(hostServiceMock.getHostByAddress(Mockito.eq(address)))
                .thenReturn(null);
        Mockito.when(hGroupServiceMock.getGroupByName(Mockito.anyString())).thenReturn(hGroup);
        Mockito.when(fileServiceMock.isValidAddress(Mockito.anyString())).thenReturn(false);

        mvc.perform(post("/hosts")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("address", address)
                .param("activity", activity)
                .param("name", name)
                .param("description", description)
                .param("hostGroupName", hostGroupName)
                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attribute("error", Matchers.is("3")));
    }

    @Test
    @WithMockUser
    public void shouldProcessImportFile() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file.txt", new byte[]{});
        User user = new User("user", "user", true, "ROLE_USER");
        Map<String, Integer> report = new HashMap<>();
        report.put("importSuccess", 6);
        report.put("importWarnings", 5);
        report.put("importErrors", 4);

        Mockito.when(userServiceMock.findByName(Mockito.anyString())).thenReturn(user);
        Mockito.when(fileServiceMock.hostsImport(
                Mockito.any(User.class), Mockito.anyMap(), Mockito.any()))
                .thenReturn(report);

        mvc.perform(MockMvcRequestBuilders.multipart("/hosts/import")
                .file(file)
                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attribute("importSuccess", Matchers.is(6)))
                .andExpect(flash().attribute("importWarnings", Matchers.is(5)))
                .andExpect(flash().attribute("importErrors", Matchers.is(4)));
    }
}
