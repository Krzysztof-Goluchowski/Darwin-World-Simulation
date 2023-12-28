package org.model;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        Board map = new Board(20, 15);

        List<Integer> genotype = List.of(0, 0, 2, 0, 0);
        SimulationParameters simulationParameters = new SimulationParameters(10, 5, 0, 0, SimulationParameters.MutationVariant.RANDOM, 2);
        Animal chomik = new Animal(new Vector2D(2, 4), 10, genotype, simulationParameters);

        map.place(chomik);
        map.generatePlants(20);

        System.out.println(map.toString());
    }
}