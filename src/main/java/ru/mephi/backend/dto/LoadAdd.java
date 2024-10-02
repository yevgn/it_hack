package ru.mephi.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

// Дополнительный спрос на дороги и метро в результате застройки объекта

@Data
@AllArgsConstructor
public class LoadAdd {
    private int roadLoad; // доп нагрузка на дороги (количество машин)
    private int metroStationLoad; // доп нагрузка на метро (количество людей)
}
