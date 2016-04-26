package za.co.discovery.assignment.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import za.co.discovery.assignment.dataAccess.PlanetDAO;
import za.co.discovery.assignment.models.Planet;

import java.util.List;

@Service
public class PlanetService {

    PlanetDAO planetDAO;

    @Autowired
    public PlanetService(PlanetDAO planetDAO) {
        this.planetDAO = planetDAO;
    }

    public Planet persistPlanet(Planet planet) {
        return planetDAO.save(planet);
    }

    public List<Planet> getPlanets() {
        return planetDAO.retrieveAll();
    }

    public Planet retrievePlanet(String planetId) {
        return planetDAO.retrieve(planetId);
    }

    public void deletePlanet(String planetNode) {
        planetDAO.deleteByNode(planetNode);
    }
}
