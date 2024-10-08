package ru.mephi.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

// Используется, если пользователь ввел площадь вручную.

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoadRequestWithArea {
    private AreaRequest areaRequest;
    private Set<RoadDTO> roadSet;  // множество всех дорог на фрагменте карты
    private Set<MetroStationDTO> metroStationSet; // множество всех станций метро на фрагменте карты
}
