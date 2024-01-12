package org.model;

import org.model.util.ConsoleMapDisplay;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        Board map = new Board(5, 5);

//        ConsoleMapDisplay observer = new ConsoleMapDisplay();
//        map.setObserver(observer);

        SimulationParameters parameters = new SimulationParametersBuilder()
                .startingAmountOfPlants(10)
                .minReproduceEnergy(5)
                .energyLostOnReproduction(5)
                .minMutations(0)
                .maxMutations(0)
                .mutationVariant(SimulationParameters.MutationVariant.RANDOM)
                .mapVariant(SimulationParameters.MapVariant.STANDARD)
                .plantEnergy(2)
                .newPlantPerDay(1)
                .energyLostPerDay(5)
                .genotypeSize(5)
                .startingAnimalEnergy(10)
                .build();

        Animal chomik = new Animal(parameters);
        Animal chomik1 = new Animal(parameters);

        chomik1.setEnergy(0);

        Simulation simulation = new Simulation(parameters, map, List.of(chomik, chomik1));
        SimulationEngine engine = new SimulationEngine(List.of(simulation));

        engine.runAsync();
    }
}