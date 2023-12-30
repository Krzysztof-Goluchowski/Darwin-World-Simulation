package org.model;

//Ta klasa bedzie przechowywac parametry charakterystyczne dla danej symulacji takie jak energie tracona przy rozmnazaniu itp.
public class SimulationParameters {
    private final int startingAmountOfPlants;
    private final int minReproduceEnergy;
    private final int energyLostOnReproduction;
    private final int minMutations;
    private final int maxMutations;
    private final int newPlantPerDay;
    private final int energyLostPerDay;
    private final int genotypeSize;
    private final int plantEnergy;

    public enum MutationVariant {
        RANDOM, SWAP
    }
    public enum MapVariant{
        STANDARD, TUNNELS
    }
    private final MutationVariant mutationVariant;
    private final MapVariant mapVariant;
    private final int startingAnimalEnergy;

    public SimulationParameters(int startingAmountOfPlants, int minReproduceEnergy, int energyLostOnReproduction, int minMutations, int maxMutations, MutationVariant mutationVariant, MapVariant mapVariant, int plantEnergy, int newPlantPerDay, int energyLostPerDay, int genotypeSize, int startingAnimalEnergy) {
        this.startingAmountOfPlants = startingAmountOfPlants;
        this.minReproduceEnergy = minReproduceEnergy;
        this.energyLostOnReproduction = energyLostOnReproduction;
        this.minMutations = minMutations;
        this.maxMutations = maxMutations;
        this.mutationVariant = mutationVariant;
        this.plantEnergy = plantEnergy;
        this.newPlantPerDay = newPlantPerDay;
        this.energyLostPerDay = energyLostPerDay;
        this.genotypeSize = genotypeSize;
        this.mapVariant = mapVariant;
        this.startingAnimalEnergy = startingAnimalEnergy;
    }

    //Gettery
    public int getMinReproduceEnergy() {
        return minReproduceEnergy;
    }

    public int getStartingAnimalEnergy() {
        return startingAnimalEnergy;
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

    public int getStartingAmountOfPlants() {
        return startingAmountOfPlants;
    }

    public int getEnergyLostPerDay() {
        return energyLostPerDay;
    }

    public int getGenotypeSize() {
        return genotypeSize;
    }

    public MapVariant getMapVariant() {
        return mapVariant;
    }
}