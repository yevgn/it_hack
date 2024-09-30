package ru.mephi.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.mephi.backend.enums.Category;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PolygonRequest {
    private List<Coordinate> coordinates;
    private Category category;
    private String residentialType;
    private int floors;  // Количество этажей (для использования, если категория "residential")
}