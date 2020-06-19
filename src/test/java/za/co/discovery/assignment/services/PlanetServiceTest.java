package za.co.discovery.assignment.services;

import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import za.co.discovery.assignment.dataAccess.PlanetDAO;
import za.co.discovery.assignment.models.Planet;
import za.co.discovery.assignment.models.PlanetBuilder;

import java.util.List;

import static com.shazam.shazamcrest.MatcherAssert.assertThat;
import static com.shazam.shazamcrest.matcher.Matchers.sameBeanAs;
import static java.util.Collections.singletonList;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PlanetServiceTest {

    PlanetService planetService;
    @Mock
    private PlanetDAO planetDAO;

    @Before
    public void init() {
        planetService = new PlanetService(planetDAO);
    }

    @Test
    public void persistPlanetShouldSavePlanet_AndReturnResult() throws Exception {

        Planet expectedPlanet = new Planet("random node", "random name");

        when(planetDAO.save(expectedPlanet)).thenReturn(expectedPlanet);

        Planet actualPlanet = planetService.persistPlanet(expectedPlanet);
        assertThat(actualPlanet, CoreMatchers.is(sameBeanAs(expectedPlanet)));
    }

    @Test
    public void getPlanetById_ShouldReturnRetrievedPlanet() throws Exception {
        String node = "random node";
        Planet expectedPlanet = PlanetBuilder
                .aPlanet().withNode(node)
                .build();
        when(planetDAO.retrieve(node)).thenReturn(expectedPlanet);
        Planet actualPlanet = planetService.getPlanetById(node);

        assertThat(actualPlanet, sameBeanAs(expectedPlanet));
    }

    @Test
    public void getPlanets_ShouldRetrieveAllPlanets() throws Exception {

        List<Planet> expectedList = singletonList(PlanetBuilder.aPlanet().build());

        when(planetDAO.retrieveAll()).thenReturn(expectedList);

        List<Planet> actualList = planetService.getPlanets();
        assertThat(actualList, CoreMatchers.is(sameBeanAs(expectedList)));
    }

    @Test
    public void deletePlanet_ShouldRemovePlanet() throws Exception {
        //TODO : Think of other ways to test this, this test is useless
        Planet expectedPlanet = new Planet("random node", "random name");
        planetService.deletePlanet(expectedPlanet.getNode());
    }

}