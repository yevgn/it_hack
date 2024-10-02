package ru.mephi.backend.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.mephi.backend.coordinateset.CoordinateSet;

import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoadDTO {
    private String name;
    private CoordinateSet coordinates;
    private int capacity; // пропускная способность
    private int intensity; // интенсивность (пассажиропоток)

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RoadDTO roadDTO = (RoadDTO) o;
        return Objects.equals(name, roadDTO.name);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name);
    }
}
