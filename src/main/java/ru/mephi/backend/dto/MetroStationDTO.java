package ru.mephi.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MetroStationDTO {
    private String name;
    private String route; // линия
    private Coordinate coordinate;
    private double capacity; // пропускная способность
    private double intensity; // интенсивность (пассажиропоток)
}
