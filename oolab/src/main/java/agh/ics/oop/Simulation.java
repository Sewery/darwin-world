package agh.ics.oop;

import agh.ics.oop.core.Configuration;
import agh.ics.oop.core.Statistics;
import agh.ics.oop.model.*;
import agh.ics.oop.model.animal_life.AgingAnimal;
import agh.ics.oop.model.animal_life.Animal;
import agh.ics.oop.model.util.Boundary;

import java.util.*;

import static java.lang.Math.max;

public class Simulation implements Runnable {

    private final List<Animal> animals;
    private final Map<List<Integer>, Integer> allGenotypes = new HashMap<>();
    private final WorldMap map;
    private final Statistics statistics;
    private final Integer genotypeLength;
    private int daysCount;
    private boolean running = true;
    private boolean paused = false;
    private int numberOfDeadAnimals = 0;
    private int totalAgeForDeadAnimals = 0;

    public Simulation(WorldMap map, Configuration config, Statistics statistics) {

        this.map = map;
        this.statistics = statistics;

        this.animals = new ArrayList<>();
        this.daysCount = 0;
        this.genotypeLength = config.genotypeLength();
        Boundary boundary = map.getCurrentBounds();
        int width = boundary.upperRight().getX() - boundary.lowerLeft().getX() + 1;
        int height = boundary.upperRight().getY() - boundary.lowerLeft().getY() + 1;

        //System.out.println(height);
        //System.out.println(width);
//        System.out.println(initialNumberOfAnimals);

        RandomPositionGenerator randomPositionGenerator = new RandomPositionGenerator(width, height, config.initialNumberOfAnimals());

        for (Vector2d animalPosition : randomPositionGenerator) {
            try {
                int[] randomGenotype = getRandomGenotype();
                allGenotypes.put(toList(randomGenotype), allGenotypes.getOrDefault(toList(randomGenotype), 0) + 1);
                Animal animal = (config.animalsBehaviourStrategy() == Configuration.AnimalsBehaviourStrategy.AGE_OF_BURDEN) ? new Animal(
                        animalPosition,
                        randomGenotype,
                        new HashSet<>(),
                        config.energyPerGrass(),
                        config.initialEnergyOfAnimals()
                ) : new AgingAnimal(
                        animalPosition,
                        randomGenotype,
                        new HashSet<>(),
                        config.energyPerGrass(),
                        config.initialEnergyOfAnimals()
                );
                map.place(animal);
                animals.add(animal);
            } catch (IncorrectPositionException e) {
                System.out.println(e.getMessage());
            }
        }

        this.running = true;
    }

    private static List<Integer> toList(int[] array) {
        List<Integer> list = new ArrayList<>();
        for (int num : array) {
            list.add(num);
        }
        return list;
    }

    private int[] getRandomGenotype() {
        Random random = new Random();
        int[] genotype = new int[genotypeLength];
        for (int i = 0; i < genotypeLength; i++) {
            genotype[i] = random.nextInt(MapDirection.values().length);
        }
        return genotype;
    }

    public synchronized void stopSimulation() {
        this.running = false;
        notify();
    }

    public synchronized void pauseSimulation() {
        map.notifyObservers("Simulation paused");
        this.paused = true;

    }

    public boolean isPaused() {
        return this.paused;
    }

    public synchronized void resumeSimulation() {
        this.paused = false;
        map.notifyObservers("Simulation resumed");
        notify();
    }

    public void run() {

        //System.out.println("Simulation started as: ");
        //System.out.println(map);
        if (animals.isEmpty()) return;

        // print genotypów
        //for (int i = 0; i < animals.size(); i++) {
        //    System.out.printf("Zwierze %s: %s, start with: %s %s\n", i, Arrays.toString(animals.get(i).getGenotype()), animals.get(i).getCurrentGene(), animals.get(i).getDirection());
        //}

        //System.out.println();

        while (this.running) {

            synchronized (this) {
                while (this.running && this.paused) {
                    try {
                        wait();
                    } catch (InterruptedException e) {
                        System.out.println("Exception: " + e.getMessage());
                    }
                }
            }

            sleep();
            removeDeadAnimals();

            sleep();
            moveAnimals();

            //System.out.printf("\nDay %s%n", daysCount);
            //System.out.println("After move: ");
            //for (int i = 0; i < animals.size(); i++) {
            //    System.out.printf("Zwierze %s: %s %s\n", i, animals.get(i).getEnergy(), animals.get(i).getPosition().toString());
            //}

            sleep();
            consumePlants();
            //System.out.println("After consume: ");
            //for (int i = 0; i < animals.size(); i++) {
            //    System.out.printf("Zwierze %s: %s %s\n", i, animals.get(i).getEnergy(), animals.get(i).getPosition().toString());
            //}


            sleep();
            reproduce();
            //System.out.println("After reproduce: ");
            //for (int i = 0; i < animals.size(); i++) {
            //    System.out.printf("Zwierze %s: %s %s\n", i, animals.get(i).getEnergy(), animals.get(i).getPosition().toString());
            //}

            sleep();
            growPlants();

            daysCount += 1;
        }


    }

