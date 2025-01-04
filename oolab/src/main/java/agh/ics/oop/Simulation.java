package agh.ics.oop;

import agh.ics.oop.core.Configuration;
import agh.ics.oop.model.*;
import agh.ics.oop.model.animal_life.Animal;
import agh.ics.oop.model.animal_life.Reproduction;
import agh.ics.oop.model.util.Boundary;

import java.util.*;

public class Simulation implements Runnable {

    private final List<Animal> animals;
    private final Map<int[], Integer> allGenotypes = new HashMap<>();
    private final WorldMap map;
    private int daysCount;

    private final Integer genotypeLength;

    public Simulation(WorldMap map, Configuration config) {

        this.map = map;

        this.animals = new ArrayList<>();
        this.daysCount = 0;
        this.genotypeLength = config.genotypeLength();
        Boundary boundary = map.getCurrentBounds();
        int width = boundary.upperRight().getX() - boundary.lowerLeft().getX()+1;
        int height = boundary.upperRight().getY() - boundary.lowerLeft().getY()+1;

        Reproduction.setMaxNumberOfMutations(config.maxNumberOfMutations());
        Reproduction.setMinNumberOfMutations(config.minNumberOfMutations());

        System.out.println(height);
        System.out.println(width);
//        System.out.println(initialNumberOfAnimals);

        RandomPositionGenerator randomPositionGenerator = new RandomPositionGenerator(width, height, config.initialEnergyOfAnimals());

        for(Vector2d animalPosition : randomPositionGenerator) {
            try
            {
                int[] randomGenotype = getRandomGenotype();
                allGenotypes.put(randomGenotype, allGenotypes.getOrDefault(randomGenotype, 0) + 1);
                Animal animal = new Animal(animalPosition, randomGenotype);
                map.place(animal);
                animals.add(animal);
            } catch (IncorrectPositionException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private int[] getRandomGenotype(){
        Random random = new Random();
        int[] genotype = new int[genotypeLength];
        for (int i = 0; i < genotypeLength; i++) {
            genotype[i] = random.nextInt(MapDirection.values().length);
        }
        return genotype;
    }


    public void run() {

        System.out.println("Simulation started as: ");
        System.out.println(map);
        if (animals.isEmpty())  return;

        // print genotypów
        for (int i = 0; i < animals.size(); i++) {
            System.out.printf("Zwierze %s: %s, start with: %s %s\n", i, Arrays.toString(animals.get(i).getGenotype()), animals.get(i).getCurrentGene(), animals.get(i).getDirection());
        }

        System.out.println();

        while (daysCount < 100) {

            sleep();
            removeDeadAnimals();

            sleep();
            moveAnimals();

            System.out.printf("\nDay %s%n", daysCount);
            System.out.println("After move: ");
            for (int i = 0; i < animals.size(); i++) {
                System.out.printf("Zwierze %s: %s %s\n", i, animals.get(i).getEnergy(), animals.get(i).getPosition().toString());
            }

            sleep();
            consumePlants();
            System.out.println("After consume: ");
            for (int i = 0; i < animals.size(); i++) {
                System.out.printf("Zwierze %s: %s %s\n", i, animals.get(i).getEnergy(), animals.get(i).getPosition().toString());
            }


            sleep();
            reproduce();
            System.out.println("After reproduce: ");
            for (int i = 0; i < animals.size(); i++) {
                System.out.printf("Zwierze %s: %s %s\n", i, animals.get(i).getEnergy(), animals.get(i).getPosition().toString());
            }

            sleep();
            growPlants();

            System.out.printf("Most common genotype: %s\n", getMostCommonGenotypes());

            daysCount += 1;
        }


    }

    private void removeDeadAnimals(){
        ArrayList<Animal> deadAnimals = new ArrayList<>();
        for (Animal animal : animals) {
            if (animal.getEnergy() == 0) {
                deadAnimals.add(animal);
                animal.setDead();
            }
            animal.increaseAge();
        }

        for (Animal deadAnimal : deadAnimals) {
            map.remove(deadAnimal);
            animals.remove(deadAnimal);
        }
        map.notifyObservers("Day %s: remove dead animals".formatted(daysCount));
    }

    private void moveAnimals(){
        for (Animal animal : animals) {
            map.move(animal);
        }
        map.notifyObservers("Day %s: move animals".formatted(daysCount));
    }

    private void growPlants(){
        map.growPlants(map.getNumberOfNewGrassesEachDay());
        map.notifyObservers("Day %s: grow plants".formatted(daysCount));
    }

    private void sleep(){
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            System.out.println("Exception: " + e.getMessage());
        }
    }

    private void consumePlants(){
        map.consumePlants();
        map.notifyObservers("Day %s: consume plants".formatted(daysCount));


    }

    private void reproduce(){
        List<Animal> createdAnimals = map.reproduce();

        System.out.println(createdAnimals);

        for (Animal animal : createdAnimals) {
            System.out.printf("Nowe zwierze: %s %s\n", animal.getEnergy(), animal.getPosition().toString());
            try
            {
                map.place(animal);
                animals.add(animal);
                allGenotypes.put(animal.getGenotype(), allGenotypes.getOrDefault(animal.getGenotype(), 0) + 1);
            } catch (IncorrectPositionException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private List<int[]> getMostCommonGenotypes(){
        int findMaxCount = 0;

        for (int count : allGenotypes.values()) {
            if (count > findMaxCount) {
                findMaxCount = count;
            }
        }

        final int maxCount = findMaxCount;

        return allGenotypes.entrySet()
                                            .stream()
                                            .filter(entry -> entry.getValue() == maxCount)
                                            .map(Map.Entry::getKey)
                                            .toList();
    }
}
