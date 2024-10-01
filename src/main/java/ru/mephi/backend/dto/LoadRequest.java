package ru.mephi.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoadRequest {
    private BuildingRequest buildingRequest;
    private TransportObjectRequest transportObjectRequest;
}
