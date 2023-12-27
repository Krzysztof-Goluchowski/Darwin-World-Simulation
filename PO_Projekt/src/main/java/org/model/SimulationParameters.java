package org.model;

//Ta klasa bedzie przechowywac parametry charakterystyczne dla danej symulacji takie jak energie tracona przy rozmnazaniu itp.
public class SimulationParameters {
    private int minReproduceEnergy;
    private int energyLostOnReproduction;
    private int maxGenValue;
    private int minMutations;
    private int maxMutations;
    private int mutationVariant;

    //Settery do ustawiania stalych
    public void setMinReproduceEnergy(int minReproduceEnergy) {
        this.minReproduceEnergy = minReproduceEnergy;
    }

    public void setEnergyLostOnReproduction(int energyLostOnReproduction) {
        this.energyLostOnReproduction = energyLostOnReproduction;
    }

    public void setMaxGenValue(int maxGenValue) {
        this.maxGenValue = maxGenValue;
    }

    public void setMinMutations(int minMutations) {
        this.minMutations = minMutations;
    }

    public void setMaxMutations(int maxMutations) {
        this.maxMutations = maxMutations;
    }

    public void setMutationVariant(int mutationVariant) {
        this.mutationVariant = mutationVariant;
    }

    //Gettery
    public int getMinReproduceEnergy() {
        return minReproduceEnergy;
    }

    public int getEnergyLostOnReproduction() {
        return energyLostOnReproduction;
    }

    public int getMaxGenValue() {
        return maxGenValue;
    }

    public int getMinMutations() {
        return minMutations;
    }

    public int getMaxMutations() {
        return maxMutations;
    }

    public int getMutationVariant() {
        return mutationVariant;
    }
}