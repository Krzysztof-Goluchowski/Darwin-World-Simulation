package org.model;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class Animal implements WorldElement, Comparable<Animal> {
    private int energy;
    private int daysSurvived = 0;
    private List<Animal> parents;
    private int amountOfChildren = 0;
    private int howManyPlantsEaten = 0;
    private int dayOfDeath;
    private Vector2D position;
    private MapDirection orientation;
    private final List<Integer> genotype;
    private final SimulationParameters params;

//    public Animal(Vector2D position, int energy, List<Integer> genotype, SimulationParameters parameters, List<Animal> parents){
//        this.position = position;
//        this.energy = energy;
//        this.genotype = genotype;
//        this.orientation = MapDirection.NORTH;
//        this.params = parameters;
//        this.parents = parents;
//    }

    //Adam i Ewa
    public Animal(SimulationParameters parameters){
        this.params = parameters;
        this.energy = parameters.getStartingAnimalEnergy();
        this.genotype = generateNewGenotype();
        this.orientation = MapDirection.NORTH;
    }

    public Animal(Vector2D position, int energy, List<Integer> genotype, SimulationParameters parameters){
        this.position = position;
        this.energy = energy;
        this.genotype = genotype;
        this.orientation = MapDirection.NORTH;
        this.params = parameters;
    }

    //Konstruktor do zliczania potomkow
    public Animal(Vector2D position, int energy, List<Integer> genotype, SimulationParameters parameters, List<Animal> parents){
        this.position = position;
        this.energy = energy;
        this.genotype = genotype;
        this.orientation = MapDirection.NORTH;
        this.params = parameters;
        this.parents = parents;
    }

    public void setOrientation(MapDirection orientation) {
        this.orientation = orientation;
    }

    public void setEnergy(int energy) {
        this.energy = energy;
    }

    public void setDaysSurvived(int daysSurvived) {
        this.daysSurvived = daysSurvived;
    }

    public void setPosition(Vector2D position) {
        this.position = position;
    }

    public void setDayOfDeath(int dayOfDeath) {
        this.dayOfDeath = dayOfDeath;
    }

    public int getDaysSurvived() {
        return daysSurvived;
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

    public List<Integer> getGenotype() {
        return genotype;
    }


    public void move(Vector2D newPosition){
        this.position = newPosition;
    }

    public void consumePlant(Plant plant){
        this.energy += params.getPlantEnergy();
        howManyPlantsEaten++;
    }

    public boolean isReadyToReproduce() {
        return this.energy > params.getMinReproduceEnergy();
    }

    //Zwraca dziecko
    public Animal reproduce(Animal partner) {
        this.energy -= params.getEnergyLostOnReproduction();
        partner.energy -= params.getEnergyLostOnReproduction();

        List<Animal> childParents = List.of(this, partner);
        increaseChildrenCountForAncestors(childParents);

        LinkedList<Integer> childGenotype = crossoverGenotype(this, partner);
        mutateGenotype(childGenotype);

        return new Animal(this.getPosition(), params.getEnergyLostOnReproduction(), childGenotype, this.params, childParents);
    }

    //Zwieksza ilosc potomkow dla kazdego ze zwierzat
    private void increaseChildrenCountForAncestors(List<Animal> ancestors) {
        if (ancestors == null) {
            return;
        }
        for (Animal ancestor : ancestors) {
            ancestor.amountOfChildren++;
            increaseChildrenCountForAncestors(ancestor.parents);
        }
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
    private void mutateGenotype(LinkedList<Integer> genotype) {
        Random rand = new Random();

        if (params.getMutationVariant() == SimulationParameters.MutationVariant.RANDOM){ //losowa liczba (wybranych również losowo) genów potomka zmienia swoje wartości na zupełnie nowe
            int numberOfMutations = rand.nextInt((params.getMaxMutations() - params.getMinMutations()) + 1) + params.getMinMutations();

            for (int i = 0; i < numberOfMutations; i++) {
                int geneIndex = rand.nextInt(genotype.size());
                int currentGeneValue = genotype.get(geneIndex);
                int newGeneValue;
                do {
                    newGeneValue = rand.nextInt(8);
                } while (newGeneValue == currentGeneValue);
                genotype.set(geneIndex, newGeneValue);
            }
        }

        if (params.getMutationVariant() == SimulationParameters.MutationVariant.SWAP) { // podmianka - mutacja może też skutkować tym, że dwa geny zamienią się miejscami
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

    }

    public int nextMove() {
        int move = this.genotype.remove(0);
        this.genotype.add(move);
        return move;
    }

    public List<Integer> generateNewGenotype(){
        int numOfGens = params.getGenotypeSize();
        LinkedList<Integer> newGenotype = new LinkedList<>();
        for (int i = 0; i < numOfGens; i++){
            int random = new Random().nextInt(8);
            newGenotype.add(random);
        }
        return newGenotype;
    }

    @Override
    public int compareTo(Animal other) {
        // Pierwszeństwo mają organizmy o największej energii
        if (this.energy != other.energy) {
            return Integer.compare(other.energy, this.energy);
        }

        // Jeżeli nie pozwala to rozstrzygnąć, to pierwszeństwo mają organizmy najstarsze
        if (this.daysSurvived != other.daysSurvived) {
            return Integer.compare(other.daysSurvived, this.daysSurvived);
        }

        // Jeżeli to nie pozwala rozstrzygnąć, to pierwszeństwo mają organizmy o największej liczbie dzieci
        if (this.amountOfChildren != other.amountOfChildren) {
            return Integer.compare(other.amountOfChildren, this.amountOfChildren);
        }

        // Jeżeli to nie pozwala rozstrzygnąć, to wybieramy losowo
        return new Random().nextInt(2) == 0 ? -1 : 1;
    }

    public String toString() {
        return "\u25A0"; // To jest znak kwadratu w Unicode
    }
}