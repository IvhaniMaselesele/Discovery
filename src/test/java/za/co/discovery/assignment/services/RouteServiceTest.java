package za.co.discovery.assignment.services;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import za.co.discovery.assignment.dataAccess.RouteDAO;
import za.co.discovery.assignment.models.*;

import java.util.List;

import static com.shazam.shazamcrest.matcher.Matchers.sameBeanAs;
import static java.util.Collections.singletonList;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RouteServiceTest {

    private RouteService routeService;
    @Mock
    private RouteDAO routeDAO;


    @Test
    public void getRoutesReturnsAllRoutes() throws Exception {
        setUpFixture();
        List<Route> expectedRoutes = singletonList(RouteBuilder.aRoute().build());
        when(routeDAO.retrieveAll()).thenReturn((expectedRoutes));
        List<Route> actualRoutes = routeService.getRoutes();
        assertThat(actualRoutes, sameBeanAs(expectedRoutes));
    }

    @Test
    public void persistRoute_ShouldReturnSavedRoute() throws Exception {
        setUpFixture();
        Route expectedRoute = RouteBuilder
                .aRoute()
                .build();
        when(routeDAO.save(expectedRoute)).thenReturn(expectedRoute);
        Route actualRoute = routeService.persistRoute(expectedRoute);

        assertThat(actualRoute, sameBeanAs(expectedRoute));
    }

    @Test
    public void retrieveRoute_ShouldReturnRoute() throws Exception {
        setUpFixture();
        int routeId = 7;
        Route expectedRoute = RouteBuilder
                .aRoute().withId(routeId)
                .build();
        when(routeDAO.retrieve(routeId)).thenReturn(expectedRoute);
        Route actualRoute = routeService.retrieveRoute(routeId);

        assertThat(actualRoute, sameBeanAs(expectedRoute));
    }


    @Test
    public void retrieveRouteAsEdge_ShouldReturnAnEdge() throws Exception {
        setUpFixture();
        int routeId = 5;
        Edge expectedEdge = EdgeBuilder
                .anEdge().withEdgeId(String.valueOf(routeId))
                .build();
        Route route = RouteBuilder
                .aRoute().withId(routeId)
                .build();

        when(routeDAO.retrieve(routeId)).thenReturn(route);
        Edge actualEdge = routeService.retrieveRouteAsEdge(routeId);
        assertThat(actualEdge, sameBeanAs(expectedEdge));
    }

    @Test
    public void createRoute_ShouldReturnNewRoute() throws Exception {
        setUpFixture();
        Planet origin = PlanetBuilder
                .aPlanet()
                .build();
        Planet destination = PlanetBuilder
                .aPlanet()
                .build();
        int i = 1;
        int distance = 1;
        double traffic = 1;
        Route expectedRoute = RouteBuilder
                .aRoute().withId(i)
                .withDistance(distance)
                .withDestination(destination)
                .withOrigin(origin)
                .build();

        Route actualRoute = routeService.createRoute(i, origin, destination, distance, traffic);

        assertThat(actualRoute, sameBeanAs(expectedRoute));

    }

    public void setUpFixture() {
        routeService = new RouteService(routeDAO);
    }
}