package org.model;

import java.util.ArrayList;
import java.util.List;

public class SimulationEngine {
    private List<Simulation> simulationList;
    private List<Thread> threadsList;

    public SimulationEngine(List<Simulation> simulationList){
        this.simulationList = simulationList;
        this.threadsList = new ArrayList<>();
    }

    public void runAsync() {
        for (Simulation simulation : simulationList){
            Thread thread = new Thread(() -> {
                try {
                    simulation.run();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            });
            thread.start();
            threadsList.add(thread);
        }
    }
}
