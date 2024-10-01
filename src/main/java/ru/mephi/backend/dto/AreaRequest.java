package ru.mephi.backend.dto;

import lombok.*;
import ru.mephi.backend.enums.BuildingCategory;
import ru.mephi.backend.enums.ResidentialType;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AreaRequest {
    private BuildingCategory category;
    private ResidentialType type;
    private double area;
}
