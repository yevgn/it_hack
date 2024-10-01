package ru.mephi.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.mephi.backend.enums.RoadType;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoadDto {
    private String name;
    private RoadType type;
    private Coordinate[] coordinates;
}
