package agh.ics.oop.core;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Statistics {

    private final List<StatisticsChangeListener> observers = new ArrayList<>();
    // daily statistics for charts
    @Getter
    private final List<Integer> dailyNumberOfAllAnimals;
    @Getter
    private final List<Integer> dailyNumberOfAllPlants;
    @Getter
    private final List<Integer> dailyEmptySpaces;
    @Getter
    private final List<Integer> dailyAverageEnergy;
    @Getter
    private final List<Integer> dailyAverageLifespan;
    @Getter
    private final List<Integer> dailyAverageNumberOfChildren;
    @Getter
    @Setter
    private int numberOfDay;
    // statistics for the whole simulation
    @Getter
    private int numberOfAllAnimals;
    @Getter
    private int numberOfAllPlants;
    @Getter
    private int emptySpaces;
    @Getter
    private int averageEnergy;
    @Getter
    private int averageLifespan;
    @Getter
    private int averageNumberOfChildren;
    @Getter
    private List<String> mostPopularGenotypes = new ArrayList<>();
    // statistics for chosen animal
    @Getter
    private List<Integer> genome;
    @Getter
    private Integer currentGene;
    @Getter
    private Integer currentEnergy;
    @Getter
    private Integer plantsEaten;
    @Getter
    private Integer numberOfChildren;
    @Getter
    private Integer numberOfDescendants;
    @Getter
    private Integer age;
    @Getter
    private Integer dayOfDeath;

    public Statistics(Configuration configuration) {
        this.numberOfAllAnimals = configuration.initialNumberOfAnimals();
        this.numberOfAllPlants = configuration.initialNumberOfGrasses();
        this.averageEnergy = configuration.initialEnergyOfAnimals();
        this.averageLifespan = 0;
        this.averageNumberOfChildren = 0;
        this.emptySpaces = configuration.height() * configuration.width() - configuration.initialNumberOfGrasses();

        this.dailyNumberOfAllAnimals = new ArrayList<>();
        this.dailyNumberOfAllAnimals.add(configuration.initialNumberOfAnimals());
        this.dailyNumberOfAllPlants = new ArrayList<>();
        this.dailyNumberOfAllPlants.add(configuration.initialNumberOfGrasses());
        this.dailyAverageEnergy = new ArrayList<>();
        this.dailyAverageEnergy.add(configuration.initialEnergyOfAnimals());
        this.dailyAverageLifespan = new ArrayList<>();
        this.dailyAverageLifespan.add(0);
        this.dailyAverageNumberOfChildren = new ArrayList<>();
        this.dailyAverageNumberOfChildren.add(0);
        this.dailyEmptySpaces = new ArrayList<>();
        this.dailyEmptySpaces.add(configuration.height() * configuration.width() - configuration.initialNumberOfGrasses());

    }

    public void addObserver(StatisticsChangeListener statisticsChangeListener) {
        observers.add(statisticsChangeListener);
    }

    public void removeObserver(StatisticsChangeListener statisticsChangeListener) {
        observers.remove(statisticsChangeListener);
    }

    public void notifyObservers() {
        for (StatisticsChangeListener observer : observers) {
            observer.statisticsChanged(this);
        }
    }

    public void updateNumberOfDay(int numberOfDay) {
        this.numberOfDay = numberOfDay;
        notifyObservers();
    }

    public void updateDailyStatistics(int numberOfAllAnimals, int numberOfAllPlants, int emptySpaces, int averageEnergy, int averageLifespan, int averageNUmberOfChildren) {
        this.dailyNumberOfAllAnimals.add(numberOfAllAnimals);
        this.dailyNumberOfAllPlants.add(numberOfAllPlants);
        this.dailyEmptySpaces.add(emptySpaces);
        this.dailyAverageEnergy.add(averageEnergy);
        this.dailyAverageLifespan.add(averageLifespan);
        this.dailyAverageNumberOfChildren.add(averageNUmberOfChildren);
        notifyObservers();
    }

    public void updateNumberOfAllAnimals(int numberOfAllAnimals) {
        this.numberOfAllAnimals = numberOfAllAnimals;
        notifyObservers();
    }

    public void updateNumberOfAllPlants(int numberOfAllPlants) {
        this.numberOfAllPlants = numberOfAllPlants;
        notifyObservers();
    }

    public void updateAverageEnergy(int averageEnergy) {
        this.averageEnergy = averageEnergy;
        notifyObservers();
    }

    public void updateAverageLifespan(int averageLifespan) {
        this.averageLifespan = averageLifespan;
        notifyObservers();
    }

    public void updateAverageNumberOfChildren(int averageNUmberOfChildren) {
        this.averageNumberOfChildren = averageNUmberOfChildren;
        notifyObservers();
    }

    public void updateMostPopularGenotypes(List<String> mostPopularGenotypes) {
        this.mostPopularGenotypes = mostPopularGenotypes;
        notifyObservers();
    }

    public void updateEmptySpaces(int emptySpaces) {
        this.emptySpaces = emptySpaces;
        notifyObservers();
    }

    public void updateHighlightedAnimal(int[] genome,
                                        int currentGene,
                                        int currentEnergy,
                                        int plantsEaten,
                                        int numberOfChildren,
                                        int numberOfDescendants,
                                        int age) {
        this.genome = Arrays.stream(genome).boxed().collect(Collectors.toList());
        this.currentGene = currentGene;
        this.currentEnergy = currentEnergy;
        this.plantsEaten = plantsEaten;
        this.numberOfChildren = numberOfChildren;
        this.numberOfDescendants = numberOfDescendants;
        this.age = age;
        this.dayOfDeath = -1;
        notifyObservers();
    }

    public void resetHighlightedAnimal() {
        this.genome = null;
        this.currentGene = null;
        this.currentEnergy = null;
        this.plantsEaten = null;
        this.numberOfChildren = null;
        this.numberOfDescendants = null;
        this.age = null;
        this.dayOfDeath = -1;
    }

    public void updateNumberOfDescendants(int numberOfDescendants) {
        this.numberOfDescendants = numberOfDescendants;
        notifyObservers();
    }

    public void updateAge(int age) {
        this.age = age;
        notifyObservers();
    }

    public void updateCurrentGene(int gene) {
        this.currentGene = gene;
        notifyObservers();
    }

    public void updateCurrentEnergy(int currentEnergy) {
        this.currentEnergy = currentEnergy;
        notifyObservers();
    }

    public void updateNumberOfChildren(int numberOfChildren) {
        this.numberOfChildren = numberOfChildren;
        notifyObservers();
    }

    public void updatePlantsEaten(int plantEaten) {
        this.plantsEaten = plantEaten;
    }

    public void updateDayOfDeath(Integer dayOfDeath) {
        this.dayOfDeath = dayOfDeath;
    }

}
