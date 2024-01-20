package org.model;

import java.util.*;

//Analogiczna klasa do tej z labow
public class Simulation {
    private final SimulationParameters parameters;
    private Board worldMap;
    private List<Animal> animalList;
    private int simulationDay = 0;

    public Simulation(SimulationParameters parameters, Board worldMap, List<Animal> animalList) {
        this.parameters = parameters;
        this.worldMap = worldMap;
        this.animalList = new ArrayList<>(animalList);
    }

    public void run() throws InterruptedException {
        // Umieszczamy zwierzęta na mapie
        for (Animal animal : animalList) {
            if (animal.getPosition() == null){
                generateNewPosition(animal);
            }
            worldMap.place(animal);
            Thread.sleep(500);
        }
        //Tworze startowa liczbe roslin
        worldMap.generatePlants(parameters.getStartingAmountOfPlants());


        while (!animalList.isEmpty()) {
            // Usunięcie martwych zwierzaków z mapy.
            ArrayList<Animal> deadAnimals = new ArrayList<>();
            for (Animal animal : animalList) {
                if(animal.getEnergy() <= 0){
                    deadAnimals.add(animal);
                    Thread.sleep(500);
                }
            }

            removeDeadAnimals(deadAnimals);

            // Skręt i przemieszczenie każdego zwierzaka.
            for (Animal animal : animalList) {
                worldMap.move(animal);
                Thread.sleep(500);
            }

            // Konsumpcja roślin, na których pola weszły zwierzaki.
            Map<Vector2D, Plant> plantsMap = worldMap.getPlants();
            Collections.sort(animalList);  // Rozstrzygamy tym możliwe konflikty
            for (Animal animal : animalList) {
                if (plantsMap.containsKey(animal.getPosition())) {
                    Vector2D animalPosition = animal.getPosition();
                    Plant plant = plantsMap.get(animalPosition);
                    animal.consumePlant(plant);
                    plantsMap.remove(animalPosition);
                    Thread.sleep(500);
                }
            }

            // Rozmnażanie się najedzonych zwierzaków znajdujących się na tym samym polu.
            reproduceAnimals();

            // Wzrastanie nowych roślin na wybranych polach mapy.
            worldMap.generatePlants(parameters.getNewPlantPerDay());

            //Koniec dnia symulacji, odejmujemy energie zuzyta na ruch i zwiekszamy ilosc przzytych dni przez zwierze
            simulationDay += 1;
            for (Animal animal : animalList) {
                animal.setEnergy(animal.getEnergy() - parameters.getEnergyLostPerDay());
                animal.setDaysSurvived(animal.getDaysSurvived() + 1);
            }
        }
    }

    private void removeDeadAnimals(ArrayList<Animal> deadAnimals) {
        for (Animal animal : deadAnimals){
            animalList.remove(animal);
        }
    }

    public void generateNewPosition(Animal animal){
        Random random = new Random();
        int x = random.nextInt(worldMap.getWidth());
        int y = random.nextInt(worldMap.getHeight());
        animal.setPosition(new Vector2D(x, y));
    }

    private void reproduceAnimals() {
        Map<Vector2D, List<Animal>> animalsByPosition = new HashMap<>();

        // Grupowanie zwierząt na podstawie ich pozycji
        for (Animal animal : animalList) {
            animalsByPosition.computeIfAbsent(animal.getPosition(), k -> new ArrayList<>()).add(animal);
        }

        // Przeszukiwanie każdej pozycji w poszukiwaniu par do rozmnażania
        for (List<Animal> animalsOnPosition : animalsByPosition.values()) {
            if (animalsOnPosition.size() >= 2) {
                Collections.sort(animalsOnPosition); // Rozstrzygamy tym możliwe konflikty
                for (int i = 0; i < animalsOnPosition.size(); i++) {
                    Animal animal1 = animalsOnPosition.get(i);
                    if (animal1.isReadyToReproduce()) {
                        for (int j = i + 1; j < animalsOnPosition.size(); j++) {
                            Animal animal2 = animalsOnPosition.get(j);
                            if (animal2.isReadyToReproduce()) {
                                Animal child = animal1.reproduce(animal2);
                                animalList.add(child);
                                worldMap.place(child);
                                break; // chyba tylko raz dziennie mozna sie rozmnazac
                            }
                        }
                    }
                }
            }
        }
    }
}