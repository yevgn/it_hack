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

import java.io.IOException;
import java.net.URISyntaxException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/load_evaluation")
@Slf4j
public class LoadEvaluationController {

    private final LoadEvaluationService loadEvaluationService;

    @PostMapping("/area")
    public ResponseEntity<LoadResponse> getLoadEvaluationByArea(@RequestBody LoadRequestWithArea loadRequest) {

        try {
            return ResponseEntity.ok(loadEvaluationService.getCapacityChangesForArea(loadRequest));
        } catch (URISyntaxException | IOException | InterruptedException e){
            log.error(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/polygon")
    public ResponseEntity<LoadResponse> getLoadEvaluationByPolygon(
            @RequestBody LoadRequestWithPolygon loadRequest){

        try {
            return ResponseEntity.ok(loadEvaluationService.getCapacityChangesForPolygon(loadRequest));
        } catch (URISyntaxException | IOException | InterruptedException e){
            log.error(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}

