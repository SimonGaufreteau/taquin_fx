package com.taquin;

public class DirectionUtils {
    public static Direction[] getPerpendicularDirections(Direction direction){
        if(direction==Direction.BOTTOM || direction==Direction.TOP){
            return new Direction[]{Direction.LEFT, Direction.RIGHT};
        }
        else
            return new Direction[]{Direction.TOP, Direction.BOTTOM};
    }
}
