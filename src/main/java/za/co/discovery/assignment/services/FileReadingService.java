package za.co.discovery.assignment.services;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import za.co.discovery.assignment.dataAccess.PlanetDAO;
import za.co.discovery.assignment.dataAccess.RouteDAO;
import za.co.discovery.assignment.models.Planet;
import za.co.discovery.assignment.models.Route;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

@Service
public class FileReadingService {

    private final InputStream inputStream;
    PlanetDAO planetDAO;
    RouteDAO routeDAO;
    PlanetService planetService;
    RouteService routeService;


    @Autowired
    public FileReadingService(PlanetDAO planetDAO, RouteDAO routeDAO, PlanetService planetService, RouteService routeService, InputStream inputStream) {
        this.inputStream = inputStream;
        this.routeService = routeService;
        this.planetService = planetService;
        this.planetDAO = planetDAO;
        this.routeDAO = routeDAO;
    }

    @PostConstruct
    public void readFile() throws IOException {
        XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
        readPlanetSheet(workbook);
        readRouteAndTrafficSheets(workbook);
    }

    public void readPlanetSheet(XSSFWorkbook workbook) {
            XSSFSheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.iterator();
            getPlanetRowValues(rowIterator);
        getTraffic(workbook);
    }

    private void getPlanetRowValues(Iterator<Row> rowIterator) {
        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            Planet planet;
            String name = "";
            String node = "";
            Iterator<Cell> cellIterator = row.cellIterator();
            while (cellIterator.hasNext()) {
                Cell cell = cellIterator.next();
                switch (cell.getCellType()) {
                    case Cell.CELL_TYPE_STRING:
                        if (cell.getColumnIndex() == 1) name = cell.getStringCellValue();
                        else if (cell.getColumnIndex() == 0) node = cell.getStringCellValue();
                        break;
                }
            }
            if (!name.toLowerCase().contains("name")) {
                planet = new Planet(node, name);
                if (!planet.getNode().equalsIgnoreCase("")) {
                    planetService.persistPlanet(planet);
                }
            }
        }
    }

    public void readRouteAndTrafficSheets(XSSFWorkbook workbook) {
        XSSFSheet sheet = workbook.getSheetAt(1);
        Iterator<Row> rowIterator = sheet.iterator();
        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            Iterator<Cell> cellIterator = row.cellIterator();
            Route route = getRowValues(cellIterator);
            if (route != null) {
                if (route.getDestination() != null && route.getOrigin() != null) {
                    routeService.persistRoute(route);
                }
            }
        }
    }

    public void getTraffic(XSSFWorkbook workbook) {
        XSSFSheet sheet = workbook.getSheetAt(2);
        Iterator<Row> rowIterator = sheet.iterator();
        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            Iterator<Cell> cellIterator = row.cellIterator();
            Route route = getRowValues(cellIterator);
            if (route != null) {
                double traffic = route.getDistance();
                int id = route.getId();
                Route retrievedRoute = routeService.retrieveRoute(id);
                if (retrievedRoute != null) {
                    retrievedRoute.setTraffic(traffic);
                    routeService.persistRoute(retrievedRoute);
                }
            }
        }
    }

    private Route getRowValues(Iterator<Cell> cellIterator) {
        String destination = "";
        double distance = 0;
        String origin = "";
        int id = -1;
        while (cellIterator.hasNext()) {
            Cell cell = cellIterator.next();
            switch (cell.getCellType()) {
                case Cell.CELL_TYPE_NUMERIC:
                    if (cell.getColumnIndex() == 3) distance = cell.getNumericCellValue();
                    else if (cell.getColumnIndex() == 0) id = (int) cell.getNumericCellValue();
                    break;
                case Cell.CELL_TYPE_STRING:
                    if (cell.getColumnIndex() == 1) origin = cell.getStringCellValue();
                    else if (cell.getColumnIndex() == 2) destination = cell.getStringCellValue();
                    break;
            }
        }
        if (id != -1) {
            return CreateRoute(id, origin, destination, distance);
        }
        return null;
    }

    private Route CreateRoute(int id, String origin, String destination, double distance) {
        Planet originPlanet = planetService.retrievePlanet(origin);
        Planet destinationPlanet = planetService.retrievePlanet(destination);
        return new Route(id, originPlanet, destinationPlanet, distance);
    }

}
