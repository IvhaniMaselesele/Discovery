package za.co.discovery.assignment.models;

import java.util.ArrayList;
import java.util.List;

public class Graph {
    private final List<Planet> planets;
    private final List<Route> routes;

    public Graph(List<Planet> planets, List<Route> routes) {
        this.planets = planets;
        this.routes = routes;
    }

    public Graph() {
        this.planets = new ArrayList<Planet>();
        this.routes = new ArrayList<Route>();
    }

    public List<Planet> getPlanets() {
        return planets;
    }

    public List<Route> getRoutes() {
        return routes;
    }

    public void addRoute(Route e) {
        routes.add(e);
    }

    public void addPlanet(Planet e) {
        planets.add(e);
    }

    public Planet getPlanetByNode(String node) {
        Planet p = null;
        for (Planet planet : planets) {
            if (node.equals(planet.getNode())) {
                p = planet;
                break;
            }
        }
        return p;
    }


}
