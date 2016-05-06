package za.co.discovery.assignment.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.util.Assert;
import za.co.discovery.assignment.Assignment.Path;
import za.co.discovery.assignment.models.Graph;
import za.co.discovery.assignment.models.Planet;
import za.co.discovery.assignment.services.*;

import javax.annotation.PostConstruct;
import java.io.InputStream;
import java.util.LinkedList;

@Component
public class PathRepository {

    private final InputStream inputStream;
    private PlanetService planetService;
    private RouteService routeService;
    private GraphService graphService;

    private FileReadingService fileReadingService;
    @Autowired
    @Qualifier("transactionManager")
    protected PlatformTransactionManager platformTransactionManager;


    @Autowired
    public PathRepository(FileReadingService fileReadingService, GraphService graphService, PlanetService planetService, RouteService routeService, InputStream inputStream) {
        this.inputStream = inputStream;
        this.graphService = graphService;
        this.fileReadingService = fileReadingService;
        this.planetService = planetService;
        this.routeService = routeService;
    }

    @PostConstruct
    public void initData() {
        TransactionTemplate tpl = new TransactionTemplate(platformTransactionManager);
        tpl.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus transactionStatus) {
            }
        });
    }

    public Path findPath(String node) {
        Assert.notNull(node);

        Graph graph = new Graph(planetService.getPlanets(), routeService.getRoutes());
        Planet planet = graph.getPlanetByNode(node);

        DijkstraAlgorithm dijkstra = new DijkstraAlgorithm(graph);
        dijkstra.execute(graph.getPlanets().get(0));

        LinkedList<String> pathList = dijkstra.getPath(planet);
        String p;
        if (pathList == null) {
            p = "No Path From : " + graph.getPlanets().get(0).getName() + " to : " +
                    planet.getName();
        } else {

            p = "";
            for (String v : pathList) {
                p += v + ",";
            }
        }
        Path path = new Path();
        path.setName("Path from:");
        path.setDestination(planet.getName());
        path.setOrigin("Earth");
        path.setPathList(p);
        return path;
    }
}
