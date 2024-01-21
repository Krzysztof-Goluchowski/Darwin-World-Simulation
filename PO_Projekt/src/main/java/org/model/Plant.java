package org.model;

public class Plant implements WorldElement {
    private final Vector2D position;

    public Plant(Vector2D position){
        this.position = position;
    }

    public Vector2D getPosition(){
        return position;
    }

    public String toString() {
        return "\u26AB";
    }
}
