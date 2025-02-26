package agh.ics.oop;

import agh.ics.oop.model.core.Configuration;
import agh.ics.oop.model.core.Statistics;
import agh.ics.oop.model.*;
import agh.ics.oop.model.animal_life.AgingAnimal;
import agh.ics.oop.model.animal_life.Animal;
import agh.ics.oop.model.util.Boundary;
import lombok.Getter;

import java.util.*;

import static agh.ics.oop.tools.CSVWriter.*;
import static java.lang.Math.max;

public class Simulation implements Runnable {

    private static Integer fileCounter = 0;
    private final List<Animal> animals;
    private final Map<List<Integer>, Integer> allGenotypes = new HashMap<>();
    private final WorldMap map;
    @Getter
    private final Statistics stats;
    @Getter
    private final Configuration config;
    private final Boolean writeToFileStats;
    private final Integer genotypeLength;
    private int daysCount;
    private boolean running = true;
    @Getter
    private boolean paused = false;
    private int numberOfDeadAnimals = 0;
    private int totalAgeForDeadAnimals = 0;

    public Simulation(WorldMap map, Configuration config, Statistics stats, Boolean writeToFileStats) {

        this.map = map;
        this.stats = stats;
        this.config = config;
        this.writeToFileStats = writeToFileStats;
        this.animals = new ArrayList<>();
        this.daysCount = 0;
        this.genotypeLength = config.genotypeLength();
        Boundary boundary = map.getCurrentBounds();
        int width = boundary.upperRight().getX() - boundary.lowerLeft().getX() + 1;
        int height = boundary.upperRight().getY() - boundary.lowerLeft().getY() + 1;
        generateRandomPositionsForAnimals(width, height);
        this.running = true;
        if (writeToFileStats)
            fileCounter++;
    }

    private void generateRandomPositionsForAnimals(int width, int height) {
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
    }

    private static List<Integer> toList(int[] array) {
        List<Integer> list = new ArrayList<>();
        for (int num : array) {
            list.add(num);
        }
        return list;
    }

    List<Animal> getAnimals() {
        return Collections.unmodifiableList(animals);
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

    public synchronized void resumeSimulation() {
        this.paused = false;
        map.notifyObservers("Simulation resumed");
        notify();
    }

    public void run() {
        if (animals.isEmpty())
            return;
        if (writeToFileStats)
            writeStatisticsHeader( "stats-" + fileCounter + ".csv");

        while (this.running) {
            //Stop optimization
            stopOptimization();

            sleep();
            removeDeadAnimals();

            sleep();
            moveAnimals();

            sleep();
            consumePlants();

            sleep();
            reproduce();

            sleep();
            growPlants();

            statsEndingDayUpdate();
        }

    }
    private void stopOptimization() {
        synchronized (this) {
            while (this.running && this.paused) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    System.out.println("Exception: " + e.getMessage());
                }
            }
        }
    }
    private void statsEndingDayUpdate(){
        stats.updateNumberOfDay(daysCount);
        if (writeToFileStats)
            writeStatisticsLine(stats, "stats-" + fileCounter + ".csv");
        daysCount += 1;
        stats.updateDailyStatistics(getNumberOfAllAnimals(), getNumberOfAllPlants(), getNUmberOfEmptySpaces(), getAverageEnergy(), getAverageLifeSpan(), getAverageNumberOfChildren());
    }
    // SIMULATIONS STEPS
    void removeDeadAnimals() {
        ArrayList<Animal> deadAnimals = new ArrayList<>();
        for (Animal animal : animals) {
            if (animal.getEnergy() == 0) {
                deadAnimals.add(animal);
                animal.setDead(daysCount);
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

        stats.updateNumberOfAllAnimals(getNumberOfAllAnimals());
        stats.updateAverageLifespan(getAverageLifeSpan());
        stats.updateAverageEnergy(getAverageEnergy());
        stats.updateAverageNumberOfChildren(getAverageNumberOfChildren());
        stats.updateMostPopularGenotypes(getMostCommonGenotypes());
    }

    void moveAnimals() {
        for (Animal animal : animals) {
            map.move(animal);
        }
        map.notifyObservers("Day %s: move animals".formatted(daysCount));
        stats.updateAverageEnergy(getAverageEnergy());

    }

    void growPlants() {
        map.growPlants(map.getNumberOfNewGrassesEachDay());
        map.notifyObservers("Day %s: grow plants".formatted(daysCount));

        stats.updateNumberOfAllPlants(getNumberOfAllPlants());
        stats.updateEmptySpaces(getNUmberOfEmptySpaces());
    }

    void consumePlants() {
        map.consumePlants();
        map.notifyObservers("Day %s: consume plants".formatted(daysCount));

        stats.updateNumberOfAllPlants(getNumberOfAllPlants());
        stats.updateEmptySpaces(getNUmberOfEmptySpaces());
        stats.updateAverageEnergy(getAverageEnergy());

    }

     void reproduce() {
        List<Animal> createdAnimals = map.reproduce();

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

        stats.updateNumberOfAllAnimals(getNumberOfAllAnimals());
        stats.updateAverageEnergy(getAverageEnergy());
        stats.updateAverageNumberOfChildren(getAverageNumberOfChildren());
        stats.updateMostPopularGenotypes(getMostCommonGenotypes());
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
