package org.model;

import java.util.LinkedList;
import java.util.Random;

public class Animal implements WorldElement {
    private int energy;
    private Vector2D position;
    private MapDirection orientation;
    private LinkedList<Integer> genotype;
    private SimulationParameters parameters;

    public Animal(Vector2D position, int energy, LinkedList<Integer> genotype, SimulationParameters parameters){
        this.position = position;
        this.energy = energy;
        this.genotype = genotype;
        this.orientation = MapDirection.NORTH;
        this.parameters = parameters;
    }

    public int getEnergy() {
        return energy;
    }

    public Vector2D getPosition() {
        return position;
    }

    public MapDirection getOrientation() {
        return orientation;
    }

    public LinkedList<Integer> getGenotype() {
        return genotype;
    }


    public void move(Vector2D newPosition){
        this.position = newPosition;
    }

    public void consumePlant(Plant plant){
        this.energy += plant.getEnergy();
    }

    public boolean isReadyToReproduce() {
        return this.energy >= parameters.getMinReproduceEnergy();
    }

    //Zwraca dziecko
    public Animal reproduce(Animal partner) {
        this.energy -= parameters.getEnergyLostOnReproduction();
        partner.energy -= parameters.getEnergyLostOnReproduction();

        LinkedList<Integer> childGenotype = crossoverGenotype(this, partner);
        childGenotype = mutateGenotype(childGenotype);

        return new Animal(this.getPosition(), parameters.getEnergyLostOnReproduction(), childGenotype, this.parameters);
    }

    //Krzyzuje geny rodzicow
    private LinkedList<Integer> crossoverGenotype(Animal parent1, Animal parent2) {
        int totalEnergy = parent1.getEnergy() + parent2.getEnergy();
        int parent1Contribution = (parent1.getEnergy() * parent1.genotype.size()) / totalEnergy;

        LinkedList<Integer> childGenotype = new LinkedList<>();
        Random rand = new Random();

        boolean startFromLeft = rand.nextBoolean();

        if (startFromLeft) {
            childGenotype.addAll(parent1.genotype.subList(0, parent1Contribution));
            childGenotype.addAll(parent2.genotype.subList(parent1Contribution, parent2.genotype.size()));
        } else {
            childGenotype.addAll(parent1.genotype.subList(parent1.genotype.size() - parent1Contribution, parent1.genotype.size()));
            childGenotype.addAll(parent2.genotype.subList(0, parent2.genotype.size() - parent1Contribution));
        }
        return childGenotype;
    }

    //Mutacje
    private LinkedList<Integer> mutateGenotype(LinkedList<Integer> genotype) {
        Random rand = new Random();

        if (parameters.getMutationVariant() == 0){ //losowa liczba (wybranych również losowo) genów potomka zmienia swoje wartości na zupełnie nowe
            int numberOfMutations = rand.nextInt(genotype.size());

            for (int i = 0; i < numberOfMutations; i++) {
                int geneIndex = rand.nextInt(genotype.size());
                int newGeneValue = rand.nextInt(8);
                genotype.set(geneIndex, newGeneValue);
            }
        }

        if (parameters.getMutationVariant() == 1) { //zmienia jeden gen na dowolny inny gen
            int geneIndex = rand.nextInt(genotype.size());
            int newGeneValue = rand.nextInt(parameters.getMaxGenValue() + 1);
            genotype.set(geneIndex, newGeneValue);
        }
        if (parameters.getMutationVariant() == 2) { // podmianka - mutacja może też skutkować tym, że dwa geny zamienią się miejscami
            int firstGeneIndex = rand.nextInt(genotype.size());
            int secondGeneIndex = rand.nextInt(genotype.size());

            while (secondGeneIndex == firstGeneIndex){
                secondGeneIndex = rand.nextInt(genotype.size());
            }
            int firstGeneValue = genotype.get(firstGeneIndex);
            int secondGeneValue = genotype.get(secondGeneIndex);

            genotype.set(firstGeneIndex, secondGeneValue);
            genotype.set(secondGeneIndex, firstGeneValue);
        }

        return genotype;
    }
}
