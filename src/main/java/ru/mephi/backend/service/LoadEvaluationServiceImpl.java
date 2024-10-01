package ru.mephi.backend.service;

import ru.mephi.backend.dto.TransportObject;
import ru.mephi.backend.dto.TransportObjectRequest;

import java.util.List;
import java.util.Map;

public class LoadEvaluationServiceImpl implements LoadEvaluationService{

    @Override
    public Map<TransportObject, Double> getCapacityChanges(List<TransportObjectRequest> transportObjects) {
        return Map.of();
    }
}
