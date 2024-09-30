package ru.mephi.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.mephi.backend.enums.Category;
import ru.mephi.backend.enums.ResidentialType;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AreaRequest {
    private double area;
    private Category category;
    private ResidentialType residentialType;
    private int floors;
}
