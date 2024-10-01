package ru.mephi.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoadResult {
    // пары транспортный объект - изменение пропускной способности
    // если со знаком "+" - запас, если с "-" - дефицит
    private Map<TransportObject, Double> capacityChanges;
}
