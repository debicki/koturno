package com.github.sacull.koturno.services;

import com.github.sacull.koturno.entities.HGroup;
import com.github.sacull.koturno.entities.Host;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class FileServiceTests {

    @InjectMocks
    private FileService fileService;

    @Mock
    private HostService hostServiceMock;

    @Mock
    private HGroupService hGroupServiceMock;

    @Test
    public void shouldCreateHostFromCsvLineWhenGroupDoesNotExist() {
        String address = "8.8.8.8";
        String name = "Google DNS";
        String description = "Primary Google DNS";
        String group = "DNS";
        String[] testInput = {address, name, description, group};
        HGroup hGroup = new HGroup(group, "");

        Mockito.when(hGroupServiceMock.getGroupByName(Mockito.anyString())).thenReturn(null);
        Mockito.when(hGroupServiceMock.save(Mockito.any(HGroup.class))).thenReturn(hGroup);

        Host hostToTest = fileService.parse(testInput);
        Assert.assertEquals(hostToTest.getAddress(), address);
        Assert.assertEquals(hostToTest.getName(), name);
        Assert.assertEquals(hostToTest.getDescription(), description);
        Assert.assertEquals(hostToTest.getHostGroup().getName(), group);
    }

    @Test
    public void shouldCreateHostFromCsvLineWhenGroupExist() {
        String address = "8.8.8.8";
        String name = "Google DNS";
        String description = "Primary Google DNS";
        String group = "DNS";
        String[] testInput = {address, name, description, group};
        HGroup hGroup = new HGroup(group, "");

        Mockito.when(hGroupServiceMock.getGroupByName(Mockito.anyString())).thenReturn(hGroup);

        Host hostToTest = fileService.parse(testInput);
        Assert.assertEquals(hostToTest.getAddress(), address);
        Assert.assertEquals(hostToTest.getName(), name);
        Assert.assertEquals(hostToTest.getDescription(), description);
        Assert.assertEquals(hostToTest.getHostGroup().getName(), group);
    }

    @Test
    public void shouldCreateHostFromCsvLineWhenMissingGroupField() {
        String address = "8.8.8.8";
        String name = "Google DNS";
        String description = "Primary Google DNS";
        String[] testInput = {address, name, description};

        Host hostToTest = fileService.parse(testInput);
        Assert.assertEquals(hostToTest.getAddress(), address);
        Assert.assertEquals(hostToTest.getName(), name);
        Assert.assertEquals(hostToTest.getDescription(), description);
        Assert.assertNull(hostToTest.getHostGroup());
    }

    @Test
    public void shouldCreateHostFromCsvLineWhenMissingDescriptionAndGroupFields() {
        String address = "8.8.8.8";
        String name = "Google DNS";
        String[] testInput = {address, name};

        Host hostToTest = fileService.parse(testInput);
        Assert.assertEquals(hostToTest.getAddress(), address);
        Assert.assertEquals(hostToTest.getName(), name);
        Assert.assertEquals(hostToTest.getDescription(), "");
        Assert.assertNull(hostToTest.getHostGroup());
    }

    @Test
    public void shouldCreateHostFromCsvLineWhenMissingNameAndDescriptionAndGroupFields() {
        String address = "8.8.8.8";
        String[] testInput = {address};

        Host hostToTest = fileService.parse(testInput);
        Assert.assertEquals(hostToTest.getAddress(), address);
        Assert.assertEquals(hostToTest.getName(), "");
        Assert.assertEquals(hostToTest.getDescription(), "");
        Assert.assertNull(hostToTest.getHostGroup());
    }

    @Test
    public void shouldCreateHostFromCsvLineWhenMissingAllFields() {
        String[] testInput = {};

        Host hostToTest = fileService.parse(testInput);
        Assert.assertEquals(hostToTest.getAddress(), "");
        Assert.assertEquals(hostToTest.getName(), "");
        Assert.assertEquals(hostToTest.getDescription(), "");
        Assert.assertNull(hostToTest.getHostGroup());
    }
}
