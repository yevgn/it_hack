package ru.mephi.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoadAdd {
    private int carLoad; // доп нагрузка на дороги (количество машин)
    private int metroLoad; // доп нагрузка на метро (количество людей)
}
