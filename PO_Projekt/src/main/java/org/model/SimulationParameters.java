package org.model;

//Ta klasa bedzie przechowywac parametry charakterystyczne dla danej symulacji takie jak energie tracona przy rozmnazaniu itp.
public class SimulationParameters {
    private final int minReproduceEnergy;
    private final int energyLostOnReproduction;
    private final int minMutations;
    private final int maxMutations;

    private final int newPlantPerDay;
    public enum MutationVariant {
        RANDOM, SWAP
    }
    private final MutationVariant mutationVariant;
    private final int plantEnergy;

    public SimulationParameters(int minReproduceEnergy, int energyLostOnReproduction, int minMutations, int maxMutations, MutationVariant mutationVariant, int plantEnergy, int newPlantPerDay) {
        this.minReproduceEnergy = minReproduceEnergy;
        this.energyLostOnReproduction = energyLostOnReproduction;
        this.minMutations = minMutations;
        this.maxMutations = maxMutations;
        this.mutationVariant = mutationVariant;
        this.plantEnergy = plantEnergy;
        this.newPlantPerDay = newPlantPerDay;
    }

    //Gettery
    public int getMinReproduceEnergy() {
        return minReproduceEnergy;
    }

    public int getEnergyLostOnReproduction() {
        return energyLostOnReproduction;
    }

    public int getMinMutations() {
        return minMutations;
    }

    public int getMaxMutations() {
        return maxMutations;
    }

    public MutationVariant getMutationVariant() {
        return mutationVariant;
    }

    public int getPlantEnergy() {
        return plantEnergy;
    }

    public int getNewPlantPerDay() {
        return newPlantPerDay;
    }
}