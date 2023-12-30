package org.model;

import org.model.util.MapVisualizer;

import java.util.*;

import static org.model.MapDirection.newDirection;

//Tworzy zwykla mape w wariancie bez tuneli
public class Board {
    private final int width;
    private final int height;
    private Map<Vector2D, Animal> animals;
    private Map<Vector2D, Plant> plants;

    public Board(int width, int height){
        this.width = width;
        this.height = height;
        this.animals = new HashMap<>();
        this.plants = new HashMap<>();
    }

    public Map<Vector2D, Animal> getAnimals() {
        return animals;
    }

    public Map<Vector2D, Plant> getPlants() {
        return plants;
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
        if (animals.containsKey(position)) {
            return animals.get(position);
        } else if (plants.containsKey(position)){
            return new Plant(position);
        }
        return null;
    }

    public boolean canMoveTo(Vector2D position){
        return position.getY() < this.height && position.getY() >= 0;
    }

    public void place(Animal animal) {
        Vector2D position = animal.getPosition();
        if (canMoveTo(position)) {
            animals.put(position, animal);
//            notifyObservers("Animal placed on (" + position.getX() + ", " + position.getY() + ")");
        }
    }

    public void move(Animal animal) {
        Vector2D newPosition;
        MapDirection newPotentialOrientation;
        int numDirection = animal.nextMove();

        MapDirection direction = newDirection(animal, numDirection);

        switch (direction){
            case NORTH -> {
                Vector2D unitVector = MapDirection.NORTH.toUnitVector();
                newPosition = animal.getPosition().add(unitVector);
                newPotentialOrientation = MapDirection.NORTH;
            }
            case NORTH_EAST -> {
                Vector2D unitVector = MapDirection.NORTH_EAST.toUnitVector();
                newPosition = animal.getPosition().add(unitVector);
                newPotentialOrientation = MapDirection.NORTH_EAST;
            }
            case EAST -> {
                Vector2D unitVector = MapDirection.EAST.toUnitVector();
                newPosition = animal.getPosition().add(unitVector);
                newPotentialOrientation = MapDirection.EAST;
            }
            case SOUTH_EAST -> {
                Vector2D unitVector = MapDirection.SOUTH_EAST.toUnitVector();
                newPosition = animal.getPosition().add(unitVector);
                newPotentialOrientation = MapDirection.SOUTH_EAST;
            }
            case SOUTH -> {
                Vector2D unitVector = MapDirection.SOUTH.toUnitVector();
                newPosition = animal.getPosition().add(unitVector);
                newPotentialOrientation = MapDirection.SOUTH;
            }
            case SOUTH_WEST -> {
                Vector2D unitVector = MapDirection.SOUTH_WEST.toUnitVector();
                newPosition = animal.getPosition().add(unitVector);
                newPotentialOrientation = MapDirection.SOUTH_WEST;
            }
            case WEST -> {
                Vector2D unitVector = MapDirection.WEST.toUnitVector();
                newPosition = animal.getPosition().add(unitVector);
                newPotentialOrientation = MapDirection.WEST;
            }
            case NORTH_WEST -> {
                Vector2D unitVector = MapDirection.NORTH_WEST.toUnitVector();
                newPosition = animal.getPosition().add(unitVector);
                newPotentialOrientation = MapDirection.NORTH_WEST;
            }
            default -> {
                return;
            }
        }

        if (newPosition != null && !canMoveTo(newPosition)) { //probowal wyjsc na biegun, nie wychodzi a jego orientacja zmienia sie na przeciwna, to dodanie 4
            animal.setOrientation(MapDirection.newDirection(animal, 4));
        }

        if (newPosition != null && canMoveTo(newPosition))  {
            Vector2D oldPosition = animal.getPosition();

            newPosition = roundEarth(newPosition);

            animals.remove(animal.getPosition());
            animal.move(newPosition);
            animal.setOrientation(newPotentialOrientation);
            animals.put(animal.getPosition(), animal);

//            notifyObservers("Animal moved from " + oldPosition + " to " + newPosition);
        }
    }

    private Vector2D roundEarth(Vector2D newPosition) {
        if (newPosition.getX() < 0){
            return new Vector2D(width - 1, newPosition.getY());
        }
        return new Vector2D(newPosition.getX() % width, newPosition.getY());
    }

    // Generowanie roślin z większą szansą na równiku
    public void generatePlants(int numOfPlants){
        int areaOfBoard = width * height;
        for (int i = 0; i < numOfPlants; i++){
            if (plants.size() < areaOfBoard) {
                boolean flag = false;

                while (!flag) {
                    Vector2D newPosition = generateNewPlantPosition();
                    if (!plants.containsKey(newPosition)) {
                        plants.put(newPosition, new Plant(newPosition));
                        flag = true;
                    }
                }
            }
        }
    }

    public Vector2D generateNewPlantPosition(){
        int middleOfBoard = this.height / 2;
        int radiusOfEquator = (int) (0.1 * this.height);
        int bottomLimitOfEquator = middleOfBoard - radiusOfEquator;
        int upperLimitOfEquator = middleOfBoard + radiusOfEquator;

        Random random = new Random();
        double losowaLiczba = random.nextDouble();

        int newY = 0;

        if (losowaLiczba < 0.8){ // Roślina wyrasta na równiku
            if (radiusOfEquator == 0){
                newY = bottomLimitOfEquator;
            } else {
                newY = random.nextInt(2 * radiusOfEquator) + bottomLimitOfEquator;
            }
        } else if (losowaLiczba < 0.9){ // Roślina wyrasta pod równikiem
            newY = random.nextInt(bottomLimitOfEquator);
        } else { // Roślina wyrasta nad równikiem
            if (height % 2 == 0){
                newY = random.nextInt(bottomLimitOfEquator) + upperLimitOfEquator;
            } else {
                newY = random.nextInt(bottomLimitOfEquator + 1) + upperLimitOfEquator;
            }
        }

        int newX = random.nextInt(this.width);

        return new  Vector2D(newX, newY);
    }

    public String toString(){
        MapVisualizer visualizer = new MapVisualizer(this);
        Vector2D bottomLeft = new Vector2D(0, 0);
        Vector2D topRight = new Vector2D(width - 1, height - 1);
        return visualizer.draw(bottomLeft, topRight);
    }
}