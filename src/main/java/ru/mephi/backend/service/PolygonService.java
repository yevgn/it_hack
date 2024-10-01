package ru.mephi.backend.service;

import ru.mephi.backend.dto.AreaRequest;
import ru.mephi.backend.dto.LoadResult;
import ru.mephi.backend.dto.PolygonRequest;

public interface PolygonService {
    double calculateArea(PolygonRequest polygonRequest);
    int calculatePopulationFromPolygonRequest(PolygonRequest polygonRequest);
    int calculatePopulationFromAreaRequest(AreaRequest area);
    LoadResult calculateLoadFromPolygon(PolygonRequest polygonRequest);
    LoadResult calculateLoadFromArea(AreaRequest area);
}