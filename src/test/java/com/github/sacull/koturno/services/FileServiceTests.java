package com.github.sacull.koturno.services;

import com.github.sacull.koturno.entities.HGroup;
import com.github.sacull.koturno.entities.Host;
import org.junit.Assert;
import org.junit.Ignore;
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
        Assert.assertEquals(address, hostToTest.getAddress());
        Assert.assertEquals(name, hostToTest.getName());
        Assert.assertEquals(description, hostToTest.getDescription());
        Assert.assertEquals(group, hostToTest.getHostGroup().getName());
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
        Assert.assertEquals(address, hostToTest.getAddress());
        Assert.assertEquals(name, hostToTest.getName());
        Assert.assertEquals(description, hostToTest.getDescription());
        Assert.assertEquals(group, hostToTest.getHostGroup().getName());
    }

    @Test
    public void shouldCreateHostFromCsvLineWhenMissingGroupField() {
        String address = "8.8.8.8";
        String name = "Google DNS";
        String description = "Primary Google DNS";
        String[] testInput = {address, name, description};

        Host hostToTest = fileService.parse(testInput);
        Assert.assertEquals(address, hostToTest.getAddress());
        Assert.assertEquals(name, hostToTest.getName());
        Assert.assertEquals(description, hostToTest.getDescription());
        Assert.assertNull(hostToTest.getHostGroup());
    }

    @Test
    public void shouldCreateHostFromCsvLineWhenMissingDescriptionAndGroupFields() {
        String address = "8.8.8.8";
        String name = "Google DNS";
        String[] testInput = {address, name};

        Host hostToTest = fileService.parse(testInput);
        Assert.assertEquals(address, hostToTest.getAddress());
        Assert.assertEquals(name, hostToTest.getName());
        Assert.assertEquals("", hostToTest.getDescription());
        Assert.assertNull(hostToTest.getHostGroup());
    }

    @Test
    public void shouldCreateHostFromCsvLineWhenMissingNameAndDescriptionAndGroupFields() {
        String address = "8.8.8.8";
        String[] testInput = {address};

        Host hostToTest = fileService.parse(testInput);
        Assert.assertEquals(address, hostToTest.getAddress());
        Assert.assertEquals("", hostToTest.getName());
        Assert.assertEquals("", hostToTest.getDescription());
        Assert.assertNull(hostToTest.getHostGroup());
    }

    @Test
    public void shouldCreateHostFromCsvLineWhenMissingAllFields() {
        String[] testInput = {};

        Host hostToTest = fileService.parse(testInput);
        Assert.assertEquals("", hostToTest.getAddress());
        Assert.assertEquals("", hostToTest.getName());
        Assert.assertEquals("", hostToTest.getDescription());
        Assert.assertNull(hostToTest.getHostGroup());
    }

    @Test
    public void shouldCreateHostFromTxtLine() {
        String address = "8.8.8.8";
        String name = "Google DNS";
        String description = "Primary Google DNS";
        String testInput = address + " " + name + "*" + description;

        Host hostToTest = fileService.parse(testInput);
        Assert.assertEquals(address, hostToTest.getAddress());
        Assert.assertEquals(name, hostToTest.getName());
        Assert.assertEquals(description, hostToTest.getDescription());
        Assert.assertNull(hostToTest.getHostGroup());
    }

    @Test
    public void shouldCreateHostFromTxtLineWhenDescriptionIsWithoutName() {
        String address = "8.8.8.8";
        String description = "Primary Google DNS";
        String testInput = address + " " + description;

        Host hostToTest = fileService.parse(testInput);
        Assert.assertEquals(address, hostToTest.getAddress());
        Assert.assertEquals("", hostToTest.getName());
        Assert.assertEquals(description, hostToTest.getDescription());
        Assert.assertNull(hostToTest.getHostGroup());
    }

    @Test
    public void shouldCreateHostFromTxtLineWhenDescriptionIsEmpty() {
        String address = "8.8.8.8";
        String testInput = address;

        Host hostToTest = fileService.parse(testInput);
        Assert.assertEquals(address, hostToTest.getAddress());
        Assert.assertEquals("", hostToTest.getName());
        Assert.assertEquals("", hostToTest.getDescription());
        Assert.assertNull(hostToTest.getHostGroup());
    }

    @Test
    public void shouldCreateHostFromTxtLineWhenIsEmpty() {
        String testInput = " ";

        Host hostToTest = fileService.parse(testInput);
        Assert.assertEquals("", hostToTest.getAddress());
        Assert.assertEquals("", hostToTest.getName());
        Assert.assertEquals("", hostToTest.getDescription());
        Assert.assertNull(hostToTest.getHostGroup());
    }

    @Test
    public void shouldReturnTrueWhenValidateAddress() {
        String address = "8.8.8.8";

        Assert.assertTrue(fileService.isValidAddress(address));
    }

    @Ignore
    @Test
    public void shouldReturnFalseWhenValidateAddress() {
        String address = "address.do.not.exist";

        Assert.assertTrue(fileService.isValidAddress(address));
    }

    // TODO: Add in future tests for hostsImport() method
}
