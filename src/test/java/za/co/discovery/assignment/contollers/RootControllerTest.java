package za.co.discovery.assignment.contollers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import za.co.discovery.assignment.dataAccess.PlanetDAO;
import za.co.discovery.assignment.dataAccess.RouteDAO;
import za.co.discovery.assignment.models.*;
import za.co.discovery.assignment.services.FileReadingService;
import za.co.discovery.assignment.services.GraphService;
import za.co.discovery.assignment.services.PlanetService;
import za.co.discovery.assignment.services.RouteService;

import java.io.InputStream;
import java.util.LinkedList;

import static com.shazam.shazamcrest.MatcherAssert.assertThat;
import static com.shazam.shazamcrest.matcher.Matchers.sameBeanAs;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@RunWith(MockitoJUnitRunner.class)
public class RootControllerTest {
    MockMvc mockMvc;
    @Mock
    PlanetDAO planetDAO;
    @Mock
    RouteDAO routeDAO;
    @Mock
    PlanetService planetService;
    RouteService routeService;
    GraphService graphService;
    Graph graph;

    @Test
    public void rootReturnsIndexPage() throws Exception {
        setupFixture();
        mockMvc.perform(get("/"))
                .andExpect(view().name("index"));
    }

    @Test
    public void planetsList_ReturnsPlanetsListPage() throws Exception {
        setupFixture();
        mockMvc.perform(get("/planetsList"))
                .andExpect(model().attributeExists("planets"))
                .andExpect(view().name("planetsList"));
    }

    @Test
    public void addPlanetGet_ShouldReturnCreateNewPlanetPage() throws Exception {
        setupFixture();

        mockMvc.perform(get("/addPlanetPage"))
                .andExpect(status().isOk())
                .andExpect(view().name("createPlanet"))
                .andExpect(model().attributeExists("planet"));
    }

    @Test
    public void addPlanetPostPage_ShouldReturnPlanetsListPage() throws Exception {
        setupFixture();
        String randomPlanetNode = "random planet node";
        String randomPlanetName = "random planet name";

        mockMvc.perform(post("/addPlanetPageSubmit")
                .param("name", randomPlanetName)
                .param("node", randomPlanetNode))
                .andExpect(view().name("planetsList"))
                .andExpect(model().attributeExists("planets"));
    }

    @Test
    public void editPlanetGet_ShouldReturnEditPlanetPage() throws Exception {
        setupFixture();
        String randomPlanetId = "random planetId";

        String randomPlanetName = "randomPlanetName";
        Planet planet = new Planet(randomPlanetId, randomPlanetName);
        when(planetService.retrievePlanet(randomPlanetId)).thenReturn(planet);
        mockMvc.perform(get("/editPlanetPage/" + randomPlanetId))
                .andExpect(status().isOk())
                .andExpect(view().name("editPlanet"))
                .andExpect(model().attributeExists("planet"));
    }

    @Test
    public void editPlanetPost_ShouldUpdatePlanet() throws Exception {
        setupFixture();
        String randomPlanetId = "random planet id";
        String newRandomPlanetName = "new random planet name";

        mockMvc.perform(put("/editPlanetPageSubmit").param("id", randomPlanetId).param("name", newRandomPlanetName))
                .andExpect(view().name("planetsList"))
                .andExpect(model().attributeExists("planets"));
    }

    @Test
    public void deletePlanet_ShouldReturnPlanetsListPage() throws Exception {
        setupFixture();
        String randomPlanetId = "random planet id";

        mockMvc.perform(delete("/deletePlanet/" + randomPlanetId))
                .andExpect(view().name("planetsList"))
                .andExpect(model().attributeExists("planets"));
    }

    @Test
    public void routeList_ReturnsRoutesListPage() throws Exception {
        setupFixture();
        mockMvc.perform(get("/routesList"))
                .andExpect(model().attributeExists("routes"))
                .andExpect(view().name("routesList"));
    }

    @Test
    public void addRoute_ShouldReturnCreateNewRoutePage() throws Exception {
        setupFixture();

        mockMvc.perform(get("/addRoutePage"))
                .andExpect(status().isOk())
                .andExpect(view().name("createRoute"))
                .andExpect(model().attributeExists("newEdge"));
    }

    @Test
    public void addRoutePostPage_ShouldReturnRoutesListPage() throws Exception {
        setupFixture();
        int randomRouteId = 0;

        when(planetService.getPlanetById(randomRouteId + "")).thenReturn(PlanetBuilder.aPlanet().build());

        mockMvc.perform(post("/addRoutePageSubmit")
                .param("edgeId", randomRouteId + ""))
                .andExpect(view().name("routesList"))
                .andExpect(model().attributeExists("routes"));
    }

    @Test
    public void editRouteGet_ShouldReturnEditRoutePage() throws Exception {
        setupFixture();
        int randomRouteId = 0;
        Route route = RouteBuilder.aRoute().build();

        when(routeService.retrieveRoute(randomRouteId)).thenReturn(route);
        mockMvc.perform(get("/editRoutePage/" + randomRouteId))
                .andExpect(status().isOk())
                .andExpect(view().name("editRoute"))
                .andExpect(model().attributeExists("edge"));
    }

    @Test
    public void shortestPath_ShouldReturnShortestPathPage() throws Exception {
        setupFixture();
        mockMvc.perform(get("/shortestPath/"))
                .andExpect(status().isOk())
                .andExpect(view().name("shortestPath"))
                .andExpect(model().attributeExists("mapList"));
    }

