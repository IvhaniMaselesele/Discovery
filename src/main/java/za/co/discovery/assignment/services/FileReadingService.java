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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;

@Service
public class FileReadingService {

    PlanetDAO planetDAO;
    RouteDAO routeDAO;
    PlanetService planetService;
    RouteService routeService;


    @Autowired
    public FileReadingService(PlanetDAO planetDAO, RouteDAO routeDAO, PlanetService planetService, RouteService routeService) {
        this.routeService = routeService;
        this.planetService = planetService;
        this.planetDAO = planetDAO;
        this.routeDAO = routeDAO;
    }

    public void readPlanetSheet() {
        try {//TODO : Use Resource as a bean
            String fileName = new File("./").getCanonicalPath() + "\\src\\main\\java\\za\\co\\discovery\\assignment\\dataObjects\\planetTravelDetails.xlsx";
            FileInputStream file = new FileInputStream(fileName);

            XSSFWorkbook workbook = new XSSFWorkbook(file);
            XSSFSheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.iterator();
            getPlanetRowValues(rowIterator);
            file.close();
        } catch (FileNotFoundException e) {
        } catch (IOException e) {
        }
    }

    //TODO :  rename this method
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

    public void readRouteAndTrafficSheets() {
        try {
            String fileName = new File("./").getCanonicalPath() + "\\src\\main\\java\\za\\co\\discovery\\assignment\\dataObjects\\planetTravelDetails.xlsx";
            FileInputStream file = new FileInputStream(fileName);

            XSSFWorkbook workbook = new XSSFWorkbook(file);
            XSSFSheet sheet = workbook.getSheetAt(1);
            Iterator<Row> rowIterator = sheet.iterator();
            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                Iterator<Cell> cellIterator = row.cellIterator();
                Route route = getRouteRowValues(cellIterator);
                if (route != null) {
                    if (route.getDestination() != null && route.getOrigin() != null) {
                        routeService.persistRoute(route);
                    }
                }
            }
            file.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        getTraffic();
    }

    public void getTraffic() {
        try {
            String fileName = new File("./").getCanonicalPath() + "\\src\\main\\java\\za\\co\\discovery\\assignment\\dataObjects\\planetTravelDetails.xlsx";
            FileInputStream file = new FileInputStream(fileName);
            XSSFWorkbook workbook = new XSSFWorkbook(file);
            XSSFSheet sheet = workbook.getSheetAt(2);

            Iterator<Row> rowIterator = sheet.iterator();
            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                Iterator<Cell> cellIterator = row.cellIterator();
                Route route = getRouteRowValues(cellIterator);

                if (route != null) {
                    double traffic = route.getDistance();
                    int id = route.getId();
                    Route retrievedRoute = routeService.retrieveRoute(id);
                    //TODO : use service todo this
                    if (retrievedRoute != null) {
                        retrievedRoute.setTraffic(traffic);
                        routeService.persistRoute(retrievedRoute);
                    }
                }
            }
            file.close();
        } catch (FileNotFoundException e) {
        } catch (IOException e) {
        }
    }

    private Route getRouteRowValues(Iterator<Cell> cellIterator) {
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
