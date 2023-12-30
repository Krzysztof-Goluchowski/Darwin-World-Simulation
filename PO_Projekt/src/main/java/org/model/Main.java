package org.model;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        Board map = new Board(5, 5);

        SimulationParameters simulationParameters = new SimulationParameters(5, 5, 5, 0, 0, SimulationParameters.MutationVariant.RANDOM, 2, 2, 1, 5);

        Animal chomik = new Animal(new Vector2D(2, 2), 10, simulationParameters);
        Animal chomik1 = new Animal(new Vector2D(11, 4), 12, simulationParameters);

        Simulation simulation = new Simulation(simulationParameters, map, List.of(chomik, chomik1));
        simulation.run();
    }
}