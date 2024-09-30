package ru.mephi.backend.service;

import ru.mephi.backend.dto.PolygonRequest;

public interface PolygonService {
    double calculateArea(PolygonRequest polygonRequest);
    int calculatePopulation(PolygonRequest polygonRequest);
}