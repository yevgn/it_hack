package ru.mephi.backend.service;

import lombok.extern.slf4j.Slf4j;
import org.locationtech.proj4j.*;
import org.springframework.stereotype.Service;
import ru.mephi.backend.dto.Coordinate;
import ru.mephi.backend.dto.PolygonRequest;
import ru.mephi.backend.enums.Category;
import ru.mephi.backend.enums.ResidentialType;

import java.util.List;

@Service
@Slf4j
public class PolygonServiceImpl implements PolygonService {
    @Override
    public double calculateArea(PolygonRequest polygonRequest) {
        List<Coordinate> coordinatesList = polygonRequest.getCoordinates();
        int n = coordinatesList.size();

        CRSFactory crsFactory = new CRSFactory();
        CoordinateReferenceSystem geographic = crsFactory.createFromName("EPSG:4326"); // WGS84
        CoordinateReferenceSystem projected = crsFactory.createFromName("EPSG:28407"); // Pulkovo 1942 / Gauss-Kruger zone 7

        CoordinateTransformFactory ctFactory = new CoordinateTransformFactory();
        CoordinateTransform transform = ctFactory.createTransform(geographic, projected);

        ProjCoordinate srcCoord = new ProjCoordinate();
        ProjCoordinate destCoord = new ProjCoordinate();
        double[][] projectedCoordinates = new double[n][2];

        for (int i = 0; i < n; i++) {
            srcCoord.x = coordinatesList.get(i).getLongitude();
            srcCoord.y = coordinatesList.get(i).getLatitude();
            transform.transform(srcCoord, destCoord);
            projectedCoordinates[i][0] = destCoord.x;
            projectedCoordinates[i][1] = destCoord.y;
        }

        if (projectedCoordinates[0][0] != projectedCoordinates[n - 1][0] ||
                projectedCoordinates[0][1] != projectedCoordinates[n - 1][1]) {
            n++;
            double[][] temp = new double[n][2];
            System.arraycopy(projectedCoordinates, 0, temp, 0, projectedCoordinates.length);
            temp[n - 1][0] = projectedCoordinates[0][0];
            temp[n - 1][1] = projectedCoordinates[0][1];
            projectedCoordinates = temp;
        }

        double area = 0.0;
        for (int i = 0; i < n - 1; i++) {
            double x1 = projectedCoordinates[i][0];
            double y1 = projectedCoordinates[i][1];
            double x2 = projectedCoordinates[i + 1][0];
            double y2 = projectedCoordinates[i + 1][1];
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
            if (polygonRequest.getResidentialType().equals(ResidentialType.COMFORT)) {
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
