package ru.mephi.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

// Используется, если пользователь отметил точки площади застройки на карте и ввел количество этажей

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoadRequestWithPolygon {
    private PolygonRequest polygonRequest;
    private Set<RoadDTO> roadSet;
    private Set<MetroStationDTO> metroStationSet;
}
