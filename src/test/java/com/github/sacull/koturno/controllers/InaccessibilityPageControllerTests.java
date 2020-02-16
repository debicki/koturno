package com.github.sacull.koturno.controllers;

import com.github.sacull.koturno.entities.*;
import com.github.sacull.koturno.services.InaccessibilityService;
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
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class InaccessibilityPageControllerTests {

    @Autowired
    private WebApplicationContext context;

    @MockBean
    private InaccessibilityService inaccessibilityServiceMock;

    private MockMvc mvc;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(InaccessibilityPageController.class);
        this.mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();
    }

    @Test
    public void shouldAnonymousUserGetRedirectResponse() throws Exception {
        mvc.perform(get("/inaccessibility"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    @WithMockUser
    public void shouldRedirectWhenUnknownAction() throws Exception {
        User user = new User("user", "user", true, "ROLE_USER");
        HGroup hGroup = new HGroup("default", "");
        Host host = new Host("host", "localhost", "", hGroup);
        IGroup iGroup = new IGroup("default", "");
        Inaccessibility inaccessibility = new Inaccessibility(host, "inaccessibility", iGroup);

        Mockito.when(inaccessibilityServiceMock.getInaccessibilityById(Mockito.anyLong())).thenReturn(inaccessibility);

        mvc.perform(get("/inaccessibility?id=666&action=hell"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/"));
    }

    @Test
    @WithMockUser
    public void shouldRedirectWhenIgnoreAction() throws Exception {
        User user = new User("user", "user", true, "ROLE_USER");
        HGroup hGroup = new HGroup("default", "");
        Host host = new Host("host", "localhost", "", hGroup);
        IGroup iGroup = new IGroup("default", "");
        Inaccessibility inaccessibility = new Inaccessibility(host, "inaccessibility", iGroup);

        Mockito.when(inaccessibilityServiceMock.getInaccessibilityById(Mockito.anyLong())).thenReturn(inaccessibility);

        mvc.perform(get("/inaccessibility?id=666&action=ignore"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/"));
    }

    @Test
    @WithMockUser
    public void shouldRedirectWhenRemoveAction() throws Exception {
        User user = new User("user", "user", true, "ROLE_USER");
        HGroup hGroup = new HGroup("default", "");
        Host host = new Host("host", "localhost", "", hGroup);
        IGroup iGroup = new IGroup("default", "");
        Inaccessibility inaccessibility = new Inaccessibility(host, "inaccessibility", iGroup);

        Mockito.when(inaccessibilityServiceMock.getInaccessibilityById(Mockito.anyLong())).thenReturn(inaccessibility);

        mvc.perform(get("/inaccessibility?id=666&action=remove&filter=all"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/history?filter=all"));
    }

    @Test
    @WithMockUser
    public void shouldAddInaccessibilityToModelWhenInfoAction() throws Exception {
        User user = new User("user", "user", true, "ROLE_USER");
        HGroup hGroup = new HGroup("default", "");
        Host host = new Host("host", "localhost", "", hGroup);
        IGroup iGroup = new IGroup("default", "");
        Inaccessibility inaccessibility = new Inaccessibility(host, "inaccessibility", iGroup);

        Mockito.when(inaccessibilityServiceMock.getInaccessibilityById(Mockito.anyLong())).thenReturn(inaccessibility);

        mvc.perform(get("/inaccessibility?id=666&action=info"))
                .andExpect(status().isOk())
                .andExpect(forwardedUrl("/WEB-INF/views/inaccessibility.jsp"))
                .andExpect(model().attribute("inaccessibility",
                        Matchers.hasProperty("description", Matchers.is("inaccessibility"))));
    }

    @Test
    @WithMockUser
    public void shouldRedirectAfterInaccessibilityDescriptionChange() throws Exception {
        User user = new User("user", "user", true, "ROLE_USER");
        HGroup hGroup = new HGroup("default", "");
        Host host = new Host("host", "localhost", "", hGroup);
        IGroup iGroup = new IGroup("default", "");
        Inaccessibility inaccessibility = new Inaccessibility(host, "inaccessibility", iGroup);
        ReflectionTestUtils.setField(inaccessibility, "id", 666L);

        Mockito.when(inaccessibilityServiceMock.getInaccessibilityById(Mockito.anyLong())).thenReturn(inaccessibility);

        mvc.perform(post("/inaccessibility")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("id", "666")
                .param("description", "Something!!!")
                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/inaccessibility?id=666&action=info"));
    }
}
