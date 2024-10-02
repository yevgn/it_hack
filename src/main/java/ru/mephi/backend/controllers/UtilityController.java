package ru.mephi.backend.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.mephi.backend.dto.AreaRequest;
import ru.mephi.backend.dto.Coordinate;
import ru.mephi.backend.dto.LoadAdd;
import ru.mephi.backend.dto.PolygonRequest;
import ru.mephi.backend.service.UtilityService;


@RestController
@RequiredArgsConstructor
@CrossOrigin
@Tag(
        name = "Контроллер для вычислительных операций над объектом застройки"
)
@RequestMapping("/api/utility")
public class UtilityController {

    private final UtilityService utilityService;

    @Operation(
            summary = "Вычисление площади застройки",
            description = "Вычисление площади застройки по координатам"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", description = "Не найдено",
                    content = @Content(mediaType = "application/json", examples = { @ExampleObject(
                            value = "{\"message\": \"Данной страницы не существует\", \"debugMessage\":\"" +
                                    "Данной страницы не существует\"}") })),
            @ApiResponse(responseCode = "200", description = "ОК")
    })
    @PostMapping("/square")
    public double calculateSquare(@RequestBody PolygonRequest polygonRequest) {
        return utilityService.calculateSquare(polygonRequest);
    }

    @Operation(
            summary = "Вычисление кол-ва населения, которое будет использовать УДС и станции метро в час-пик " +
                    "в результате застройки территории, если пользователь сам отметил точки территории застройки " +
                    "на карте",
            description = "От общего числа дополнительного населения берем 10% в случае жилой площади, 35% - в случае" +
                    " офисной застройки"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", description = "Не найдено",
                    content = @Content(mediaType = "application/json", examples = { @ExampleObject(
                            value = "{\"message\": \"Данной страницы не существует\", \"debugMessage\":\"" +
                                    "Данной страницы не существует\"}") })),
            @ApiResponse(responseCode = "200", description = "ОК")
    })
    @PostMapping("/population/polygon")
    public int calculatePopulation(@RequestBody PolygonRequest polygonRequest) {
        return utilityService.calculatePopulationFromPolygonRequest(polygonRequest);
    }

    @Operation(
            summary = "Вычисление кол-ва населения, которое будет использовать УДС и станции метро в час-пик " +
                    "в результате застройки территории, если пользователь вручную ввел площадь застройки",
            description = "От общего числа дополнительного населения берем 10% в случае жилой площади, 35% - в случае" +
                    " офисной застройки"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", description = "Не найдено",
                    content = @Content(mediaType = "application/json", examples = { @ExampleObject(
                            value = "{\"message\": \"Данной страницы не существует\", \"debugMessage\":\"" +
                                    "Данной страницы не существует\"}") })),
            @ApiResponse(responseCode = "200", description = "ОК")
    })
    @PostMapping("/population/area")
    public int calculatePopulationArea(@RequestBody AreaRequest areaRequest) {
        return utilityService.calculatePopulationFromAreaRequest(areaRequest);
    }

    @Operation(
            summary = "Вычисление суммарного дополнительного спроса на УДС и станции метро в час-пик в " +
                    "результате застройки, в случае если пользователь отметил точки территории застройки на карте",
            description = "70% всего спроса идет на станции метро, 30% - на авто (УДС)"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", description = "Не найдено",
                    content = @Content(mediaType = "application/json", examples = { @ExampleObject(
                            value = "{\"message\": \"Данной страницы не существует\", \"debugMessage\":\"" +
                                    "Данной страницы не существует\"}") })),
            @ApiResponse(responseCode = "200", description = "ОК")
    })
    @PostMapping("/load/polygon")
    public LoadAdd calculateLoad(@RequestBody PolygonRequest polygonRequest) {
        return utilityService.calculateLoadFromPolygon(polygonRequest);
    }

    @Operation(
            summary = "Вычисление суммарного дополнительного спроса на УДС и станции метро в час-пик в " +
                    "результате застройки, в случае если пользователь вручную ввел площадь застройки",
            description = "70% всего спроса идет на станции метро, 30% - на авто (УДС)"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", description = "Не найдено",
                    content = @Content(mediaType = "application/json", examples = { @ExampleObject(
                            value = "{\"message\": \"Данной страницы не существует\", \"debugMessage\":\"" +
                                    "Данной страницы не существует\"}") })),
            @ApiResponse(responseCode = "200", description = "ОК")
    })
    @PostMapping("/load/area")
    public LoadAdd calculateLoadArea(@RequestBody AreaRequest areaRequest) {
        return utilityService.calculateLoadFromArea(areaRequest);
    }


    @Operation(
            summary = "Вычисление центра координат застройки",
            description = "Вычисление центра координат застройки по введенным точкам"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", description = "Не найдено",
                    content = @Content(mediaType = "application/json", examples = { @ExampleObject(
                            value = "{\"message\": \"Данной страницы не существует\", \"debugMessage\":\"" +
                                    "Данной страницы не существует\"}") })),
            @ApiResponse(responseCode = "200", description = "ОК")
    })
    @PostMapping("/centroid")
    public Coordinate calculateCentroid(@RequestBody PolygonRequest polygonRequest) {
        return utilityService.calculateCentroid(polygonRequest);
    }
}
