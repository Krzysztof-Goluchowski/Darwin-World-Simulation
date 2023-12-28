package org.model;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        Board map = new Board(20, 15);

        List<Integer> genotype = List.of(0, 0, 2, 0, 0);
        SimulationParameters simulationParameters = new SimulationParameters(10, 10, 5, 0, 0, SimulationParameters.MutationVariant.RANDOM, 2, 2, 1, 5);
        Animal chomik = new Animal(new Vector2D(2, 4), 10, genotype, simulationParameters);
        Animal chomik1 = new Animal(new Vector2D(2, 4), 10, genotype, simulationParameters);

        Simulation simulation = new Simulation(simulationParameters, map, List.of(chomik, chomik1));
        simulation.run();

        map.place(chomik);
        System.out.println(map.toString());
    }
}