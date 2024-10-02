package ru.mephi.backend.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.mephi.coordinateset.CoordinateSet;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoadDTO {
    private String name;
    private CoordinateSet coordinates;
    private int capacity; // пропускная способность
    private int intensity; // интенсивность (пассажиропоток)
}
