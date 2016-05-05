package za.co.discovery.assignment.services;

import org.junit.Test;
import za.co.discovery.assignment.models.Graph;
import za.co.discovery.assignment.models.Planet;

import static com.shazam.shazamcrest.matcher.Matchers.sameBeanAs;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class GraphServiceTest {
    GraphService graphService;
    Graph graph;

    @Test
    public void createNewGraph_ShouldReturnNewInstanceOfGraph() throws Exception {
        setUpFixture();
        Graph expectedGraph = new Graph();
        Graph actualGraph = graphService.createNewGraph();
        assertThat(actualGraph, sameBeanAs(expectedGraph));
    }

    @Test
    public void addPlanetWithNodeAndName_ShouldUpdateListOfPlanets() throws Exception {
        setUpFixture();
        String name = "random name";
        String node = "random node";
        assertThat(graph.getPlanets().size(), is(0));
        graphService.addPlanet(graph, node, name);
        assertThat(graph.getPlanets().size(), is(1));
    }

    @Test
    public void addPlanetWithPlanet_ShouldUpdateListOfPlanets() throws Exception {
        setUpFixture();
        String name = "random name";
        String node = "random node";
        Planet planet = new Planet(node, name);
        assertThat(graph.getPlanets().size(), is(0));
        graphService.addPlanet(graph, planet);
        assertThat(graph.getPlanets().size(), is(1));
    }

    @Test
    public void addRoute_ShouldUpdateListOfRoutes() throws Exception {
        setUpFixture();

        String id = "1";
        int weight = 2;
        String randomOriginNode = "randomOriginNode";
        Planet randomOrigin = new Planet(randomOriginNode, "");
        String randomDestinationNode = "randomDestinationNode";
        Planet randomDestination = new Planet(randomDestinationNode, "");
        graph.addPlanet(randomOrigin);
        graph.addPlanet(randomDestination);

        assertThat(graph.getRoutes().size(), is(0));
        graphService.addRoute(graph, id, randomOriginNode, randomDestinationNode, weight);
        assertThat(graph.getRoutes().size(), is(1));
    }

    public void setUpFixture() {
        graphService = new GraphService();
        graph = new Graph();
    }
}