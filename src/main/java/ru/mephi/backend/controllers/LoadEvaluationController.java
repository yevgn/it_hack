package ru.mephi.backend.controllers;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.mephi.backend.dto.LoadRequestWithArea;
import ru.mephi.backend.dto.LoadRequestWithPolygon;
import ru.mephi.backend.service.LoadEvaluationService;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/load_evaluation")
public class LoadEvaluationController {

    private final LoadEvaluationService loadEvaluationService;

    @PostMapping("/area")
    public ResponseEntity<LoadRequestWithArea> getLoadEvaluationByArea(@RequestBody LoadRequestWithArea loadRequest){
        // получить данные
        // отправить запросы на UtilityController: посчитать площадь, население, доп нагрузку и тд
        // отправить запрос яндексу
        // отправить запрос graphhoper
        // отправить ответ в виде LoadResult
        return ResponseEntity.ok(loadRequest);
    }

    @PostMapping("/polygon")
    public ResponseEntity<LoadRequestWithPolygon> getLoadEvaluationByPolygon(@RequestBody LoadRequestWithPolygon loadRequest){
        // получить данные
        // отправить запросы на UtilityController: посчитать площадь, население, доп нагрузку и тд
        // отправить запрос яндексу
        // отправить запрос graphhoper
        // отправить ответ в виде LoadResult
        return ResponseEntity.ok(loadRequest);
    }

}

