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

import java.util.List;

@Service
@Slf4j
public class PolygonServiceImpl implements PolygonService {

    @Override
    public double calculateSquare(PolygonRequest polygonRequest) {
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
    public int calculatePopulationFromPolygonRequest(PolygonRequest polygonRequest) {
        double area = calculateSquare(polygonRequest);
        final float percentOfOverallDemandForResidentialBuilding = 0.1f; // 10% от спроса для жилых застроек
        return Math.round(calcPopulation(area, polygonRequest.getCategory(), polygonRequest.getType(),
                polygonRequest.getFloors()) * percentOfOverallDemandForResidentialBuilding);
    }

    @Override
    public int calculatePopulationFromAreaRequest(AreaRequest area1) {
        double area = area1.getArea();
        final float percentOfOverallDemandForOffices = 0.35f;
        return Math.round(calcPopulation(area, area1.getCategory(), area1.getType(),
                1) * percentOfOverallDemandForOffices);
    }

    @Override
    public LoadAdd calculateLoadFromPolygon(PolygonRequest polygonRequest) {
        int totalPopulation = calculatePopulationFromPolygonRequest(polygonRequest);
        return calcLoad(totalPopulation);
    }

    @Override
    public LoadAdd calculateLoadFromArea(AreaRequest area) {
        int totalPopulation = calculatePopulationFromAreaRequest(area);
        return calcLoad(totalPopulation);
    }

    @Override
    public Coordinate calculateCentroid(PolygonRequest polygonRequest) {
        List<Coordinate> coordinatesList = polygonRequest.getCoordinates();
        int n = coordinatesList.size();

        float centroidX = 0, centroidY = 0;
        float signedArea = 0.0f;
        float x0, y0, x1, y1;  // Временные переменные для координат вершин
        float a;

        // Проходим по всем вершинам кроме последней
        for (int i = 0; i < n - 1; ++i) {
            x0 = (float) coordinatesList.get(i).getLongitude();
            y0 = (float) coordinatesList.get(i).getLatitude();
            x1 = (float) coordinatesList.get(i + 1).getLongitude();
            y1 = (float) coordinatesList.get(i + 1).getLatitude();
            a = x0 * y1 - x1 * y0;
            signedArea += a;
            centroidX += (x0 + x1) * a;
            centroidY += (y0 + y1) * a;
        }

        // Последняя вершина (замыкаем полигон)
        x0 = coordinatesList.get(n - 1).getLongitude();
        y0 = coordinatesList.get(n - 1).getLatitude();
        x1 = coordinatesList.get(0).getLongitude();
        y1 = coordinatesList.get(0).getLatitude();
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

        return population;
    }

    private LoadAdd calcLoad(int totalPopulation) {
        int workingAgePopulation = (int) (totalPopulation * 0.57);

        // Modal split: 70% public transport, 30% individual transport
        int publicTransportUsers = (int) (workingAgePopulation * 0.70);
        int individualTransportUsers = (int) (workingAgePopulation * 0.30);

        // Calculate the car load: number of cars needed considering an occupancy rate of 1.2
        int carLoad = (int) Math.ceil(individualTransportUsers / 1.2);

        return new LoadAdd(carLoad, publicTransportUsers);
    }
}
