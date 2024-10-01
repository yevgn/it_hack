package ru.mephi.backend.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoadDTO {
    private String name;
    private List<Coordinate> coordinates;
    private double capacity; // пропускная способность
    private double intensity; // интенсивность (пассажиропоток)
}
