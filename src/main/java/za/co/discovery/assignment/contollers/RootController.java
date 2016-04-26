package za.co.discovery.assignment.contollers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import za.co.discovery.assignment.models.Graph;
import za.co.discovery.assignment.models.Planet;
import za.co.discovery.assignment.services.FileReadingService;
import za.co.discovery.assignment.services.GraphService;
import za.co.discovery.assignment.services.PlanetService;

import java.util.UUID;

@Controller
public class RootController {
    PlanetService planetService;
    FileReadingService fileReadingService;
    GraphService graphService;
    Graph graph;

    @Autowired
    public RootController(PlanetService planetService, FileReadingService fileReadingService, GraphService graphService) {
        this.planetService = planetService;
        this.fileReadingService = fileReadingService;
        this.graphService = graphService;
        graph = graphService.createNewGraph();
        fileReadingService.readPlanetSheet();

    }

    @RequestMapping("/")
    public String index() {
        return "index";
    }

    @RequestMapping("/planetsList")
    public String getPlanetsList(Model model) {
        model.addAttribute("planets", planetService.getPlanets());
        return "planetsList";
    }

    @RequestMapping("/addPlanetPage")
    public String addPlanetGet(Model model) {
        String planetId = UUID.randomUUID().toString();
        Planet planet = new Planet();
        planet.setNode(planetId);
        model.addAttribute("planet", planet);
        return "createPlanet";
    }

    @RequestMapping(value = "addPlanetPageSubmit", method = RequestMethod.POST)
    public String addPlanetPageSubmit(@ModelAttribute(value = "planet") Planet planet, Model model) {
        planetService.persistPlanet(planet);
        model.addAttribute("planets", planetService.getPlanets());
        return "planetsList";
    }

    @RequestMapping("/editPlanetPage/{planetId}")
    public String editPlanetGet(@PathVariable String planetId, Model model) {
        Planet planet = planetService.retrievePlanet(planetId);
        model.addAttribute("planet", planet);
        return "editPlanet";
    }

    @RequestMapping(value = "editPlanetPageSubmit", method = RequestMethod.PUT)
    public String editPlanetPageSubmit(@ModelAttribute(value = "planet") Planet planet, Model model) {
        planetService.persistPlanet(planet);
        model.addAttribute("planets", planetService.getPlanets());
        return "planetsList";
    }

    @RequestMapping(value = "/deletePlanet/{planetId}", method = RequestMethod.DELETE)
    public String deletePlanet(@PathVariable String planetId, Model model) {
        planetService.deletePlanet(planetId);
        model.addAttribute("planets", planetService.getPlanets());
        return "planetsList";
    }

    @RequestMapping("/routesList")
    public String getRoutesList(Model model) {
        model.addAttribute("routes", planetService.getPlanets());
        return "routesList";
    }
    /*
    @RequestMapping("planet/{id}")
    public String showPlanet(@PathVariable Integer id, Model model){
        model.addAttribute("product", productService.getProductById(id));
        return "productshow";
    }*/


}
