package ru.mephi.backend.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.mephi.backend.dto.PolygonRequest;
import ru.mephi.backend.service.PolygonService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/polygon")
public class UtilityController {

    private final PolygonService polygonService;

    @PostMapping("/area")
    public double calculateArea(@RequestBody PolygonRequest polygonRequest) {
        return polygonService.calculateArea(polygonRequest);
    }

    @PostMapping("/population")
    public int calculatePopulation(@RequestBody PolygonRequest polygonRequest) {
        return polygonService.calculatePopulation(polygonRequest);
    }
}
