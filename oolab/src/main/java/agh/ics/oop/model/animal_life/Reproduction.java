package agh.ics.oop.model.animal_life;

import agh.ics.oop.core.AppState;
import agh.ics.oop.model.Vector2d;

import java.util.*;

public class Reproduction {

    private final Animal parentOne;
    private final Animal parentTwo;
    private final Vector2d position;

    private final AnimalLife animalLife;


    public Reproduction(Animal animal1,
                        Animal animal2,
                        AnimalLife animalLife) {
        parentOne = animal1;
        parentTwo = animal2;
        position = animal1.getPosition();
        this.animalLife = animalLife;
    }

    private int[] createGenotype() {

        int energyOne = parentOne.getEnergy();
        int energyTwo = parentTwo.getEnergy();
        int totalEnergy = energyOne + energyTwo;

        int numberOfGenesFromParentOne = (animalLife.genotypeLength() * energyOne)/totalEnergy;
        int numberOfGenesFromParentTwo = animalLife.genotypeLength() - numberOfGenesFromParentOne;
        //System.out.println(numberOfGenesFromParentOne);
        //System.out.println(numberOfGenesFromParentTwo);

        int chooseSideForParentOne = new Random().nextInt(2);

        int[] childrenGenotype = new int[animalLife.genotypeLength()];

        switch (chooseSideForParentOne) {
            case 0: {
                System.arraycopy(parentOne.getGenotype(), 0, childrenGenotype, 0, numberOfGenesFromParentOne);
                System.arraycopy(parentTwo.getGenotype(), 0, childrenGenotype, numberOfGenesFromParentOne, numberOfGenesFromParentTwo);
            }
            case 1: {
                System.arraycopy(parentTwo.getGenotype(), 0, childrenGenotype, 0, numberOfGenesFromParentTwo);
                System.arraycopy(parentOne.getGenotype(), 0, childrenGenotype, numberOfGenesFromParentTwo, numberOfGenesFromParentOne);
            }
        }

        if (animalLife.minNumberOfMutations() == 0 && animalLife.maxNumberOfMutations() == 0) { return childrenGenotype; }
        return mutateGenotype(childrenGenotype);
    }

    private int randomGeneValue(int bannedNumber){
        List<Integer> possibleGenes =  new ArrayList<>();
        for (int i = 0; i <animalLife.genotypeLength(); i++) {
            if (i != bannedNumber) {
                possibleGenes.add(i);
            }
        }
        return possibleGenes.get(new Random().nextInt(possibleGenes.size()));
    }


    private int[] mutateGenotype(int[] genotype) {
        int numberOfMutations = new Random().nextInt(animalLife.maxNumberOfMutations() - animalLife.minNumberOfMutations() + 1) + animalLife.minNumberOfMutations();
        //System.out.println(Arrays.toString(genotype));
        //System.out.println(numberOfMutations);
        switch (numberOfMutations){
            case 0: return genotype;
            case 1: {
                int chosenGene = new Random().nextInt(animalLife.genotypeLength());
                genotype[chosenGene] = randomGeneValue(genotype[chosenGene]);
                genotype[chosenGene] += 1;
                //System.out.println(Arrays.toString(genotype));
                return genotype;
            }
            default: {
                List<Integer> indices = new ArrayList<>();
                for (int i = 0; i < animalLife.genotypeLength();i++) {
                    indices.add(i);
                }
                Collections.shuffle(indices);
                List<Integer> indicesToMutate = indices.subList(0, numberOfMutations);

                for (int i: indicesToMutate) {
                    genotype[i] = randomGeneValue(genotype[i]);
                }
                //System.out.println(Arrays.toString(genotype));
                return genotype;
            }
        }
    }

    public Animal createAChild(){
        //System.out.println(Arrays.toString(parentOne.getGenotype()) );
        //System.out.println(Arrays.toString(parentTwo.getGenotype()));
        parentOne.reproduce(animalLife.initialEnergyOfAnimals()/2);
        parentTwo.reproduce(animalLife.initialEnergyOfAnimals()-animalLife.initialEnergyOfAnimals()/2);

        Set<Animal> unionedAncestors = new HashSet<>(parentOne.getAncestors()); // Kopiujemy elementy z set1
        unionedAncestors.addAll(parentTwo.getAncestors());
        unionedAncestors.add(parentOne);
        unionedAncestors.add(parentTwo);

        for (Animal ancestor: unionedAncestors) {
            ancestor.increaseNumberOfDescendants();
        }

        return new Animal(
                this.position,
                createGenotype(),
                unionedAncestors,
                animalLife.energyPerGrass(),
                animalLife.initialEnergyOfAnimals()
        );
    }
}
