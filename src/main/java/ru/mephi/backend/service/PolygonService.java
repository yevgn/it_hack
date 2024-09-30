package ru.mephi.backend.service;

import ru.mephi.backend.dto.AreaRequest;
import ru.mephi.backend.dto.PolygonRequest;

public interface PolygonService {
    double calculateArea(PolygonRequest polygonRequest);
    int calculatePopulationFromPolygonRequest(PolygonRequest polygonRequest);
    int calculatePopulationFromAreaRequest(AreaRequest area);
}