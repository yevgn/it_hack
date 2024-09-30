package ru.mephi.backend.service;

import ru.mephi.backend.dto.Coordinate;
import ru.mephi.backend.dto.PolygonRequest;
import ru.mephi.backend.enums.Category;

import java.util.List;

public class PolygonServiceImpl implements PolygonService {
    @Override
    public double calculateArea(PolygonRequest polygonRequest) {
        List<Coordinate> coordinatesList = polygonRequest.getCoordinates();

        int n = coordinatesList.size();

        // Ensure the polygon is closed by repeating the first point at the end if necessary
        if (coordinatesList.get(0).getX() != coordinatesList.get(n - 1).getX() ||
                coordinatesList.get(0).getY() != coordinatesList.get(n - 1).getY()) {
            coordinatesList.add(new Coordinate(coordinatesList.get(0).getX(), coordinatesList.get(0).getY()));
            n = coordinatesList.size();
        }

        double area = 0.0;

        for (int i = 0; i < n - 1; i++) {
            double x1 = coordinatesList.get(i).getX();
            double y1 = coordinatesList.get(i).getY();
            double x2 = coordinatesList.get(i + 1).getX();
            double y2 = coordinatesList.get(i + 1).getY();
            area += x1 * y2 - x2 * y1;
        }

        return Math.abs(area) / 2.0;
    }

    @Override
    public int calculatePopulation(PolygonRequest polygonRequest) {
        double area = calculateArea(polygonRequest);
        int population = 0;

        if (polygonRequest.getCategory().equals(Category.RESIDENTIAL)) {
            double areaPerPerson;
            if ("comfort".equalsIgnoreCase(polygonRequest.getResidentialType())) {
                areaPerPerson = 45.0;
            } else {
                areaPerPerson = 25.0;
            }
            double totalArea = area * polygonRequest.getFloors();
            population = (int) (totalArea / areaPerPerson);
        } else if (polygonRequest.getCategory().equals(Category.OFFICE)) {
            double areaPerPerson = 35.0;
            population = (int) (area / areaPerPerson);
        }

        return population;

    }
}
