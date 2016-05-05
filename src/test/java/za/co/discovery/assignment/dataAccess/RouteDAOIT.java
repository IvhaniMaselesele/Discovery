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
import za.co.discovery.assignment.models.Route;
import za.co.discovery.assignment.models.RouteBuilder;

import java.util.ArrayList;
import java.util.List;

import static com.shazam.shazamcrest.MatcherAssert.assertThat;
import static com.shazam.shazamcrest.matcher.Matchers.sameBeanAs;
import static org.hamcrest.CoreMatchers.is;


@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {PersistenceConfig.class, DataSourceConfig.class, RouteDAO.class,
        DaoConfig.class}, loader = AnnotationConfigContextLoader.class)
public class RouteDAOIT {
    @Autowired
    private SessionFactory sessionFactory;
    private PlanetDAO planetDAO;
    private RouteDAO routeDAO;

    @Before
    public void init() throws Exception {
        routeDAO = new RouteDAO(sessionFactory);
        planetDAO = new PlanetDAO(sessionFactory);
    }

    @Test
    public void savedRouteCanBeRetrieved() {
        Route expectedRoute = getRoute(1);
        Session session = sessionFactory.getCurrentSession();

        routeDAO.save(expectedRoute);

        Criteria criteria = session.createCriteria(Route.class);
        Route actualRoute = (Route) criteria.uniqueResult();

        assertThat(actualRoute, is(sameBeanAs(expectedRoute)));
    }

    @Test
    public void savedRouteCanBeUpdated() {
        Planet origin = PlanetBuilder
                .aPlanet().withNode("A")
                .build();
        Planet newOrigin = PlanetBuilder
                .aPlanet().withNode("C")
                .build();

        Route route = RouteBuilder
                .aRoute().withId(2)
                .withOrigin(origin)
                .build();
        planetDAO.save(origin);

        Route expectedRoute = RouteBuilder
                .aRoute().withId(2)
                .withOrigin(newOrigin)
                .build();
        Route savedRoute = routeDAO.save(route);

        Route retrievedRoute = routeDAO.retrieve(savedRoute.getId());
        retrievedRoute.setOrigin(newOrigin);

        Route actualRoute = routeDAO.update(retrievedRoute);

        assertThat(actualRoute, is(sameBeanAs(expectedRoute)));
    }

    @Test
    public void deletedRouteCannotBeRetrieved() {
        int id = 1;
        Route route = getRoute(id);

        routeDAO.save(route);
        routeDAO.deleteById(id);
        Route deletedRoute = routeDAO.retrieve(id);

        assertThat(deletedRoute, is(sameBeanAs(null)));
    }

    @Test
    public void retrieveAll_ShouldReturnAllPlanetsSaved() {
        Route route0 = getRoute(0);
        Route route1 = getRoute(1);

        List<Route> expectedRouteList = new ArrayList<>();
        expectedRouteList.add(route0);
        expectedRouteList.add(route1);

        routeDAO.save(route0);
        routeDAO.save(route1);
        List<Route> actualRouteList = routeDAO.retrieveAll();

        Assert.assertThat(actualRouteList, sameBeanAs(expectedRouteList));
    }

    @Test
    public void retrieveRoute_ReturnsSavedRoute() {
        Route expectedRoute = getRoute(2);

        Route savedRoute = routeDAO.save(expectedRoute);
        Route actualRoute = routeDAO.retrieve(savedRoute.getId());

        assertThat(actualRoute, is(sameBeanAs(expectedRoute)));
    }

    private Route getRoute(int id) {
        Planet origin = PlanetBuilder
                .aPlanet().withNode("A")
                .build();
        planetDAO.save(origin);
        Planet destination = PlanetBuilder
                .aPlanet().withNode("B")
                .build();
        planetDAO.save(destination);
        return RouteBuilder
                .aRoute().withId(id)
                .withOrigin(origin)
                .withDestination(destination)
                .withDistance(0.2)
                .build();
    }

}