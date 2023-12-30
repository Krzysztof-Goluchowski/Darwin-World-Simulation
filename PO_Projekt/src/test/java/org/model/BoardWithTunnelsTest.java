package org.model;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.model.BoardWithTunnels;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class BoardWithTunnelsTest {
    @Test
    public void testMove(){
        SimulationParameters simulationParameters = new SimulationParameters(10, 5, 5, 0, 0, SimulationParameters.MutationVariant.RANDOM, SimulationParameters.MapVariant.TUNNELS, 2, 1, 5, 5, 10);

        List<Integer> genotype = new LinkedList<>();
        genotype.add(0);

        BoardWithTunnels map = new BoardWithTunnels(5, 5, 0);
        Animal chomik = new Animal(new Vector2D(0, 0), 10, genotype, simulationParameters);

        Vector2D tunnelEnter = new Vector2D(0, 1);
        Vector2D tunnelExit = new Vector2D(3,3);
        Map<Vector2D, Vector2D> tunnelsMaps = new HashMap<>();
        tunnelsMaps.put(tunnelEnter, tunnelExit);
        tunnelsMaps.put(tunnelExit, tunnelEnter);

        map.setTunnelsMaps(tunnelsMaps);

        map.place(chomik);

        map.move(chomik);
        assertEquals(chomik.getPosition(), new Vector2D(0, 1));

        map.move(chomik);
        assertEquals(map.objectAt(new Vector2D(3, 4)), chomik);
        assertNotEquals(map.objectAt(new Vector2D(0, 2)), chomik);
    }
}
