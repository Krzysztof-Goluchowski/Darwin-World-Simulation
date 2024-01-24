package org.model;

import java.util.*;

public class Simulation {
    private final SimulationParameters parameters;
    private final Board worldMap;
    private final List<Animal> animalList;
    private boolean isPaused = false;
    private final List<SimulationObserver> observers = new ArrayList<>();
    private int simulationDay;
    public void addObserver(SimulationObserver observer) {
        observers.add(observer);
    }

    protected void notifyObservers() {
        boolean isEnd = animalList.isEmpty();
        for (SimulationObserver observer : observers) {
            observer.update(animalList.size(), worldMap.getPlants().size(), calculateAverageEnergy(),
                    calculateAverageLifeSpan(), worldMap.getAmountOfFreeSpots(), simulationDay,
                    calculateAverageChildren(), isEnd, findMostPopularGenotype());
        }
    }

    public double calculateAverageChildren() {
        if (animalList.isEmpty()) {
            return 0;
        }
        int totalChildren = 0;
        for (Animal animal : animalList) {
            totalChildren += animal.getAmountOfChildren();
        }
        return (double) totalChildren / animalList.size();
    }

    public double calculateAverageEnergy() {
        if (animalList.isEmpty()) {
            return 0;
        }
        double totalEnergy = 0;
        for (Animal animal : animalList) {
            totalEnergy += animal.getEnergy();
        }
        return totalEnergy / animalList.size();
    }
    private final List<Animal> allDeadAnimals = new ArrayList<>(); // Lista przechowująca wszystkie martwe zwierzęta

    public double calculateAverageLifeSpan() {
        if (allDeadAnimals.isEmpty()) {
            return 0;
        }
        int totalLifeSpan = 0;
        for (Animal animal : allDeadAnimals) {
            totalLifeSpan += animal.getDaysSurvived();
        }
        return (double) totalLifeSpan / allDeadAnimals.size();
    }

    public List<Integer> findMostPopularGenotype() {
        Map<List<Integer>, Integer> genotypeFrequency = new HashMap<>();

        // Zliczanie częstotliwości każdego genotypu
        for (Animal animal : animalList) {
            List<Integer> genotype = animal.getGenotype();
            genotypeFrequency.put(genotype, genotypeFrequency.getOrDefault(genotype, 0) + 1);
        }

        // Znajdowanie najpopularniejszego genotypu
        List<Integer> mostPopularGenotype = null;
        int maxFrequency = 0;
        for (Map.Entry<List<Integer>, Integer> entry : genotypeFrequency.entrySet()) {
            if (entry.getValue() > maxFrequency) {
                mostPopularGenotype = entry.getKey();
                maxFrequency = entry.getValue();
            }
        }

        return mostPopularGenotype;
    }

    public Simulation(SimulationParameters parameters, Board worldMap, List<Animal> animalList) {
        this.parameters = parameters;
        this.worldMap = worldMap;
        this.animalList = new ArrayList<>(animalList);
    }

    public void pause() {
        List<Integer> mostPopularGenotype = findMostPopularGenotype();

        for (Animal animal : animalList) {
            if (animal.getGenotype().equals(mostPopularGenotype)) {
                animal.setHasMostPopularGenotype(true);
            } else {
                animal.setHasMostPopularGenotype(false);
            }
        }
        isPaused = true;
    }

    public void resume() {
        for (Animal animal : animalList) {
            animal.setHasMostPopularGenotype(false);
        }

        isPaused = false;
    }

    public void run() throws InterruptedException {
        // Umieszczamy zwierzęta na mapie
        for (Animal animal : animalList) {
            if (animal.getPosition() == null){
                generateNewPosition(animal);
            }
            worldMap.place(animal);
        }
        worldMap.notifyObservers("Animals placed");
        //Tworze startowa liczbe roslin
        worldMap.generatePlants(parameters.getStartingAmountOfPlants());


        while (!animalList.isEmpty()) {
            // Usunięcie martwych zwierzaków z mapy.
            ArrayList<Animal> deadAnimals = new ArrayList<>();
            for (Animal animal : animalList) {
                if(animal.getEnergy() <= 0){
                    deadAnimals.add(animal);
                    allDeadAnimals.add(animal);
                }
            }

            removeDeadAnimals(deadAnimals);

            // Skręt i przemieszczenie każdego zwierzaka.
            for (Animal animal : animalList) {
                worldMap.move(animal);
            }

            // Konsumpcja roślin, na których pola weszły zwierzaki.
            Map<Vector2D, Plant> plantsMap = worldMap.getPlants();
            Collections.sort(animalList);  // Rozstrzygamy tym możliwe konflikty
            for (Animal animal : animalList) {
                if (plantsMap.containsKey(animal.getPosition())) {
                    Vector2D animalPosition = animal.getPosition();
                    animal.consumePlant();
                    plantsMap.remove(animalPosition);
                }
            }

            // Rozmnażanie się najedzonych zwierzaków znajdujących się na tym samym polu.
            reproduceAnimals();

            // Wzrastanie nowych roślin na wybranych polach mapy.
            worldMap.generatePlants(parameters.getNewPlantPerDay());

            //Koniec dnia symulacji, odejmujemy energie zuzyta na ruch i zwiekszamy ilosc przzytych dni przez zwierze
            for (Animal animal : animalList) {
                animal.setEnergy(animal.getEnergy() - parameters.getEnergyLostPerDay());
                animal.setDaysSurvived(animal.getDaysSurvived() + 1);
            }
            worldMap.notifyObservers("Animal moved");
            Thread.sleep(200);

            simulationDay++;
            notifyObservers();

            while (isPaused) {
                Thread.sleep(100); // Poczekaj, jeśli symulacja jest w trybie pauzy
            }
        }
    }

    private void removeDeadAnimals(ArrayList<Animal> deadAnimals) {
        for (Animal animal : deadAnimals){
            Map<Vector2D, Animal> animals = worldMap.getAnimals();
            animals.remove(animal.getPosition());
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
                Collections.sort(animalsOnPosition);
                for (int i = 0; i < animalsOnPosition.size(); i++) {
                    Animal animal1 = animalsOnPosition.get(i);
                    if (animal1.isReadyToReproduce()) {
                        for (int j = i + 1; j < animalsOnPosition.size(); j++) {
                            Animal animal2 = animalsOnPosition.get(j);
                            if (animal2.isReadyToReproduce()) {
                                Animal child = animal1.reproduce(animal2);
                                animalList.add(child);
                                worldMap.place(child);
                                break;
                            }
                        }
                    }
                }
            }
        }
    }
}