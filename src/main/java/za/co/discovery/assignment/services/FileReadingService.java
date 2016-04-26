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


    @Autowired
    public FileReadingService(PlanetDAO planetDAO, RouteDAO routeDAO, PlanetService planetService) {
        this.planetService = planetService;
        this.planetDAO = planetDAO;
        this.routeDAO = routeDAO;
    }

    public void readPlanetSheet() {
        try {
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


}
