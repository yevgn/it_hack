package ru.mephi.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoadResponse {
    // пары транспортный объект - изменение пропускной способности
    // если со знаком "+" - запас, если с "-" - дефицит
    private Map<RoadDTO, Integer> roadCapacityChanges;
    private Map<MetroStationDTO, Integer> metroStationCapacityChanges;
}