    // SIMULATIONS STEPS
    private void removeDeadAnimals() {
        ArrayList<Animal> deadAnimals = new ArrayList<>();
        for (Animal animal : animals) {
            if (animal.getEnergy() == 0) {
                deadAnimals.add(animal);
                animal.setDead();
            }
            animal.increaseAge();
        }

        for (Animal deadAnimal : deadAnimals) {
            this.numberOfDeadAnimals++;
            this.totalAgeForDeadAnimals += deadAnimal.getAge();
            map.remove(deadAnimal);
            animals.remove(deadAnimal);
            allGenotypes.remove(toList(deadAnimal.getGenotype()));
        }
        map.notifyObservers("Day %s: remove dead animals".formatted(daysCount));

        statistics.updateNumberOfAllAnimals(getNumberOfAllAnimals());
        statistics.updateAverageLifespan(getAverageLifeSpan());
        statistics.updateAverageEnergy(getAverageEnergy());
        statistics.updateAverageNUmberOfChildren(getAverageNumberOfChildren());
        statistics.updateMostPopularGenotypes(getMostCommonGenotypes());
    }

    private void moveAnimals() {
        for (Animal animal : animals) {
            map.move(animal);
        }
        map.notifyObservers("Day %s: move animals".formatted(daysCount));
        statistics.updateAverageEnergy(getAverageEnergy());

    }

    private void growPlants() {
        map.growPlants(map.getNumberOfNewGrassesEachDay());
        map.notifyObservers("Day %s: grow plants".formatted(daysCount));

        statistics.updateNumberOfAllPlants(getNumberOfAllPlants());
        statistics.updateEmptySpaces(getNUmberOfEmptySpaces());
    }

    private void consumePlants() {
        map.consumePlants();
        map.notifyObservers("Day %s: consume plants".formatted(daysCount));

        statistics.updateNumberOfAllPlants(getNumberOfAllPlants());
        statistics.updateEmptySpaces(getNUmberOfEmptySpaces());
        statistics.updateAverageEnergy(getAverageEnergy());

    }

    private void reproduce() {
        List<Animal> createdAnimals = map.reproduce();

        //System.out.println(createdAnimals);

        for (Animal animal : createdAnimals) {
            //System.out.printf("Nowe zwierze: %s %s\n", animal.getEnergy(), animal.getPosition().toString());
            try {
                map.place(animal);
                animals.add(animal);
                allGenotypes.put(toList(animal.getGenotype()), allGenotypes.getOrDefault(toList(animal.getGenotype()), 0) + 1);
            } catch (IncorrectPositionException e) {
                System.out.println(e.getMessage());
            }
        }

        statistics.updateNumberOfAllAnimals(getNumberOfAllAnimals());
        statistics.updateAverageEnergy(getAverageEnergy());
        statistics.updateAverageNUmberOfChildren(getAverageNumberOfChildren());
        statistics.updateMostPopularGenotypes(getMostCommonGenotypes());
    }

    private void sleep() {
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            System.out.println("Exception: " + e.getMessage());
        }
    }

    // STATISTICS GETTERS
    public int getNumberOfAllAnimals() {
        //System.out.println("Animals size: " + animals.size());
        return animals.size();
    }

    public int getNumberOfAllPlants() {
        return map.getNumberOfGrasses();
    }

    public int getNUmberOfEmptySpaces() {
        return map.getNumberOfEmptySpaces();
    }

    public List<String> getMostCommonGenotypes() {
        int findMaxCount = 0;

        for (int count : allGenotypes.values()) {
            if (count > findMaxCount) {
                findMaxCount = count;
            }
        }

        final int maxCount = findMaxCount;

        //System.out.println(allGenotypes.keySet());

        return allGenotypes.entrySet()
                .stream()
                .filter(entry -> entry.getValue() == maxCount)
                .map(Map.Entry::getKey)
                .map(Object::toString)
                .toList();
    }

    public int getAverageEnergy() {
        if (animals.isEmpty()) {
            return 0;
        }
        int result = 0;

        for (Animal animal : animals) {
            result += animal.getEnergy();
        }
        return result / animals.size();
    }

    public int getAverageLifeSpan() {
        if (totalAgeForDeadAnimals == 0) {
            return 0;
        }
        return numberOfDeadAnimals / totalAgeForDeadAnimals;
    }

    public int getAverageNumberOfChildren() {
        if (animals.isEmpty()) {
            return 0;
        }
        int result = 0;
        for (Animal animal : animals) {
            result += animal.getNumberOfChildren();
        }
        return result / animals.size();
    }

    public int getMaxEnergy() {
        if (animals.isEmpty()) {
            return 0;
        }
        int result = 0;

        for (Animal animal : animals) {
            result = max(result, animal.getEnergy());
        }
        return result;
    }


}
