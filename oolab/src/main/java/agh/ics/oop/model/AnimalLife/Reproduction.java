package agh.ics.oop.model.AnimalLife;

import agh.ics.oop.model.Vector2d;

import java.util.*;

public class Reproduction {

    private final Animal parentOne;
    private final Animal parentTwo;
    private final Vector2d position;

    private static int MIN_NUMBER_OF_MUTATIONS;
    private static int MAX_NUMBER_OF_MUTATIONS;

    public static void setMinNumberOfMutations(int minNumberOfMutations) {
        MIN_NUMBER_OF_MUTATIONS = minNumberOfMutations;
    }

    public static void setMaxNumberOfMutations(int maxNumberOfMutations) {
        MAX_NUMBER_OF_MUTATIONS = maxNumberOfMutations;
    }


    public Reproduction(Animal animal1, Animal animal2) {
        parentOne = animal1;
        parentTwo = animal2;
        position = animal1.getPosition();
    }

    private int[] createGenotype() {

        int energyOne = parentOne.getEnergy();
        int energyTwo = parentTwo.getEnergy();
        int totalEnergy = energyOne + energyTwo;

        int numberOfGenesFromParentOne = (Animal.getGenotypeLength() * energyOne)/totalEnergy;
        int numberOfGenesFromParentTwo = Animal.getGenotypeLength() - numberOfGenesFromParentOne;
        System.out.println(numberOfGenesFromParentOne);
        System.out.println(numberOfGenesFromParentTwo);

        int chooseSideForParentOne = new Random().nextInt(2);

        int[] childsGenotype = new int[Animal.getGenotypeLength()];

        switch (chooseSideForParentOne) {
            case 0: {
                System.arraycopy(parentOne.getGenotype(), 0, childsGenotype, 0, numberOfGenesFromParentOne);
                System.arraycopy(parentTwo.getGenotype(), 0, childsGenotype, numberOfGenesFromParentOne, numberOfGenesFromParentTwo);
            }
            case 1: {
                System.arraycopy(parentTwo.getGenotype(), 0, childsGenotype, 0, numberOfGenesFromParentTwo);
                System.arraycopy(parentOne.getGenotype(), 0, childsGenotype, numberOfGenesFromParentTwo, numberOfGenesFromParentOne);
            }
        }

        if (MIN_NUMBER_OF_MUTATIONS == 0 && MAX_NUMBER_OF_MUTATIONS == 0) { return childsGenotype; }
        return mutateGenotype(childsGenotype);
    }

    private int randomGeneValue(int bannedNumber){
        List<Integer> possibleGenes =  new ArrayList<>();
        for (int i = 0; i < Animal.getGenotypeLength(); i++) {
            if (i != bannedNumber) {
                possibleGenes.add(i);
            }
        }
        return possibleGenes.get(new Random().nextInt(possibleGenes.size()));
    }


    private int[] mutateGenotype(int[] genotype) {
        int numberOfMutations = new Random().nextInt(MAX_NUMBER_OF_MUTATIONS - MIN_NUMBER_OF_MUTATIONS + 1) + MIN_NUMBER_OF_MUTATIONS;
        System.out.println(Arrays.toString(genotype));
        System.out.println(numberOfMutations);
        switch (numberOfMutations){
            case 0: return genotype;
            case 1: {
                int chosenGene = new Random().nextInt(Animal.getGenotypeLength());
                genotype[chosenGene] = randomGeneValue(genotype[chosenGene]);
                genotype[chosenGene] += 1;
                System.out.println(Arrays.toString(genotype));
                return genotype;
            }
            default: {
                List<Integer> indices = new ArrayList<>();
                for (int i = 0; i < Animal.getGenotypeLength(); i++) {
                    indices.add(i);
                }
                Collections.shuffle(indices);
                List<Integer> indicesToMutate = indices.subList(0, numberOfMutations);

                for (int i: indicesToMutate) {
                    genotype[i] = randomGeneValue(genotype[i]);
                }
                System.out.println(Arrays.toString(genotype));
                return genotype;
            }
        }
    }

    public Animal createAChild(){

        System.out.println(Arrays.toString(parentOne.getGenotype()) );
        System.out.println(Arrays.toString(parentTwo.getGenotype()));
        parentOne.reproduce(Animal.getInitialEnergy()/2);
        parentTwo.reproduce(Animal.getInitialEnergy()-Animal.getInitialEnergy()/2);
        return new Animal(this.position, createGenotype());

    }
}
