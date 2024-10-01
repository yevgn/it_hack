package ru.mephi.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoadRequestWithPolygon {
    private PolygonRequest polygonRequest;
    private List<TransportObjectRequest> transportObjectRequestList;
}
