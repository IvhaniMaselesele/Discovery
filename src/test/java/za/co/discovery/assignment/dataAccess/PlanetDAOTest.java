package za.co.discovery.assignment.dataAccess;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.transaction.annotation.Transactional;
import za.co.discovery.assignment.DaoConfig;
import za.co.discovery.assignment.DataSourceConfig;
import za.co.discovery.assignment.PersistenceConfig;
import za.co.discovery.assignment.models.Planet;
import za.co.discovery.assignment.models.PlanetBuilder;

import java.util.ArrayList;
import java.util.List;

import static com.shazam.shazamcrest.MatcherAssert.assertThat;
import static com.shazam.shazamcrest.matcher.Matchers.sameBeanAs;
import static org.hamcrest.CoreMatchers.is;

@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {PersistenceConfig.class, DataSourceConfig.class, PlanetDAO.class,
        DaoConfig.class}, loader = AnnotationConfigContextLoader.class)
public class PlanetDAOTest {
    @Autowired
    private SessionFactory sessionFactory;

    private PlanetDAO planetDAO;

    @Before
    public void init() {
        planetDAO = new PlanetDAO(sessionFactory);
    }

    @Test
    public void savedPlanetCanBeRetrieved() {
        Planet expectedPlanet = getPlanet("A");
        Session session = sessionFactory.getCurrentSession();

        planetDAO.save(expectedPlanet);

        Criteria criteria = session.createCriteria(Planet.class);
        Planet actualPlanet = (Planet) criteria.uniqueResult();

        assertThat(actualPlanet, is(sameBeanAs(expectedPlanet)));
    }

    @Test
    public void savedPlanetCanBeUpdated() {
        Planet planet = getPlanet("A");
        Planet expectedPlanet = new Planet("A", "new name");

        Planet savedPlanet = planetDAO.save(planet);
        Planet retrievedPlanet = planetDAO.retrieve(savedPlanet.getNode());
        retrievedPlanet.setName("new name");

        Planet actualPlanet = planetDAO.update(retrievedPlanet);

        assertThat(actualPlanet, is(sameBeanAs(expectedPlanet)));
    }

    @Test
    public void deletedPlanetCannotBeRetrieved() {
        String node = "A";
        Planet planet = getPlanet(node);

        planetDAO.save(planet);
        planetDAO.deleteByNode(node);
        Planet deletedPlanet = planetDAO.retrieve(node);

        assertThat(deletedPlanet, is(sameBeanAs(null)));
    }

    @Test
    public void retrievePlanet_ReturnsSavedPlanet() {
        Planet expectedPlanet = getPlanet("B");

        Planet savedPlanet = planetDAO.save(expectedPlanet);
        Planet actualPlanet = planetDAO.retrieve(savedPlanet.getNode());

        assertThat(actualPlanet, is(sameBeanAs(expectedPlanet)));
    }

    @Test
    public void retrieveAll_ShouldReturnAllPlanetsSaved() {
        Planet planet0 = PlanetBuilder.aPlanet().withNode("A").build();
        Planet planet1 = PlanetBuilder.aPlanet().withNode("B").build();

        List<Planet> expectedPlanetList = new ArrayList<>();
        expectedPlanetList.add(planet0);
        expectedPlanetList.add(planet1);

        planetDAO.save(planet0);
        planetDAO.save(planet1);
        List<Planet> actualPlanetList = planetDAO.retrieveAll();

        Assert.assertThat(actualPlanetList, sameBeanAs(expectedPlanetList));
    }


    private Planet getPlanet(String node) {
        return new Planet(node, "AB");
    }
}