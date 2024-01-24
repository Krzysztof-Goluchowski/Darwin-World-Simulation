package org.model;

import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MapDirectionTest {
    @Test
    public void testNewDirection(){
        Vector2D positionOfZwierzak = new Vector2D(10, 10);
        SimulationParameters simulationParameters = new SimulationParameters(10, 5, 5, 0, 0, SimulationParameters.MutationVariant.RANDOM, SimulationParameters.MapVariant.STANDARD, 2, 1, 5, 5, 10);
        List<Integer> genotype = new LinkedList<>();
        genotype.add(0);

        Animal zwierzak = new Animal(positionOfZwierzak, 10, genotype, simulationParameters);

        assertEquals(zwierzak.getOrientation(), MapDirection.NORTH);

        MapDirection directionEast = MapDirection.newDirection(zwierzak, 2);
        assertEquals(directionEast, MapDirection.EAST);

        MapDirection directionNorthEast = MapDirection.newDirection(zwierzak, 1);
        assertEquals(directionNorthEast, MapDirection.NORTH_EAST);

        MapDirection directionSouth = MapDirection.newDirection(zwierzak, 4);
        assertEquals(directionSouth, MapDirection.SOUTH);

        MapDirection directionSouthEast = MapDirection.newDirection(zwierzak, 3);
        assertEquals(directionSouthEast, MapDirection.SOUTH_EAST);

        MapDirection directionSouthWest = MapDirection.newDirection(zwierzak, 5);
        assertEquals(directionSouthWest, MapDirection.SOUTH_WEST);

        MapDirection directionWest = MapDirection.newDirection(zwierzak, 6);
        assertEquals(directionWest, MapDirection.WEST);

        MapDirection directionNorthWest = MapDirection.newDirection(zwierzak, 7);
        assertEquals(directionNorthWest, MapDirection.NORTH_WEST);

        zwierzak.setOrientation(MapDirection.WEST);
        MapDirection directionEast2 = MapDirection.newDirection(zwierzak, 4);
        assertEquals(directionEast2, MapDirection.EAST);

    }

    @Test
    public void testGetOrientationNumber(){
        Vector2D positionOfZwierzak = new Vector2D(10, 10);
        SimulationParameters simulationParameters = new SimulationParameters(10, 5, 5, 0, 0, SimulationParameters.MutationVariant.RANDOM, SimulationParameters.MapVariant.STANDARD, 2, 1, 5, 5, 10);
        List<Integer> genotype = new LinkedList<>();
        genotype.add(0);

        Animal zwierzak = new Animal(positionOfZwierzak, 10, genotype, simulationParameters);
        int orientationNorthNum = MapDirection.getOrientationNumber(zwierzak);
        assertEquals(orientationNorthNum, 0);

        zwierzak.setOrientation(MapDirection.EAST);
        int orientationEastNum = MapDirection.getOrientationNumber(zwierzak);
        assertEquals(orientationEastNum, 2);

        zwierzak.setOrientation(MapDirection.SOUTH);
        int orientationSouthNum = MapDirection.getOrientationNumber(zwierzak);
        assertEquals(orientationSouthNum, 4);

        zwierzak.setOrientation(MapDirection.WEST);
        int orientationWestNum = MapDirection.getOrientationNumber(zwierzak);
        assertEquals(orientationWestNum, 6);

        zwierzak.setOrientation(MapDirection.NORTH_EAST);
        int orientationNorthEastNum = MapDirection.getOrientationNumber(zwierzak);
        assertEquals(orientationNorthEastNum, 1);

        zwierzak.setOrientation(MapDirection.SOUTH_EAST);
        int orientationSouthEastNum = MapDirection.getOrientationNumber(zwierzak);
        assertEquals(orientationSouthEastNum, 3);

        zwierzak.setOrientation(MapDirection.SOUTH_WEST);
        int orientationSouthWestNum = MapDirection.getOrientationNumber(zwierzak);
        assertEquals(orientationSouthWestNum, 5);

        zwierzak.setOrientation(MapDirection.NORTH_WEST);
        int orientationNorthWestNum = MapDirection.getOrientationNumber(zwierzak);
        assertEquals(orientationNorthWestNum, 7);
    }

    @Test
    public void testToUnitVector(){
        assertEquals(MapDirection.NORTH.toUnitVector(), new Vector2D(0, 1));
        assertEquals(MapDirection.NORTH_EAST.toUnitVector(), new Vector2D(1, 1));
        assertEquals(MapDirection.EAST.toUnitVector(), new Vector2D(1, 0));
        assertEquals(MapDirection.SOUTH_EAST.toUnitVector(), new Vector2D(1, -1));
        assertEquals(MapDirection.SOUTH.toUnitVector(), new Vector2D(0, -1));
        assertEquals(MapDirection.SOUTH_WEST.toUnitVector(), new Vector2D(-1, -1));
        assertEquals(MapDirection.WEST.toUnitVector(), new Vector2D(-1, 0));
        assertEquals(MapDirection.NORTH_WEST.toUnitVector(), new Vector2D(-1, 1));
    }
}
