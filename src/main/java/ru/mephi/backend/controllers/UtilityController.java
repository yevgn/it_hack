package ru.mephi.backend.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.mephi.backend.dto.AreaRequest;
import ru.mephi.backend.dto.LoadAdd;
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

    @PostMapping("/population/polygon")
    public int calculatePopulation(@RequestBody PolygonRequest polygonRequest) {
        return polygonService.calculatePopulationFromPolygonRequest(polygonRequest);
    }

    @PostMapping("/population/area")
    public int calculatePopulationArea(@RequestBody AreaRequest areaRequest) {
        return polygonService.calculatePopulationFromAreaRequest(areaRequest);
    }

    @PostMapping("/load/polygon")
    public LoadAdd calculateLoad(@RequestBody PolygonRequest polygonRequest) {
        return polygonService.calculateLoadFromPolygon(polygonRequest);
    }

    @PostMapping("/load/area")
    public LoadAdd calculateLoadArea(@RequestBody AreaRequest areaRequest) {
        return polygonService.calculateLoadFromArea(areaRequest);
    }
}
