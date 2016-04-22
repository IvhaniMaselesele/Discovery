package za.co.discovery.assignment.contollers;

import org.junit.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import za.co.discovery.assignment.services.PlanetService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

public class RootControllerTest {
    MockMvc mockMvc;

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
        // TODO : test that model exists
    }


    public void setupFixture() {
        mockMvc = standaloneSetup(
                new RootController(new PlanetService()))
                .setViewResolvers(getInternalResourceViewResolver())
                .build();
    }


    private InternalResourceViewResolver getInternalResourceViewResolver() {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setSuffix(".html");
        return viewResolver;
    }
}