package ru.mephi.backend.dto;

import lombok.*;
import ru.mephi.backend.enums.BuildingCategory;
import ru.mephi.backend.enums.ResidentialType;

import java.util.List;

@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class PolygonRequest extends BuildingRequest{
    private List<Coordinate> coordinates;
    private int floors;

    public PolygonRequest(BuildingCategory category, ResidentialType type, List<Coordinate> coordinates, int floors){
        super(category, type);
        this.coordinates = coordinates;
        this.floors = floors;
    }

}