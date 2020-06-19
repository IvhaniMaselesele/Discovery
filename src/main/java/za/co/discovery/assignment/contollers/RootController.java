package za.co.discovery.assignment.contollers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import za.co.discovery.assignment.models.Edge;
import za.co.discovery.assignment.models.Graph;
import za.co.discovery.assignment.models.Planet;
import za.co.discovery.assignment.models.Route;
import za.co.discovery.assignment.services.*;

import java.util.LinkedList;
import java.util.UUID;

@Controller
public class RootController {
    PlanetService planetService;
    FileReadingService fileReadingService;
    GraphService graphService;
    Graph graph;
    private RouteService routeService;

    @Autowired
    public RootController(PlanetService planetService, FileReadingService fileReadingService, GraphService graphService, RouteService routeService) {
        this.routeService = routeService;
        this.planetService = planetService;
        this.fileReadingService = fileReadingService;
        this.graphService = graphService;
        graph = graphService.createNewGraph();
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
        model.addAttribute("routes", routeService.getRoutes());
        return "routesList";
    }

    @RequestMapping("/addRoutePage")
    public String addRouteGet(Model model) {
        String edgeId = (routeService.getNextAvailableKey()) + "";
        Edge edge = new Edge();
        edge.setEdgeId(edgeId);
        model.addAttribute("planets", planetService.getPlanets());
        model.addAttribute("newEdge", edge);
        return "createRoute";
    }

    @RequestMapping(value = "addRoutePageSubmit", method = RequestMethod.POST)
    public String addRoutePageSubmit(@ModelAttribute(value = "newEdge") Edge edge, Model model) {
        Planet originPlanet = planetService.getPlanetById(edge.getOriginId());
        Planet destinationPlanet = planetService.getPlanetById(edge.getDestinationId());
        Route route = routeService.createRoute(Integer.parseInt(edge.getEdgeId()), originPlanet, destinationPlanet, edge.getWeight(), edge.getTraffic());
        routeService.persistRoute(route);
        model.addAttribute("routes", routeService.getRoutes());
        return "routesList";
    }

    @RequestMapping("/editRoutePage/{edge}")
    public String editRoutePage(@PathVariable String edge, Model model) {
        Edge edgeById = routeService.retrieveRouteAsEdge(Integer.parseInt(edge));
        model.addAttribute("edge", edgeById);
        model.addAttribute("planets", planetService.getPlanets());
        return "editRoute";
    }

    @RequestMapping(value = "editRoutePageSubmit", method = RequestMethod.PUT)
    public String editRoutePageSubmit(@ModelAttribute(value = "edge") Edge edge, Model model) {
        Planet originPlanet = planetService.getPlanetById(edge.getOriginId());
        Planet destinationPlanet = planetService.getPlanetById(edge.getDestinationId());
        Route route = routeService.retrieveRoute(Integer.parseInt(edge.getEdgeId()));
        route.setOrigin(originPlanet);
        route.setDestination(destinationPlanet);
        route.setTraffic(edge.getTraffic());
        route.setDistance(edge.getWeight());

        routeService.persistRoute(route);
        model.addAttribute("routes", routeService.getRoutes());
        model.addAttribute("planets", planetService.getPlanets());
        return "routesList";
    }

    @RequestMapping(value = "/deleteRoute/{routeId}", method = RequestMethod.DELETE)
    public String deleteRoute(@PathVariable String routeId, Model model) {
        routeService.deleteRoute(routeId);
        model.addAttribute("routes", routeService.getRoutes());
        return "routesList";
    }

    @RequestMapping(
            value = "shortestPath",
            method = RequestMethod.GET)
    public String getShortestPath(Model model) {
        graph = graphService.createNewGraph(planetService.getPlanets(), routeService.getRoutes());
        model.addAttribute("mapList", planetService.getPlanets());
        return "shortestPath";
    }

    @RequestMapping(value = "shortestPath/{planet}", method = RequestMethod.GET)
    @ResponseBody
    public LinkedList<String> shortestPath(@PathVariable String planet) {
        graph = graphService.createNewGraph(planetService.getPlanets(), routeService.getRoutes());
        String node = planet.split(",")[0];
        String withTraffic = planet.split(",")[1];

        DijkstraAlgorithm dijkstra = new DijkstraAlgorithm(graph);
        if (withTraffic.equals("true")) {
            dijkstra.setWithTraffic(true);
        }
        dijkstra.execute(graph.getPlanets().get(0));
        LinkedList<String> path = dijkstra.getPath(graph.getPlanetByNode(node));
        if (path == null) {
            path = new LinkedList<>();
            path.add("No Path From : " + graph.getPlanets().get(0).getName() + " to : " +
                    graph.getPlanetByNode(node).getName());
        }
        return path;
    }

}
