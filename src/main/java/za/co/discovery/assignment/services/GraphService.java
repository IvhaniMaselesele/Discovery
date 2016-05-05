package za.co.discovery.assignment.services;

import org.springframework.stereotype.Service;
import za.co.discovery.assignment.models.Graph;
import za.co.discovery.assignment.models.Planet;
import za.co.discovery.assignment.models.Route;

@Service
public class GraphService {
    public Graph createNewGraph() {
        return new Graph();
    }

    public void addPlanet(Graph graph, Planet planet) {
        graph.addPlanet(planet);
    }

    public void addPlanet(Graph graph, String node, String name) {
        Planet vertex = new Planet(node, name);
        graph.addPlanet(vertex);
    }

    public void addRoute(Graph graph,
                         String id,
                         String originPlanetNode,
                         String destinationPlanetNode,
                         double weight) {

        Planet originPlanet = graph.getPlanetByNode(originPlanetNode);
        Planet destinationPlanet = graph.getPlanetByNode(destinationPlanetNode);
        Route route = new Route(Integer.parseInt(id),
                originPlanet,
                destinationPlanet,
                weight);
        graph.addRoute(route);

    }
}
