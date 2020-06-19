package za.co.discovery.assignment.services;

import org.junit.Test;
import za.co.discovery.assignment.models.*;

import java.util.*;

import static com.shazam.shazamcrest.matcher.Matchers.sameBeanAs;
import static java.util.Collections.singletonList;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class DijkstraAlgorithmTest {
    DijkstraAlgorithm dijkstraAlgorithm;

    @Test
    public void getShortestDistance_WithoutExistingShortestDistanceReturnsMaxNumber() throws Exception {
        setUpFixture();
        double expectedShortestDistance = Integer.MAX_VALUE;
        double actualShortestDistance = dijkstraAlgorithm.getShortestDistance(new Planet());
        assertThat(actualShortestDistance, sameBeanAs(expectedShortestDistance));
    }

    @Test
    public void getMinimumReturnsPlanetWithShortestDistanceFromSource() throws Exception {
        setUpFixture();
        Double distance0 = (double) 20;
        Double distance1 = (double) 2;

        Planet planet0 = PlanetBuilder
                .aPlanet()
                .build();
        Planet expectedPlanet = PlanetBuilder
                .aPlanet().withName("A")
                .build();

        Set<Planet> planets = new HashSet<>();
        planets.add(planet0);
        planets.add(expectedPlanet);

        dijkstraAlgorithm.setDistance(planet0, distance0);
        dijkstraAlgorithm.setDistance(expectedPlanet, distance1);
        Planet actualPlanet = dijkstraAlgorithm.getMinimum(planets);

        assertThat(actualPlanet, sameBeanAs(expectedPlanet));
    }

    @Test
    public void getShortestDistance_WithExistingShortestDistanceReturnsMaxNumber() throws Exception {
        setUpFixture();
        Double expectedShortestDistance = (double) 2;
        Planet planet = PlanetBuilder.aPlanet().build();
        dijkstraAlgorithm.setDistance(planet, expectedShortestDistance);

        double actualShortestDistance = dijkstraAlgorithm.getShortestDistance(planet);
        assertThat(actualShortestDistance, sameBeanAs(expectedShortestDistance));
    }

    @Test
    public void getDistanceWithoutTrafficReturnsDistance() throws Exception {
        double expectedDistance = 2;
        Planet sourcePlanet = PlanetBuilder.aPlanet().build();
        Planet destinationPlanet = PlanetBuilder.aPlanet().build();
        Route route = RouteBuilder.aRoute()
                .withOrigin(sourcePlanet)
                .withDestination(destinationPlanet)
                .withDistance(expectedDistance)
                .build();
        List<Planet> nodes;
        nodes = new ArrayList<>();
        nodes.add(sourcePlanet);
        nodes.add(destinationPlanet);
        List<Route> routes = singletonList(route);

        setUpFixture(nodes, routes);
        dijkstraAlgorithm.initialize();

        double actualDistance = dijkstraAlgorithm.getDistance(sourcePlanet, destinationPlanet);
        assertThat(actualDistance, is(expectedDistance));
    }

    @Test
    public void getDistanceWithTrafficReturnsSumOfTrafficAndDistance() throws Exception {
        double expectedDistance = 7;
        double distance = 2;
        double traffic = 5;
        Planet sourcePlanet = PlanetBuilder.aPlanet().build();
        Planet destinationPlanet = PlanetBuilder.aPlanet().build();
        Route route = RouteBuilder.aRoute()
                .withOrigin(sourcePlanet)
                .withDestination(destinationPlanet)
                .withDistance(distance)
                .withTraffic(traffic)
                .build();
        List<Planet> nodes;
        nodes = new ArrayList<>();
        nodes.add(sourcePlanet);
        nodes.add(destinationPlanet);
        List<Route> routes = singletonList(route);

        setUpFixture(nodes, routes);
        dijkstraAlgorithm.initialize();
        dijkstraAlgorithm.setWithTraffic(true);

        double actualDistance = dijkstraAlgorithm.getDistance(sourcePlanet, destinationPlanet);
        assertThat(actualDistance, is(expectedDistance));
    }

    @Test
    public void getPlanetRoutes_ShouldReturnAllNeighbouringRoutes() throws Exception {
        Planet sourcePlanet = PlanetBuilder.aPlanet().build();
        Planet destinationPlanet = PlanetBuilder.aPlanet().build();
        Route route = RouteBuilder.aRoute()
                .withOrigin(sourcePlanet)
                .withDestination(destinationPlanet)
                .build();
        List<Planet> nodes;
        nodes = new ArrayList<>();
        nodes.add(sourcePlanet);
        nodes.add(destinationPlanet);
        List<Route> routes = singletonList(route);

        setUpFixture(nodes, routes);

        List<Route> expectedRoutes = routes;
        List<Route> actualRoutes = dijkstraAlgorithm.getPlanetRoutes(sourcePlanet);

        assertThat(actualRoutes, sameBeanAs(expectedRoutes));
    }

    @Test
    public void getNeighborsReturnsAllPlanetsWithRouteComingFromPlanet() {
        Planet sourcePlanet = PlanetBuilder.aPlanet().build();
        Planet destinationPlanet = PlanetBuilder.aPlanet().build();
        Route route = RouteBuilder.aRoute()
                .withOrigin(sourcePlanet)
                .withDestination(destinationPlanet)
                .build();
        List<Planet> nodes;
        nodes = new ArrayList<>();
        nodes.add(sourcePlanet);
        nodes.add(destinationPlanet);
        List<Route> routes = singletonList(route);

        setUpFixture(nodes, routes);

        List<Planet> expectedNeighboringPlanets = singletonList(destinationPlanet);

//        when(route.getOrigin())
        dijkstraAlgorithm.initialize();
        List<Planet> actualNeighboringPlanets = dijkstraAlgorithm.getNeighbors(sourcePlanet);

        assertThat(actualNeighboringPlanets, sameBeanAs(expectedNeighboringPlanets));
    }

    private void setUpFixture(List<Planet> nodes, List<Route> routes) {
        Graph graph = new Graph(nodes, routes);
        dijkstraAlgorithm = new DijkstraAlgorithm(graph);
    }

    @Test
    public void getPathReturnsPredecessorReversed() throws Exception {
        Map<Planet, Planet> predecessors = new HashMap<>();
        Planet planet0 = PlanetBuilder
                .aPlanet().withNode("A").withName("A")
                .build();
        Planet planet1 = PlanetBuilder
                .aPlanet().withNode("B").withName("B")
                .build();
        Planet planet2 = PlanetBuilder
                .aPlanet().withNode("C").withName("C")
                .build();
        predecessors.put(planet0, planet1);
        predecessors.put(planet1, planet2);

        setUpFixture();
        dijkstraAlgorithm.initialize();
        dijkstraAlgorithm.setPredecessors(predecessors);
        LinkedList<String> expectedPath = new LinkedList<>();
        expectedPath.add("C");
        expectedPath.add("B");
        expectedPath.add("A");
        LinkedList<String> actualPath = dijkstraAlgorithm.getPath(planet0);

        assertThat(actualPath, sameBeanAs(expectedPath));
    }

    @Test
    public void getPathWithNoPredecessorReturnsNull() throws Exception {
        Planet planet0 = PlanetBuilder
                .aPlanet().withNode("A").withName("A")
                .build();
        setUpFixture();
        dijkstraAlgorithm.initialize();

        LinkedList<String> actualPath = dijkstraAlgorithm.getPath(planet0);

        assertThat(actualPath, sameBeanAs(null));
    }

    private void setUpFixture() {
        Planet sourcePlanet = PlanetBuilder.aPlanet().build();
        Planet destinationPlanet = PlanetBuilder.aPlanet().build();
        Route route = RouteBuilder.aRoute()
                .withOrigin(sourcePlanet)
                .withDestination(destinationPlanet)
                .build();
        List<Planet> nodes;
        nodes = new ArrayList<>();
        nodes.add(sourcePlanet);
        nodes.add(destinationPlanet);
        List<Route> routes = singletonList(route);

        Graph graph = new Graph(nodes, routes);
        dijkstraAlgorithm = new DijkstraAlgorithm(graph);
        dijkstraAlgorithm.initialize();
    }


}