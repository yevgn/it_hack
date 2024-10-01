package ru.mephi.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoadRequestWithArea {
    private AreaRequest areaRequest;
    private List<RoadDTO> roadList;
    private List<MetroStationDTO> metroStationList;
}
