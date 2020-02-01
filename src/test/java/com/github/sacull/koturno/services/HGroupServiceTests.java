package com.github.sacull.koturno.services;

import com.github.sacull.koturno.entities.HGroup;
import com.github.sacull.koturno.repositories.HGroupRepo;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;

@RunWith(SpringRunner.class)
public class HGroupServiceTests {

    @InjectMocks
    private HGroupService hGroupService;

    @Mock
    private HGroupRepo hGroupRepoMock;

    @Test
    public void shouldReturnGroupByName() {
        Long id = 666L;
        String name = "Test";
        String description = "Test group";
        HGroup hGroup = new HGroup(name, description);
        ReflectionTestUtils.setField(hGroup, "id", id);

        Mockito.when(hGroupRepoMock.findByName(Mockito.eq(name))).thenReturn(hGroup);

        Assert.assertEquals(hGroup, hGroupService.getGroupByName(name));
    }

    @Test
    public void shouldReturnGroupById() {
        Long id = 666L;
        String name = "Test";
        String description = "Test group";
        HGroup hGroup = new HGroup(name, description);
        ReflectionTestUtils.setField(hGroup, "id", id);

        Mockito.when(hGroupRepoMock.getOne(Mockito.eq(id))).thenReturn(hGroup);

        Assert.assertEquals(hGroup, hGroupService.getGroupById(id));
    }

    @Test
    public void shouldReturnListWithThreeGroups() {
        HGroup firstGroup = new HGroup("default", "");
        HGroup secondGroup = new HGroup("test", "");
        HGroup thirdGroup = new HGroup("group", "");

        Mockito.when(hGroupRepoMock.findAll()).thenReturn(Arrays.asList(firstGroup, secondGroup, thirdGroup));

        Assert.assertEquals(3, hGroupService.getAllGroups().size());
        Assert.assertTrue(hGroupService.getAllGroups().contains(firstGroup));
        Assert.assertTrue(hGroupService.getAllGroups().contains(secondGroup));
        Assert.assertTrue(hGroupService.getAllGroups().contains(thirdGroup));
    }
}
