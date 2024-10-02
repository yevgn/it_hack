package ru.mephi.backend.controllers;

import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ru.mephi.backend.dto.*;
import ru.mephi.backend.service.LoadEvaluationService;
import ru.mephi.backend.service.PolygonService;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/load_evaluation")
@Slf4j
public class LoadEvaluationController {

    private final LoadEvaluationService loadEvaluationService;
    private final PolygonService polygonService;

    @PostMapping("/area")
    public ResponseEntity<LoadResponse> getLoadEvaluationByArea(@RequestBody LoadRequestWithArea loadRequest) {

        LoadAdd extraLoad = polygonService.calculateLoadFromArea(loadRequest.getAreaRequest());

        try {
            Map<RoadDTO, Integer> roadCapacityChanges = loadEvaluationService.getRoadCapacityChanges(
                    loadRequest.getRoadSet(),
                    extraLoad.getRoadLoad(),
                    loadRequest.getAreaRequest().getCoordinate()
            );

            Map<MetroStationDTO, Integer> metroStationCapacityChanges = loadEvaluationService.getMetroStationCapacityChanges(
                    loadRequest.getMetroStationSet(),
                    extraLoad.getMetroStationLoad(),
                    loadRequest.getAreaRequest().getCoordinate()
            );

            return ResponseEntity.ok(
                    LoadResponse.builder()
                            .roadCapacityChanges(roadCapacityChanges)
                            .metroStationCapacityChanges(metroStationCapacityChanges)
                            .build()
            );

        } catch (URISyntaxException | IOException | InterruptedException e){
            log.error(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/polygon")
    public ResponseEntity<LoadResponse> getLoadEvaluationByPolygon(
            @RequestBody LoadRequestWithPolygon loadRequest){

        try {
            Coordinate constructionPoint = polygonService.calculateCentroid(loadRequest.getPolygonRequest());
            LoadAdd extraLoad = polygonService.calculateLoadFromPolygon(loadRequest.getPolygonRequest());

            Map<RoadDTO, Integer> roadCapacityChanges = loadEvaluationService.getRoadCapacityChanges(
                    loadRequest.getRoadSet(),
                    extraLoad.getRoadLoad(),
                    constructionPoint
            );

            Map<MetroStationDTO, Integer> metroStationCapacityChanges = loadEvaluationService.getMetroStationCapacityChanges(
                    loadRequest.getMetroStationSet(),
                    extraLoad.getMetroStationLoad(),
                    constructionPoint
            );

            return ResponseEntity.ok(
                    LoadResponse.builder()
                            .roadCapacityChanges(roadCapacityChanges)
                            .metroStationCapacityChanges(metroStationCapacityChanges)
                            .build()
            );
        } catch (URISyntaxException | IOException | InterruptedException e){
            log.error(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}

