package org.model;

public interface SimulationObserver {
    void update(int animalsCount, int plantsCount, double averageEnergy, double averageLifespan, int amountOfFreeSpot, int simulationDay,
                double averageNumberOfChildren, boolean isEnd);
}