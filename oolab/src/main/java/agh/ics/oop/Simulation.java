package agh.ics.oop;

import agh.ics.oop.model.*;
import agh.ics.oop.model.AnimalLife.Animal;
import agh.ics.oop.model.AnimalLife.Reproduction;
import agh.ics.oop.model.util.Boundary;

import java.util.*;
import java.util.stream.Collectors;

public class Simulation implements Runnable {

    private final List<Animal> animals;
    private final Map<int[], Integer> allGenotypes = new HashMap<>();
    private final WorldMap map;
    private int daysCount;


    public Simulation(WorldMap map, int initialNumberOfAnimals, int initialEnergy, int GENOTYPE_LENGTH, int AGE_OF_BURDEN, int MIN_ENERGY_TO_REPRODUCE, int ENERGY_GIVEN_BY_ONE_GRASS, int MIN_NUMBER_OF_MUTATIONS, int MAX_NUMBER_OF_MUTATIONS) {

        this.map = map;

        this.animals = new ArrayList<>();
        this.daysCount = 0;

        Boundary boundary = map.getCurrentBounds();
        int width = boundary.upperRight().getX() - boundary.lowerLeft().getX()+1;
        int height = boundary.upperRight().getY() - boundary.lowerLeft().getY()+1;

        Animal.set_MIN_ENERGY_TO_REPRODUCE(MIN_ENERGY_TO_REPRODUCE);
        Animal.set_AGE_OF_BURDEN(AGE_OF_BURDEN);
        Animal.set_GENOTYPE_LENGTH(GENOTYPE_LENGTH);
        Animal.set_ENERGY_GIVEN_BY_ONE_GRASS(ENERGY_GIVEN_BY_ONE_GRASS);
        Animal.set_INITIAL_ENERGY(initialEnergy);

        Reproduction.setMaxNumberOfMutations(MAX_NUMBER_OF_MUTATIONS);
        Reproduction.setMinNumberOfMutations(MIN_NUMBER_OF_MUTATIONS);

        System.out.println(height);
        System.out.println(width);
        System.out.println(initialNumberOfAnimals);

        RandomPositionGenerator randomPositionGenerator = new RandomPositionGenerator(width, height, initialNumberOfAnimals);

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
        int[] genotype = new int[Animal.getGenotypeLength()];
        for (int i = 0; i < Animal.getGenotypeLength(); i++) {
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