    @Test
    public void editRoutePost_ShouldRoute() throws Exception {
        setupFixture();
        String randomSourceId = "A";
        String randomDestinationId = "B";
        String newRandomDestinationId = "C";
        //TODO : use a builder

        Edge edge = new Edge();
        edge.setDestinationId(randomDestinationId);
        edge.setOriginId(randomSourceId);
        edge.setWeight(2);
        edge.setEdgeId("1");

        when(routeService.retrieveRoute(1)).thenReturn(RouteBuilder.aRoute().build());

        mockMvc.perform(put("/editRoutePageSubmit")
                .param("edgeId", edge.getEdgeId())
                .param("sourceId", randomSourceId)
                .param("destinationId", newRandomDestinationId)
                .param("weight", "2"))
                .andExpect(view().name("routesList"))
                .andExpect(model().attributeExists("routes"));
    }

    @Test
    public void deleteRoute_ShouldReturnRouteListPage() throws Exception {
        setupFixture();
        int randomRouteId = 5;
        mockMvc.perform(delete("/deleteRoute/" + String.valueOf(randomRouteId)))
                .andExpect(view().name("routesList"))
                .andExpect(model().attributeExists("routes"));
    }

    @Test
    public void test() throws Exception {
        setupFixture();
        Planet planet1 = aPlanet("A", "Anna");
        Planet planet2 = aPlanet("B", "BOb");
        graph.addPlanet(planet1);
        graph.addPlanet(planet2);
        Route route1 = aRoute(1, planet1, planet2, 100, 10);
        graph.addRoute(route1);
        LinkedList<String> expectedPath = new LinkedList<>();
        expectedPath.add("Anna");
        expectedPath.add("BOb");

        ResultActions result = mockMvc.perform(get("/shortestPath/" + planet2.getNode() + ",true")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith("application/json"));

        String json = result.andReturn().getResponse().getContentAsString();
        ObjectMapper mapper = new ObjectMapper();
        LinkedList<String> actualPath = mapper.readValue(json, new TypeReference<LinkedList<String>>() {
        });
        assertThat(actualPath, sameBeanAs(expectedPath));
    }

    @Test
    public void shortestPathWithNonExistingPathReturnsPredefinedString() throws Exception {
        setupFixture();
        Planet planet1 = aPlanet("A", "Anna");
        Planet planet2 = aPlanet("B", "BOb");

        graph.addPlanet(planet1);
        graph.addPlanet(planet2);
        LinkedList<String> expectedPath = new LinkedList<>();
        expectedPath.add("No Path From : " + planet1.getName() + " to : " + planet2.getName());

        ResultActions result = mockMvc.perform(get("/shortestPath/" + planet2.getNode() + ",true")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith("application/json"));

        String json = result.andReturn().getResponse().getContentAsString();
        ObjectMapper mapper = new ObjectMapper();
        LinkedList<String> actualPath = mapper.readValue(json, new TypeReference<LinkedList<String>>() {
        });
        assertThat(actualPath, sameBeanAs(expectedPath));
    }

    @Test
    public void shortestPathWithTraffic_ShouldReturnCorrectPath() throws Exception {
        setupFixture();
        Planet planet1 = aPlanet("A", "Anna");
        Planet planet2 = aPlanet("B", "BOb");
        Planet planet3 = aPlanet("C", "Carl");
        graph.addPlanet(planet1);
        graph.addPlanet(planet2);
        graph.addPlanet(planet3);
        Route route1 = aRoute(1, planet1, planet2, 100, 10);
        Route route2 = aRoute(2, planet1, planet3, 1, 6);
        Route route3 = aRoute(3, planet3, planet2, 1, 6);
        graph.addRoute(route1);
        graph.addRoute(route2);
        graph.addRoute(route3);


        LinkedList<String> expectedPath = new LinkedList<>();
        expectedPath.add("Anna");
        expectedPath.add("Carl");
        expectedPath.add("BOb");

        ResultActions result = mockMvc.perform(get("/shortestPath/" + planet2.getNode() + ",true")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith("application/json"));

        String json = result.andReturn().getResponse().getContentAsString();
        ObjectMapper mapper = new ObjectMapper();
        LinkedList<String> actualPath = mapper.readValue(json, new TypeReference<LinkedList<String>>() {
        });
        assertThat(actualPath, sameBeanAs(expectedPath));
    }

    private Planet aPlanet(String node, String name) {
        return PlanetBuilder.aPlanet()
                .withNode(node)
                .withName(name)
                .build();
    }

    private Route aRoute(int id, Planet planet1, Planet planet2, double traffic, double distance) {
        return RouteBuilder.aRoute()
                .withId(id)
                .withOrigin(planet1)
                .withDestination(planet2)
                .withDistance(distance)
                .withTraffic(traffic)
                .build();
    }

    public void setupFixture() {
        InputStream inputStream = mock(InputStream.class);
        graphService = mock(GraphService.class);
        graph = new Graph();
        planetService = new PlanetService(planetDAO);
        routeService = new RouteService(routeDAO);

        when(graphService.createNewGraph()).thenReturn(graph);

        mockMvc = standaloneSetup(
                new RootController(planetService,
                        new FileReadingService(planetDAO, routeDAO, planetService, routeService, inputStream), graphService, routeService))
                .setViewResolvers(getInternalResourceViewResolver())
                .build();
    }


    private InternalResourceViewResolver getInternalResourceViewResolver() {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setSuffix(".html");
        return viewResolver;
    }
}