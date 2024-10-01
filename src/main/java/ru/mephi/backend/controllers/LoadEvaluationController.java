package ru.mephi.backend.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.mephi.backend.dto.LoadRequestWithArea;
import ru.mephi.backend.dto.LoadRequestWithPolygon;


@RestController
@RequestMapping("/api/load_evaluation")
public class LoadEvaluationController {
    private static final Logger log = LoggerFactory.getLogger(LoadEvaluationController.class);

    @PostMapping("/area")
    public ResponseEntity<LoadRequestWithArea> getLoadEvaluationByArea(@RequestBody LoadRequestWithArea loadRequest){
        // получить данные
        // отправить запросы на UtilityController: посчитать площадь, население, доп нагрузку и тд
        // отправить запрос яндексу
        // отправить запрос graphhoper
        // отправить ответ в виде LoadResult
        return null;
    }

    @PostMapping("/polygon")
    public ResponseEntity<LoadRequestWithPolygon> getLoadEvaluationByPolygon(@RequestBody LoadRequestWithPolygon loadRequest){
        // получить данные
        // отправить запросы на UtilityController: посчитать площадь, население, доп нагрузку и тд
        // отправить запрос яндексу
        // отправить запрос graphhoper
        // отправить ответ в виде LoadResult
        return null;
    }

}

