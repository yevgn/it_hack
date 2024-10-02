package ru.mephi.backend;


import java.util.HashSet;
import ru.mephi.backend.dto.Coordinate;

public class CustomSet extends HashSet<Coordinate> {
    public static final float epsilon = 0.002f;

    @Override
    public boolean contains(Object o) {
        Coordinate that = (Coordinate) o;
        for(Coordinate c : this){
            if(Math.abs(c.getLatitude() - that.getLatitude()) < epsilon
                    && Math.abs(c.getLongitude() - that.getLongitude()) < epsilon)
                return true;
        }
        return false;
    }
}
