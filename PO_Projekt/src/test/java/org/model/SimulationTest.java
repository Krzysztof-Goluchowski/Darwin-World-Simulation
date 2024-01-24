package org.model;

import org.junit.jupiter.api.Test;
import org.model.util.ConsoleMapDisplay;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class SimulationTest {

    @Test
    public void testPlace(){
        SimulationParameters simulationParameters = new SimulationParameters(10, 5, 5, 0, 0, SimulationParameters.MutationVariant.RANDOM, SimulationParameters.MapVariant.STANDARD, 2, 1, 5, 5, 10);

        List<Integer> genotype = new LinkedList<>();
        genotype.add(0);

        Board map = new Board(5, 5);

        ConsoleMapDisplay observer = new ConsoleMapDisplay();
        map.setObserver(observer);

        Animal chomik = new Animal(new Vector2D(2, 2), 10, genotype, simulationParameters);

        assertNotEquals(map.objectAt(new Vector2D(2, 2)), chomik);

        map.place(chomik);
        assertEquals(map.objectAt(new Vector2D(2, 2)), chomik);
    }

    @Test
    public void testMove(){
        SimulationParameters simulationParameters = new SimulationParameters(10, 5, 5, 0, 0, SimulationParameters.MutationVariant.RANDOM, SimulationParameters.MapVariant.STANDARD, 2, 1, 5, 5, 10);

        List<Integer> genotype = new LinkedList<>();
        genotype.add(0);

        Board map = new Board(5, 5);

        ConsoleMapDisplay observer = new ConsoleMapDisplay();
        map.setObserver(observer);

        Animal chomik = new Animal(new Vector2D(2, 2), 10, genotype, simulationParameters);

        map.place(chomik);

        map.move(chomik);
        assertEquals(chomik.getPosition(), new Vector2D(2, 3));
        assertEquals(map.objectAt(new Vector2D(2, 3)), chomik);
        assertNotEquals(map.objectAt(new Vector2D(2, 2)), chomik);
    }

    @Test
    public void testBoundaries(){
        SimulationParameters simulationParameters = new SimulationParameters(10, 5, 5, 0, 0, SimulationParameters.MutationVariant.RANDOM, SimulationParameters.MapVariant.STANDARD, 2, 1, 5, 5, 10);

        List<Integer> genotype = new LinkedList<>();
        genotype.add(0);

        Board map = new Board(5, 5);

        ConsoleMapDisplay observer = new ConsoleMapDisplay();
        map.setObserver(observer);

        Animal chomik = new Animal(new Vector2D(3, 2), 10, genotype, simulationParameters);
        chomik.setOrientation(MapDirection.EAST);

        map.move(chomik);
        assertEquals(chomik.getPosition(), new Vector2D(4,2));

        map.move(chomik);
        assertEquals(chomik.getPosition(), new Vector2D(0,2));

        map.move(chomik);
        assertEquals(chomik.getPosition(), new Vector2D(1,2));

        List<Integer> genotype2 = new LinkedList<>();
        genotype2.add(0);

        Animal chomik2 = new Animal(new Vector2D(1, 2), 10, genotype2, simulationParameters);
        chomik2.setOrientation(MapDirection.WEST);

        map.move(chomik2);
        assertEquals(chomik2.getPosition(), new Vector2D(0,2));

        map.move(chomik2);
        assertEquals(chomik2.getPosition(), new Vector2D(4,2));

        map.move(chomik2);
        assertEquals(chomik2.getPosition(), new Vector2D(3,2));

        List<Integer> genotype3 = new LinkedList<>();
        genotype3.add(0);

        Animal chomik3 = new Animal(new Vector2D(2, 3), 10, genotype3, simulationParameters);

        map.move(chomik3);
        assertEquals(chomik3.getPosition(), new Vector2D(2,4));

        map.move(chomik3);
        assertEquals(chomik3.getPosition(), new Vector2D(2,4));

        map.move(chomik3);
        assertEquals(chomik3.getPosition(), new Vector2D(2,3));

        List<Integer> genotype4 = new LinkedList<>();
        genotype4.add(0);

        Animal chomik4 = new Animal(new Vector2D(2, 1), 10, genotype4, simulationParameters);
        chomik4.setOrientation(MapDirection.SOUTH);

        map.move(chomik4);
        assertEquals(chomik4.getPosition(), new Vector2D(2,0));

        map.move(chomik4);
        assertEquals(chomik4.getPosition(), new Vector2D(2,0));

        map.move(chomik4);
        assertEquals(chomik4.getPosition(), new Vector2D(2,1));
    }

    @Test
    public void testConsumePlant(){
        SimulationParameters simulationParameters = new SimulationParameters(10, 5, 5, 0, 0, SimulationParameters.MutationVariant.RANDOM, SimulationParameters.MapVariant.STANDARD, 2, 1, 5, 5, 10);

        List<Integer> genotype = new LinkedList<>();
        genotype.add(0);

        Board map = new Board(5, 5);

        ConsoleMapDisplay observer = new ConsoleMapDisplay();
        map.setObserver(observer);

        Animal chomik = new Animal(new Vector2D(2, 2), 10, genotype, simulationParameters);

        map.place(chomik);

        map.move(chomik);

        chomik.setEnergy(chomik.getEnergy() - simulationParameters.getEnergyLostPerDay());

        assertEquals(chomik.getEnergy(), 5);

        Map<Vector2D, Plant> plantsMap = new HashMap<>();
        plantsMap.put(new Vector2D(2, 3), new Plant(new Vector2D(2, 3)));

        if (plantsMap.containsKey(chomik.getPosition())) {
            Vector2D animalPosition = chomik.getPosition();
            chomik.consumePlant();
            plantsMap.remove(animalPosition);
        }

        assertEquals(chomik.getEnergy(), 7);

    }

    @Test
    public void testReproduce(){
        SimulationParameters simulationParameters = new SimulationParameters(10, 5, 5, 0, 0, SimulationParameters.MutationVariant.RANDOM, SimulationParameters.MapVariant.STANDARD, 2, 1, 5, 5, 10);

        List<Integer> genotype = new LinkedList<>();
        genotype.add(0);

        Board map = new Board(5, 5);

        ConsoleMapDisplay observer = new ConsoleMapDisplay();
        map.setObserver(observer);

        Animal chomik1 = new Animal(new Vector2D(2, 2), 10, genotype, simulationParameters);
        Animal chomik2 = new Animal(new Vector2D(2, 2), 10, genotype, simulationParameters);

        map.place(chomik1);
        map.place(chomik2);

        List<Animal> animalList = new LinkedList<>();
        animalList.add(chomik1);
        animalList.add(chomik2);

        reproduceAnimals(animalList, map);

        Animal child = animalList.get(2);
        assertNotEquals(null, child);
    }

    private void reproduceAnimals(List<Animal> animalList, Board worldMap) {
        Map<Vector2D, List<Animal>> animalsByPosition = new HashMap<>();

        // Grupowanie zwierząt na podstawie ich pozycji
        for (Animal animal : animalList) {
            animalsByPosition.computeIfAbsent(animal.getPosition(), k -> new ArrayList<>()).add(animal);
        }

        // Przeszukiwanie każdej pozycji w poszukiwaniu par do rozmnażania
        for (List<Animal> animalsOnPosition : animalsByPosition.values()) {
            if (animalsOnPosition.size() >= 2) {
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
