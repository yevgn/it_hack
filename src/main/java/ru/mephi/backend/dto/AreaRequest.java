package ru.mephi.backend.dto;

import lombok.*;
import ru.mephi.backend.enums.BuildingCategory;
import ru.mephi.backend.enums.ResidentialType;

// Используется, если пользователь ввел площадь вручную.

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AreaRequest {
    private BuildingCategory category;
    private ResidentialType type;
    private double area;
    Coordinate coordinate;
}
