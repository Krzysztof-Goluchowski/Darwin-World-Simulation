package org.model;

public enum MapDirection {
    NORTH,
    NORTH_EAST,
    EAST,
    SOUTH_EAST,
    SOUTH,
    SOUTH_WEST,
    WEST,
    NORTH_WEST;

    public MapDirection direction(Animal animal, int num){
        int orientationNumber = getOrientationNumber(animal);

        int newDirection = (num + orientationNumber) % 8;

        return switch (num) {
            case 0 -> NORTH;
            case 1 -> NORTH_EAST;
            case 2 -> EAST;
            case 3 -> SOUTH_EAST;
            case 4 -> SOUTH;
            case 5 -> SOUTH_WEST;
            case 6 -> WEST;
            case 7 -> NORTH_WEST;
            default -> throw new IllegalStateException("Unexpected value: " + num);
        };
    }

    public int getOrientationNumber(Animal animal) {
        MapDirection orientation = animal.getOrientation();
        return switch (orientation) {
            case NORTH -> 0;
            case NORTH_EAST -> 1;
            case EAST -> 2;
            case SOUTH_EAST -> 3;
            case SOUTH -> 4;
            case SOUTH_WEST -> 5;
            case WEST -> 6;
            case NORTH_WEST -> 7;
            default -> -1;
        };
    }

    public Vector2D toUnitVector(){
        return switch (this){
            case NORTH -> new Vector2D(0, 1);
            case NORTH_EAST -> new Vector2D(1, 1);
            case EAST -> new Vector2D(1, 0);
            case SOUTH_EAST -> new Vector2D(1, -1);
            case SOUTH -> new Vector2D(0, -1);
            case SOUTH_WEST -> new Vector2D(-1, -1);
            case WEST -> new Vector2D(-1, 0);
            case NORTH_WEST -> new Vector2D(-1, 1);
        };
    }
}
