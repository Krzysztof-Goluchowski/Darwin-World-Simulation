package org.model;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        Board map = new Board(5, 5);

        SimulationParameters simulationParameters = new SimulationParameters(10, 5, 5, 0, 0, SimulationParameters.MutationVariant.RANDOM, SimulationParameters.MapVariant.STANDARD, 2, 1, 5, 5, 10);
        Animal chomik = new Animal(simulationParameters);
        Animal chomik1 = new Animal(simulationParameters);

        Simulation simulation = new Simulation(simulationParameters, map, List.of(chomik, chomik1));
        simulation.run();
    }
}