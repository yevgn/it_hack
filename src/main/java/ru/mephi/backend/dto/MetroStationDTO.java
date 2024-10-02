package ru.mephi.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MetroStationDTO {
    private String name;
    private String route; // линия
    private Coordinate coordinate;
    private int capacity; // пропускная способность
    private int intensity; // интенсивность (пассажиропоток)
}
