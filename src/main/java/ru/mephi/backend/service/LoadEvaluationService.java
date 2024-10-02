package ru.mephi.backend.service;

import ru.mephi.backend.dto.*;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.Set;

public interface LoadEvaluationService {
    Map<RoadDTO, Integer> getRoadCapacityChanges(Set<RoadDTO> roads, int extraLoad, Coordinate constructionPoint)
            throws URISyntaxException, IOException, InterruptedException;

    Map<MetroStationDTO, Integer> getMetroStationCapacityChanges(
            Set<MetroStationDTO> metroStations, int extraLoad, Coordinate constructionPoint)
            throws URISyntaxException, IOException, InterruptedException;

    LoadResponse getCapacityChangesForPolygon(LoadRequestWithPolygon request)
            throws URISyntaxException, IOException, InterruptedException;

    LoadResponse getCapacityChangesForArea(LoadRequestWithArea request)
            throws URISyntaxException, IOException, InterruptedException ;
}
