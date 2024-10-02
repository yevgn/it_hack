package ru.mephi.backend.coordinateset;


import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import ru.mephi.backend.dto.Coordinate;

// Свое множество для координат

public class CoordinateSet extends HashSet<Coordinate> {
    public static final float epsilon = 0.002f;

    @Override
    public boolean contains(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    public boolean containsCoordinate(float latitude, float longitude) {
        for(Coordinate c : this){
            if(Math.abs(c.getLatitude() - latitude) < epsilon
                    && Math.abs(c.getLongitude() - longitude) < epsilon)
                return true;
        }
        return false;
    }

}
