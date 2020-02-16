package com.github.sacull.koturno.services;

import com.github.sacull.koturno.entities.HGroup;
import com.github.sacull.koturno.entities.Host;
import com.github.sacull.koturno.entities.User;
import com.github.sacull.koturno.repositories.HostRepo;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;

@RunWith(SpringRunner.class)
public class HostServiceTests {

    @InjectMocks
    private HostService hostService;

    @Mock
    private HostRepo hostRepoMock;

    @Test
    public void shouldReturnListWithAllHosts() {
        User user = new User("user", "user", true, "ROLE_USER");
        User admin = new User("admin", "admin", true, "ROLE_ADMIN");
        HGroup firstGroup = new HGroup("default", "");
        HGroup secondGroup = new HGroup("default", "");
        Host firstHost = new Host("firstHost", "localhost", "", firstGroup);
        Host secondHost = new Host("secondHost", "localhost", "", secondGroup);
        Host thirdHost = new Host("thirdHost", "localhost", "", firstGroup);

        Mockito.when(hostRepoMock.findAll()).thenReturn(Arrays.asList(firstHost, secondHost, thirdHost));

        Assert.assertEquals(3, hostService.getAllHosts().size());
    }

    @Test
    public void shouldReturnListWithAllHostsOfUser() {
        User user = new User("user", "user", true, "ROLE_USER");
        User admin = new User("admin", "admin", true, "ROLE_ADMIN");
        HGroup firstGroup = new HGroup("default", "");
        HGroup secondGroup = new HGroup("default", "");
        Host firstHost = new Host("firstHost", "localhost", "", firstGroup);
        Host secondHost = new Host("secondHost", "localhost", "", secondGroup);
        Host thirdHost = new Host("thirdHost", "localhost", "", firstGroup);

        Mockito.when(hostRepoMock.findAll()).thenReturn(Arrays.asList(firstHost, secondHost));

        Assert.assertEquals(2, hostService.getAllHosts().size());
    }

    @Test
    public void shouldReturnListWithAllHostsOfUserAndHostGroup() {
        User user = new User("user", "user", true, "ROLE_USER");
        User admin = new User("admin", "admin", true, "ROLE_ADMIN");
        HGroup firstGroup = new HGroup("default", "");
        HGroup secondGroup = new HGroup("default", "");
        Host firstHost = new Host("firstHost", "localhost", "", firstGroup);
        Host secondHost = new Host("secondHost", "localhost", "", secondGroup);
        Host thirdHost = new Host("thirdHost", "localhost", "", firstGroup);

        Mockito.when(hostRepoMock.findAll()).thenReturn(Arrays.asList(firstHost));

        Assert.assertEquals(1, hostService.getAllHosts().size());
    }

    @Test
    public void shouldReturnValidHostById() {
        User user = new User("user", "user", true, "ROLE_USER");
        HGroup firstGroup = new HGroup("default", "");
        Host firstHost = new Host("firstHost", "localhost", "", firstGroup);

        Mockito.when(hostRepoMock.getOne(Mockito.anyLong())).thenReturn(firstHost);

        Assert.assertEquals(firstHost, hostService.getHostById(666L));
    }

    @Test
    public void shouldReturnValidHostByAddress() {
        User user = new User("user", "user", true, "ROLE_USER");
        HGroup firstGroup = new HGroup("default", "");
        Host firstHost = new Host("firstHost", "localhost", "", firstGroup);

        Mockito.when(hostRepoMock.findByAddress(Mockito.anyString())).thenReturn(firstHost);

        Assert.assertEquals(firstHost, hostService.getHostByAddress("localhost"));
    }
}
