package org.model;

import java.util.LinkedList;
import java.util.List;

public class Animal implements WorldElement {
    private int energy;
    private Vector2D position;
    private MapDirection orientation;
    private LinkedList<Integer> genotype;

    public Animal(Vector2D position, int energy, LinkedList<Integer> genotype){
        this.position = position;
        this.energy = energy;
        this.genotype = genotype;
        this.orientation = MapDirection.NORTH;
    }

    public int getEnergy() {
        return energy;
    }

    public Vector2D getPosition() {
        return position;
    }

    public MapDirection getOrientation() {
        return orientation;
    }

    public LinkedList<Integer> getGenotype() {
        return genotype;
    }


    public void move(Vector2D newPosition){
        this.position = newPosition;
    }
}
