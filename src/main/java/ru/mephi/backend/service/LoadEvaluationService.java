package ru.mephi.backend.service;

import ru.mephi.backend.dto.TransportObject;
import ru.mephi.backend.dto.TransportObjectRequest;

import java.util.List;
import java.util.Map;

public interface LoadEvaluationService {
    Map<TransportObject, Double> getCapacityChanges(List<TransportObjectRequest> transportObjects);
}
