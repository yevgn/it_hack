package ru.mephi.backend.service;

import lombok.extern.slf4j.Slf4j;
import org.locationtech.proj4j.*;
import org.springframework.stereotype.Service;
import ru.mephi.backend.dto.AreaRequest;
import ru.mephi.backend.dto.Coordinate;
import ru.mephi.backend.dto.LoadAdd;
import ru.mephi.backend.dto.PolygonRequest;
import ru.mephi.backend.enums.BuildingCategory;
import ru.mephi.backend.enums.ResidentialType;
import ru.mephi.coordinateset.CoordinateSet;

import java.util.Iterator;
import java.util.List;

@Service
@Slf4j
public class PolygonServiceImpl implements PolygonService {
    private final float percentOfOverallDemandForOffices = 0.35f;
    private final float percentOfOverallDemandForResidencies = 0.1f;

    @Override
    public double calculateSquare(PolygonRequest polygonRequest) {
        List<Coordinate> coordinates = polygonRequest.getCoordinates();
        int n = coordinates.size();

        CRSFactory crsFactory = new CRSFactory();
        CoordinateReferenceSystem geographic = crsFactory.createFromName("EPSG:4326"); // WGS84
        CoordinateReferenceSystem projected = crsFactory.createFromName("EPSG:28407"); // Pulkovo 1942 / Gauss-Kruger zone 7

        CoordinateTransformFactory ctFactory = new CoordinateTransformFactory();
        CoordinateTransform transform = ctFactory.createTransform(geographic, projected);

        ProjCoordinate srcCoord = new ProjCoordinate();
        ProjCoordinate destCoord = new ProjCoordinate();
        double[][] projectedCoordinates = new double[n][2];

        for (int i = 0; i < n; i++) {
            srcCoord.x = coordinates.get(i).getLongitude();
            srcCoord.y = coordinates.get(i).getLatitude();
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
    public int calculatePopulationFromPolygonRequest(PolygonRequest polygonRequest) {
        double area = calculateSquare(polygonRequest);
        return calcPopulation(area, polygonRequest.getCategory(), polygonRequest.getType(),
                polygonRequest.getFloors());
    }

    @Override
    public int calculatePopulationFromAreaRequest(AreaRequest area1) {
        double area = area1.getArea();
        return calcPopulation(area, area1.getCategory(), area1.getType(),
                1);
    }

    @Override
    public LoadAdd calculateLoadFromPolygon(PolygonRequest polygonRequest) {
        int population = calculatePopulationFromPolygonRequest(polygonRequest);
        return calcLoad(population, polygonRequest.getCategory());
    }

    @Override
    public LoadAdd calculateLoadFromArea(AreaRequest area) {
        int population = calculatePopulationFromAreaRequest(area);
        return calcLoad(population, area.getCategory());
    }

    @Override
    public Coordinate calculateCentroid(PolygonRequest polygonRequest) {
        List<Coordinate> coordinates = polygonRequest.getCoordinates();

        int n = coordinates.size();

        float centroidX = 0, centroidY = 0;
        float signedArea = 0.0f;
        float x0, y0, x1, y1;  // Временные переменные для координат вершин
        float a;

        // Проходим по всем вершинам кроме последней
        for (int i = 0; i < n; i++) {
            x0 = (float) coordinates.get(i).getLongitude();
            y0 = (float) coordinates.get(i).getLatitude();
            x1 = (float) coordinates.get(i + 1).getLongitude();
            y1 = (float) coordinates.get(i + 1).getLatitude();
            a = x0 * y1 - x1 * y0;
            signedArea += a;
            centroidX += (x0 + x1) * a;
            centroidY += (y0 + y1) * a;
        }

        // Последняя вершина (замыкаем полигон)
        x0 = coordinates.get(n - 1).getLongitude();
        y0 = coordinates.get(n - 1).getLatitude();
        x1 = coordinates.get(0).getLongitude();
        y1 = coordinates.get(0).getLatitude();
        a = x0 * y1 - x1 * y0;
        signedArea += a;
        centroidX += (x0 + x1) * a;
        centroidY += (y0 + y1) * a;

        // Завершающие вычисления
        signedArea *= 0.5f;
        centroidX /= (6.0f * signedArea);
        centroidY /= (6.0f * signedArea);

        // Возвращаем в виде (широта, долгота)
        return new Coordinate(centroidY, centroidX);
    }

    private int calcPopulation(double area, BuildingCategory category, ResidentialType type, int floors) {
        int population = 0;

        if (category.equals(BuildingCategory.RESIDENTIAL)) {
            double areaPerPerson;
            if (type.equals(ResidentialType.COMFORT)) {
                areaPerPerson = 45.0;
            } else {
                areaPerPerson = 25.0;
            }
            double totalArea = area * floors;
            population = (int) (totalArea / areaPerPerson);
        } else if (category.equals(BuildingCategory.OFFICE)) {
            double areaPerPerson = 35.0;
            population = (int) (area / areaPerPerson);
        }

        if(category == BuildingCategory.RESIDENTIAL)
            population = Math.round(population * percentOfOverallDemandForResidencies);
        if(category == BuildingCategory.OFFICE)
            population = Math.round(population * percentOfOverallDemandForOffices);

        return population;
    }

    private LoadAdd calcLoad(int population, BuildingCategory category) {
        int workingAgePopulation = 0;

        if(category == BuildingCategory.RESIDENTIAL)
            workingAgePopulation = (int) (population * 0.57);
        else
            workingAgePopulation = population;

        // Modal split: 70% public transport, 30% individual transport
        int publicTransportUsers = (int) (workingAgePopulation * 0.70);
        int individualTransportUsers = (int) (workingAgePopulation * 0.30);

        // Calculate the car load: number of cars needed considering an occupancy rate of 1.2
        int carLoad = (int) Math.ceil(individualTransportUsers / 1.2);

        return new LoadAdd(carLoad, publicTransportUsers);
    }
}
