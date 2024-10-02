package ru.mephi.backend.dto;

import lombok.*;
import ru.mephi.coordinateset.CoordinateSet;
import ru.mephi.backend.enums.BuildingCategory;
import ru.mephi.backend.enums.ResidentialType;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PolygonRequest{
    private BuildingCategory category;
    private ResidentialType type;
    private List<Coordinate> coordinates;
    private int floors;
}