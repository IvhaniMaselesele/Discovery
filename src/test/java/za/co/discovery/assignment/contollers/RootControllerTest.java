package za.co.discovery.assignment.contollers;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import za.co.discovery.assignment.dataAccess.PlanetDAO;
import za.co.discovery.assignment.dataAccess.RouteDAO;
import za.co.discovery.assignment.models.Graph;
import za.co.discovery.assignment.models.Planet;
import za.co.discovery.assignment.services.FileReadingService;
import za.co.discovery.assignment.services.GraphService;
import za.co.discovery.assignment.services.PlanetService;

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
/*
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
    }*/

    public void setupFixture() {
        //planetService = new PlanetService(planetDAO);
        graphService = mock(GraphService.class);
        graph = new Graph();
        planetService = new PlanetService(planetDAO);

        when(graphService.createNewGraph()).thenReturn(graph);

        //graph = graphService.createNewGraph();
        mockMvc = standaloneSetup(
                new RootController(planetService,
                        new FileReadingService(planetDAO, routeDAO, planetService), graphService))
                .setViewResolvers(getInternalResourceViewResolver())
                .build();
    }


    private InternalResourceViewResolver getInternalResourceViewResolver() {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setSuffix(".html");
        return viewResolver;
    }
}