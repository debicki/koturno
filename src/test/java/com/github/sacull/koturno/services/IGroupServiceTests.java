package com.github.sacull.koturno.services;

import com.github.sacull.koturno.entities.IGroup;
import com.github.sacull.koturno.repositories.IGroupRepo;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class IGroupServiceTests {

    @InjectMocks
    private IGroupService iGroupService;

    @Mock
    private IGroupRepo iGroupRepoMock;

    @Test
    public void shouldReturnGroupByName() {
        IGroup iGroup = new IGroup("default", "");

        Mockito.when(iGroupRepoMock.findByName(Mockito.anyString())).thenReturn(iGroup);

        Assert.assertEquals(iGroup, iGroupService.getGroup("default"));
    }
}
