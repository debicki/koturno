package com.github.sacull.koturno.services;

import com.github.sacull.koturno.entities.Inaccessibility;
import com.github.sacull.koturno.repositories.InaccessibilityRepo;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;

@RunWith(SpringRunner.class)
public class InaccessibilityServiceTests {

    @InjectMocks
    private InaccessibilityService inaccessibilityService;
    
    @Mock
    private InaccessibilityRepo inaccessibilityRepoMock;
    
    @Test
    public void shouldReturnListWithThreeInstabilities() {
        Inaccessibility firstInaccessibility = new Inaccessibility(null, "firstInaccessibility", null);
        Inaccessibility secondInaccessibility = new Inaccessibility(null, "secondInaccessibility", null);
        Inaccessibility thirdInaccessibility = new Inaccessibility(null, "thirdInaccessibility", null);

        Mockito.when(inaccessibilityRepoMock.findAll())
                .thenReturn(Arrays.asList(firstInaccessibility, secondInaccessibility, thirdInaccessibility));

        Assert.assertEquals(3, inaccessibilityService.getAllInaccessibility().size());
        Assert.assertTrue(inaccessibilityService.getAllInaccessibility().contains(firstInaccessibility));
        Assert.assertTrue(inaccessibilityService.getAllInaccessibility().contains(secondInaccessibility));
        Assert.assertTrue(inaccessibilityService.getAllInaccessibility().contains(thirdInaccessibility));
    }

    // TODO: Add more test after expansion tested service in future
}
