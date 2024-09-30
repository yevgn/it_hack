package ru.mephi.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.mephi.backend.enums.Category;
import ru.mephi.backend.enums.ResidentialType;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PolygonRequest {
    private List<Coordinate> coordinates;
    private Category category;
    private ResidentialType residentialType;
    private int floors;  // Количество этажей (для использования, если категория "residential")
}