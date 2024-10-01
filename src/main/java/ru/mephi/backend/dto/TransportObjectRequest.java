package ru.mephi.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.mephi.backend.enums.TransportObjectCategory;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransportObjectRequest {
    private TransportObject object;
    private Coordinate[] coordinates;
    private double capacity; // пропускная способность
    private double intensity; // интенсивность (пассажиропоток)
}
