package org.model;

import org.model.util.MapVisualizer;

import java.util.HashMap;
import java.util.Map;

public class Board {
    private final int width;
    private final int height;
    protected final Map<Vector2D, Animal> animals;
    protected final Map<Vector2D, Plant> plants;

    public Board(int width, int height){
        this.width = width;
        this.height = height;
        this.animals = new HashMap<>();
        this.plants = new HashMap<>();
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public boolean isOccupied(Vector2D position) {
        return objectAt(position) != null;
    }

    public WorldElement objectAt(Vector2D position) {
        return animals.get(position);
    }

    public boolean canMoveTo(Vector2D position){
        return !animals.containsKey(position);
    }

    public boolean place(Animal animal) {
        Vector2D position = animal.getPosition();
        if (canMoveTo(position) && !animals.containsKey(position)) {
            animals.put(position, animal);
//            notifyObservers("Animal placed on (" + position.getX() + ", " + position.getY() + ")");
            return true;
        }
        return false;
    }

    public void move(Animal animal, int numDirection) {
        Vector2D newPosition = null;

        MapDirection direction = MapDirection.newDirection(animal, numDirection);

        switch (direction){
            case NORTH -> {
                Vector2D unitVector = MapDirection.NORTH.toUnitVector();
                newPosition = animal.getPosition().add(unitVector);
            }
            case NORTH_EAST -> {
                Vector2D unitVector = MapDirection.NORTH_EAST.toUnitVector();
                newPosition = animal.getPosition().add(unitVector);
            }
            case EAST -> {
                Vector2D unitVector = MapDirection.EAST.toUnitVector();
                newPosition = animal.getPosition().add(unitVector);
            }
            case SOUTH_EAST -> {
                Vector2D unitVector = MapDirection.SOUTH_EAST.toUnitVector();
                newPosition = animal.getPosition().add(unitVector);
            }
            case SOUTH -> {
                Vector2D unitVector = MapDirection.SOUTH.toUnitVector();
                newPosition = animal.getPosition().add(unitVector);
            }
            case SOUTH_WEST -> {
                Vector2D unitVector = MapDirection.SOUTH_WEST.toUnitVector();
                newPosition = animal.getPosition().add(unitVector);
            }
            case WEST -> {
                Vector2D unitVector = MapDirection.WEST.toUnitVector();
                newPosition = animal.getPosition().add(unitVector);
            }
            case NORTH_WEST -> {
                Vector2D unitVector = MapDirection.NORTH_WEST.toUnitVector();
                newPosition = animal.getPosition().add(unitVector);
            }
            default -> {
                return;
            }
        }

        if (newPosition != null && canMoveTo(newPosition))  {
            Vector2D oldPosition = animal.getPosition();

            animals.remove(animal.getPosition());
            animal.move(newPosition);
            animals.put(animal.getPosition(), animal);

            // Zakladam, ze zwierze zje rosline zawszy gdy stanie na polu z istniejaca roslina
            if (plants.containsKey(newPosition)) {
                Plant plant = plants.get(newPosition);
                animal.consumePlant(plant);
                plants.remove(newPosition);
            }

//            notifyObservers("Animal moved from " + oldPosition + " to " + newPosition);
        }
    }

    public String toString(){
        MapVisualizer visualizer = new MapVisualizer(this);
        Vector2D bottomLeft = new Vector2D(0, 0);
        Vector2D topRight = new Vector2D(width - 1, height - 1);
        return visualizer.draw(bottomLeft, topRight);
    }
}
