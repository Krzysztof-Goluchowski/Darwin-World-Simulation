package org.model;

import java.util.List;
import java.util.Map;

//Analogiczna klasa do tej z labow
public class Simulation {
    private final SimulationParameters parameters;
    private Board worldMap;
    private List<Animal> animalList;

    public Simulation(SimulationParameters parameters, Board worldMap, List<Animal> animalList) {
        this.parameters = parameters;
        this.worldMap = worldMap;
        this.animalList = animalList;
    }

    public void run() {
        // Umieszczamy zwierzęta na mapie
        for (Animal animal : animalList){
            worldMap.place(animal);
        }


        while (!animalList.isEmpty()){
            // Usunięcie martwych zwierzaków z mapy.
            for (Animal animal : animalList){
                if (animal.getEnergy() == 0){
                    animalList.remove(animal);
                }
            }

            // Skręt i przemieszczenie każdego zwierzaka.
            for (Animal animal : animalList){
                worldMap.move(animal);
            }

            // Konsumpcja roślin, na których pola weszły zwierzaki.
            Map<Vector2D, Plant> plantsMap = worldMap.getPlants();
            for (Animal animal : animalList){
                if (plantsMap.containsKey(animal.getPosition())) {
                    Vector2D animalPosition = animal.getPosition();
                    Plant plant = plantsMap.get(animalPosition);
                    animal.consumePlant(plant);
                    plantsMap.remove(animalPosition);
                }
            }

            // Rozmnażanie się najedzonych zwierzaków znajdujących się na tym samym polu.


            // Wzrastanie nowych roślin na wybranych polach mapy.
            worldMap.generatePlants(parameters.getNewPlantPerDay());
        }



    }
}