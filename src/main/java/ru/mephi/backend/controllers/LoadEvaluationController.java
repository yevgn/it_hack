package ru.mephi.backend.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ru.mephi.backend.dto.*;
import ru.mephi.backend.service.LoadEvaluationService;

import java.io.IOException;
import java.net.URISyntaxException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/load_evaluation")
@Slf4j
@CrossOrigin
@Tag(
        name = "Контроллер для оценки запаса/дефицита пропускной способности транспортных объектов в час-пик " +
                "при возникновении дополнительного спроса"
)
public class LoadEvaluationController {

    private final LoadEvaluationService loadEvaluationService;

    @Operation(
            summary = "Получение оценки спроса/дефицита пропускной способности объекта в час-пик" +
                    " в случае если пользователь ввел площадь застройки вручную",
            description = "Оценка дается в виде пар ключ-знание. Ключ - транспортный объект. " +
                    "Значение - результат (разность между максимальной пропускной способностью объекта и пассажиропотоком" +
                    " с учетом дополнительного спроса)"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", description = "Не найдено",
                    content = @Content(mediaType = "application/json", examples = { @ExampleObject(
                            value = "{\"message\": \"Данной страницы не существует\", \"debugMessage\":\"" +
                                    "Данной страницы не существует\"}") })),
            @ApiResponse(responseCode = "200", description = "ОК")
    })
    @PostMapping("/area")
    public ResponseEntity<LoadResponse> getLoadEvaluationByArea(@RequestBody LoadRequestWithArea loadRequest) {

        try {
            return ResponseEntity.ok(loadEvaluationService.getCapacityChangesForArea(loadRequest));
        } catch (URISyntaxException | IOException | InterruptedException e){
            log.error(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(
            summary = "Получение оценки спроса/дефицита пропускной способности объекта в час-пик в случае " +
                    "если пользователь отметил точки территории застройки",
            description = "В данном случае на сервере вычисляется центр координат и суммарная поэтажная площадь. " +
                    "Оценка спроса/дефицита пропускной способности объекта дается в виде пар ключ-знание." +
                    " Ключ - транспортный объект. " +
                    "Значение - результат (разность между максимальной пропускной способностью объекта" +
                    " и пассажиропотоком с учетом дополнительного спроса)"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", description = "Не найдено",
                    content = @Content(mediaType = "application/json", examples = { @ExampleObject(
                            value = "{\"message\": \"Данной страницы не существует\", \"debugMessage\":\"" +
                                    "Данной страницы не существует\"}") })),
            @ApiResponse(responseCode = "200", description = "ОК")
    })
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

