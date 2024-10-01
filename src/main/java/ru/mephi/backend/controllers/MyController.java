package ru.mephi.backend.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class MyController {
    @PostMapping("/metroStations")
    public ResponseEntity<Object> putMetroStations(){
        return null;
    }

    @PostMapping("/roads")
    public ResponseEntity<Object> putRoads(){
        return null;
    }

    @GetMapping()
    public ResponseEntity<Object> getEvaluation(){

        return null;
    }

}

