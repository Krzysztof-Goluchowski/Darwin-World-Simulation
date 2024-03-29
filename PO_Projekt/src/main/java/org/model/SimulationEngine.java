package org.model;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SimulationEngine {
    private final List<Simulation> simulationList;
    private final ExecutorService executorService = Executors.newFixedThreadPool(4);

    public SimulationEngine(List<Simulation> simulationList){
        this.simulationList = simulationList;
    }

    public void runAsync() {
        for (Simulation simulation : simulationList) {
            executorService.submit(() -> {
                try {
                    simulation.run();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            });
        }

        // Poczekaj, aż wszystkie zadania zostaną zakończone
        executorService.shutdown();
    }

    public void pauseAllSimulations() {
        for (Simulation simulation : simulationList) {
            simulation.pause();
        }
    }

    public void resumeAllSimulations() {
        for (Simulation simulation : simulationList) {
            simulation.resume();
        }
    }
}
