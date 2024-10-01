package ru.mephi.backend.service;

import ru.mephi.backend.dto.*;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;

public interface LoadEvaluationService {
    Map<RoadDTO, Double> getRoadCapacityChanges(List<RoadDTO> roads, int extraLoad, Coordinate constructionPoint)
            throws URISyntaxException, IOException, InterruptedException;
    Map<MetroStationDTO, Double> getMetroStationCapacityChanges(
            List<MetroStationDTO> metroStations, int extraLoad, Coordinate constructionPoint);
}
