package ru.mephi.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.mephi.backend.enums.TransportObjectCategory;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransportObject {
    private TransportObjectCategory category;
    private String name;
}
