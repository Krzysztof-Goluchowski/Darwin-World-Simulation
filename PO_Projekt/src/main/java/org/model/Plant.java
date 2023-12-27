package org.model;

public class Plant implements WorldElement {
    private final Vector2D position;
    private final int energy;

    public Plant(Vector2D position, int energy){
        this.position = position;
        this.energy = energy;
    }

    public Vector2D getPosition(){
        return position;
    }
    public int getEnergy() {
        return energy;
    }
}
