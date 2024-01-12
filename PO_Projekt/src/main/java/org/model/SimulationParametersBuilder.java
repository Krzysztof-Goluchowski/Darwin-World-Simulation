package org.model;

public class SimulationParametersBuilder {
    private int startingAmountOfPlants;
    private int minReproduceEnergy;
    private int energyLostOnReproduction;
    private int minMutations;
    private int maxMutations;
    private int newPlantPerDay;
    private int energyLostPerDay;
    private int genotypeSize;
    private int plantEnergy;
    private SimulationParameters.MutationVariant mutationVariant;
    private SimulationParameters.MapVariant mapVariant;
    private int startingAnimalEnergy;

    public SimulationParametersBuilder startingAmountOfPlants(int startingAmountOfPlants) {
        this.startingAmountOfPlants = startingAmountOfPlants;
        return this;
    }

    public SimulationParametersBuilder minReproduceEnergy(int minReproduceEnergy) {
        this.minReproduceEnergy = minReproduceEnergy;
        return this;
    }

    public SimulationParametersBuilder energyLostOnReproduction(int energyLostOnReproduction) {
        this.energyLostOnReproduction = energyLostOnReproduction;
        return this;
    }

    public SimulationParametersBuilder minMutations(int minMutations) {
        this.minMutations = minMutations;
        return this;
    }

    public SimulationParametersBuilder maxMutations(int maxMutations) {
        this.maxMutations = maxMutations;
        return this;
    }

    public SimulationParametersBuilder newPlantPerDay(int newPlantPerDay) {
        this.newPlantPerDay = newPlantPerDay;
        return this;
    }

    public SimulationParametersBuilder energyLostPerDay(int energyLostPerDay) {
        this.energyLostPerDay = energyLostPerDay;
        return this;
    }

    public SimulationParametersBuilder genotypeSize(int genotypeSize) {
        this.genotypeSize = genotypeSize;
        return this;
    }

    public SimulationParametersBuilder plantEnergy(int plantEnergy) {
        this.plantEnergy = plantEnergy;
        return this;
    }

    public SimulationParametersBuilder mutationVariant(SimulationParameters.MutationVariant mutationVariant) {
        this.mutationVariant = mutationVariant;
        return this;
    }

    public SimulationParametersBuilder mapVariant(SimulationParameters.MapVariant mapVariant) {
        this.mapVariant = mapVariant;
        return this;
    }

    public SimulationParametersBuilder startingAnimalEnergy(int startingAnimalEnergy) {
        this.startingAnimalEnergy = startingAnimalEnergy;
        return this;
    }

    public SimulationParameters build() {
        return new SimulationParameters(
                startingAmountOfPlants, minReproduceEnergy, energyLostOnReproduction,
                minMutations, maxMutations, mutationVariant, mapVariant,
                plantEnergy, newPlantPerDay, energyLostPerDay, genotypeSize,
                startingAnimalEnergy
        );
    }
}
