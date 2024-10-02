package ru.mephi.backend.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.mephi.backend.dto.AreaRequest;
import ru.mephi.backend.dto.Coordinate;
import ru.mephi.backend.dto.LoadAdd;
import ru.mephi.backend.dto.PolygonRequest;
import ru.mephi.backend.service.UtilityService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/utility")
public class UtilityController {

    private final UtilityService utilityService;

    @PostMapping("/square")
    public double calculateSquare(@RequestBody PolygonRequest polygonRequest) {
        return utilityService.calculateSquare(polygonRequest);
    }

    @PostMapping("/population/polygon")
    public int calculatePopulation(@RequestBody PolygonRequest polygonRequest) {
        return utilityService.calculatePopulationFromPolygonRequest(polygonRequest);
    }

    @PostMapping("/population/area")
    public int calculatePopulationArea(@RequestBody AreaRequest areaRequest) {
        return utilityService.calculatePopulationFromAreaRequest(areaRequest);
    }

    @PostMapping("/load/polygon")
    public LoadAdd calculateLoad(@RequestBody PolygonRequest polygonRequest) {
        return utilityService.calculateLoadFromPolygon(polygonRequest);
    }

    @PostMapping("/load/area")
    public LoadAdd calculateLoadArea(@RequestBody AreaRequest areaRequest) {
        return utilityService.calculateLoadFromArea(areaRequest);
    }

    @PostMapping("/centroid")
    public Coordinate calculateCentroid(@RequestBody PolygonRequest polygonRequest) {
        return utilityService.calculateCentroid(polygonRequest);
    }
}
