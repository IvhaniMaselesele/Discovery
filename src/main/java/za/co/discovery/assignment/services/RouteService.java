package za.co.discovery.assignment.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import za.co.discovery.assignment.dataAccess.RouteDAO;
import za.co.discovery.assignment.models.Edge;
import za.co.discovery.assignment.models.Planet;
import za.co.discovery.assignment.models.Route;

import java.util.List;

@Service
public class RouteService {
    RouteDAO routeDAO;

    @Autowired
    public RouteService(RouteDAO routeDAO) {
        this.routeDAO = routeDAO;
    }

    public Route persistRoute(Route route) {
        return routeDAO.save(route);
    }

    public List<Route> getRoutes() {
        return routeDAO.retrieveAll();
    }

    public Route retrieveRoute(int routeId) {
        return routeDAO.retrieve(routeId);
    }

    public Edge retrieveRouteAsEdge(int routeId) {
        Route route = routeDAO.retrieve(routeId);
        Edge edge = new Edge(route.getId() + "", route.getOrigin(), route.getDestination(), route.getDistance());
        edge.setTraffic(route.getTraffic());
        return edge;
    }

    public int getNextAvailableKey() {
        List<Route> routes = getRoutes();
        int size = routes.size();
        int nextId = size;

        while (true) {
            Route route = retrieveRoute(nextId);
            if (route == null) {
                break;
            } else {
                nextId += 1;

            }
        }
        return nextId;
    }

    //TODO : Test this
    public void deleteRoute(String routeId) {
        routeDAO.deleteById(Integer.parseInt(routeId));
    }


    public Route createRoute(int i, Planet originPlanet, Planet destinationPlanet, double weight, double traffic) {
        return new Route(i, originPlanet, destinationPlanet, weight, traffic);
    }
}
