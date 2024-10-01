package ru.mephi.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import ru.mephi.backend.enums.BuildingCategory;
import ru.mephi.backend.enums.ResidentialType;

@Data
@AllArgsConstructor
public abstract class BuildingRequest {
    private BuildingCategory category;
    private ResidentialType residentialType;
}
