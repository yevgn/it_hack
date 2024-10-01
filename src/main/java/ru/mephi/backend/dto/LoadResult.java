package ru.mephi.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoadResult {
    private int carLoad; // Нагрузка на дороги (количество машин)
    private int metroLoad; // Нагрузка на метро (количество людей)
}
