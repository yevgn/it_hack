package ru.mephi.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MetroStationDTO that = (MetroStationDTO) o;
        return Objects.equals(name, that.name) && Objects.equals(route, that.route);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, route);
    }
}
