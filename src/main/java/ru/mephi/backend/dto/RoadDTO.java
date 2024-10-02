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
    private int capacity; // пропускная способность
    private int intensity; // интенсивность (пассажиропоток)
}
