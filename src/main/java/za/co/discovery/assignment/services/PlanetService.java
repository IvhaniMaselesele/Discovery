package za.co.discovery.assignment.services;

import org.springframework.stereotype.Service;
import za.co.discovery.assignment.models.Planet;

import java.util.ArrayList;
import java.util.List;

@Service
public class PlanetService {
    private List<Planet> planets;

    public List<Planet> getPlanets() {
        planets = new ArrayList<>();
        planets.add(new Planet("1", "1"));
        planets.add(new Planet("2", "2"));
        planets.add(new Planet("3", "3"));
        planets.add(new Planet("4", "4"));
        return planets;
    }
}
