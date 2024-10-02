package ru.mephi.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoadRequestWithPolygon {
    private PolygonRequest polygonRequest;
    private Set<RoadDTO> roadSet;
    private Set<MetroStationDTO> metroStationSet;
}
