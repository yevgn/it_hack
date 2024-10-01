package ru.mephi.backend.dto;

import lombok.*;
import ru.mephi.backend.enums.BuildingCategory;
import ru.mephi.backend.enums.ResidentialType;

@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class AreaRequest extends BuildingRequest {
    private double area;

    public AreaRequest(BuildingCategory category, ResidentialType type, double area ){
        super(category, type);
        this.area = area;
    }
}
