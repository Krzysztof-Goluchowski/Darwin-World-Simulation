package org.model;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        Board map = new Board(40, 20);

        List<Integer> genotype = List.of(7,7,7,7,7);
        genotype = new ArrayList<>(genotype);
        List<Integer> genotype1 = List.of(0, 0, 2, 0, 0);
        genotype1 = new ArrayList<>(genotype1);
        SimulationParameters simulationParameters = new SimulationParameters(10, 5, 5, 0, 0, SimulationParameters.MutationVariant.RANDOM, 2, 2, 1, 5);



        Animal chomik = new Animal(new Vector2D(12, 4), 10, genotype, simulationParameters);
        Animal chomik1 = new Animal(new Vector2D(11, 4), 10, genotype1, simulationParameters);

        Simulation simulation = new Simulation(simulationParameters, map, List.of(chomik, chomik1));
        simulation.run();
    }
}