package ru.mephi.backend.service;

import ru.mephi.backend.dto.AreaRequest;
import ru.mephi.backend.dto.Coordinate;
import ru.mephi.backend.dto.LoadAdd;
import ru.mephi.backend.dto.PolygonRequest;

public interface UtilityService {
    double calculateSquare(PolygonRequest polygonRequest);
    int calculatePopulationFromPolygonRequest(PolygonRequest polygonRequest);
    int calculatePopulationFromAreaRequest(AreaRequest area);
    LoadAdd calculateLoadFromPolygon(PolygonRequest polygonRequest);
    LoadAdd calculateLoadFromArea(AreaRequest area);
    Coordinate calculateCentroid(PolygonRequest polygonRequest);
}