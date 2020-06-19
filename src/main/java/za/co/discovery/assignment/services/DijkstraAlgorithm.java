package za.co.discovery.assignment.services;

import za.co.discovery.assignment.models.Graph;
import za.co.discovery.assignment.models.Planet;
import za.co.discovery.assignment.models.Route;

import java.util.*;
import java.util.stream.Collectors;

public class DijkstraAlgorithm {

    private final List<Planet> nodes;
    private final List<Route> routes;
    private Set<Planet> settledNodes;
    private Set<Planet> unSettledNodes;
    private Map<Planet, Planet> predecessors;

    private Map<Planet, Double> distance;

    public boolean withTraffic;

    public DijkstraAlgorithm(Graph graph) {
        this.nodes = new ArrayList<>(graph.getPlanets());
        this.routes = new ArrayList<>(graph.getRoutes());
    }

    public void setPredecessors(Map<Planet, Planet> predecessors) {
        this.predecessors = predecessors;
    }

    public void execute(Planet source) {
        initialize();
        distance.put(source, new Double(0));
        unSettledNodes.add(source);
        while (unSettledNodes.size() > 0) {
            Planet node = getMinimum(unSettledNodes);
            settledNodes.add(node);
            unSettledNodes.remove(node);
            findMinimalDistances(node);
        }
    }

    public void initialize() {
        settledNodes = new HashSet<>();
        unSettledNodes = new HashSet<>();
        distance = new HashMap<>();
        predecessors = new HashMap<>();
    }

    private void findMinimalDistances(Planet node) {
        List<Planet> adjacentNodes = getNeighbors(node);
        for (Planet target : adjacentNodes) {
            if (getShortestDistance(target) > getShortestDistance(node)
                    + getDistance(node, target)) {
                distance.put(target, getShortestDistance(node)
                        + getDistance(node, target));
                predecessors.put(target, node);
                unSettledNodes.add(target);
            }
        }
    }

    public void setWithTraffic(boolean withTraffic) {
        this.withTraffic = withTraffic;
    }


    public double getDistance(Planet node, Planet target) {
        double distance = -1;
        for (Route route : routes) {
            if (route.getOrigin().equals(node)
                    && route.getDestination().equals(target)) {
                if (withTraffic) {
                    return (route.getTraffic() + route.getDistance());
                }
                distance = route.getDistance();
            }
        }
        return distance;
    }

    public List<Planet> getNeighbors(Planet node) {
        List<Planet> neighbors = routes.stream().filter(route -> route.getOrigin().equals(node)
                && !isSettled(route.getDestination())).map(Route::getDestination).collect(Collectors.toList());
        return neighbors;
    }

    public List<Route> getPlanetRoutes(Planet node) {
        List<Route> neighbors = routes.stream().filter(route -> route.getOrigin().equals(node) || route.getDestination().equals(node)).collect(Collectors.toList());
        return neighbors;
    }


    public Planet getMinimum(Set<Planet> planets) {
        Planet minimum = null;
        for (Planet planet : planets) {
            if (minimum == null) {
                minimum = planet;
            } else {
                if (getShortestDistance(planet) < getShortestDistance(minimum)) {
                    minimum = planet;
                }
            }
        }
        return minimum;
    }

    private boolean isSettled(Planet vertex) {
        return settledNodes.contains(vertex);
    }

    public double getShortestDistance(Planet destination) {
        Double d = distance.get(destination);
        if (d == null) {
            return Integer.MAX_VALUE;
        } else {
            return d;
        }
    }

    /*
     * This method returns the path from the source to the selected target and
     * NULL if no path exists
     */
    public LinkedList<String> getPath(Planet target) {
        LinkedList<String> path = new LinkedList<>();
        Planet step = target;
        // check if a path exists
        if (predecessors.get(step) == null) {
            return null;
        }
        path.add(step.getName());
        while (predecessors.get(step) != null) {
            step = predecessors.get(step);
            path.add(step.getName());
        }
        // Put it into the correct order
        Collections.reverse(path);
        return path;
    }

    public void setDistance(Planet planet, Double distance) {
        this.distance.put(planet, distance);
    }
}
