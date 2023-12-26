package org.model;

public class Plant {
    private final Vector2D position;

    public Plant(Vector2D position){
        this.position = position;
    }

    public Vector2D getPosition(){
        return position;
    }
}
