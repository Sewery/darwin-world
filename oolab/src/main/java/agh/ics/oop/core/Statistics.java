package agh.ics.oop.core;

import agh.ics.oop.model.util.StatisticsChangeListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Statistics {

    protected final List<StatisticsChangeListener> observers = new ArrayList<>();

    // statistics for the whole simulation
    private final List<Integer> numberOfAllAnimals;
    private final List<Integer> numberOfAllPlants;
    private final List<Integer> emptySpaces;
    private final List<Integer> averageEnergy;
    private final List<Integer> averageLifespan;
    private final List<Integer> averageNUmberOfChildren;
    private List<String> mostPopularGenotypes = new ArrayList<>();
    // statistics for chosen animal
    private List<Integer> genome;
    private Integer currentGene;
    private Integer plantsEaten;
    private Integer numberOfChildren;
    private Integer numberOfDescendants;
    private Integer age;

    public Statistics(Configuration configuration) {
        this.numberOfAllAnimals = new ArrayList<>();
        this.numberOfAllAnimals.add(configuration.initialNumberOfAnimals());
        this.numberOfAllPlants = new ArrayList<>();
        this.numberOfAllPlants.add(configuration.initialNumberOfGrasses());
        this.averageEnergy = new ArrayList<>();
        this.averageEnergy.add(configuration.initialEnergyOfAnimals());
        this.averageLifespan = new ArrayList<>();
        this.averageLifespan.add(0);
        this.averageNUmberOfChildren = new ArrayList<>();
        this.averageNUmberOfChildren.add(0);
        this.emptySpaces = new ArrayList<>();
        this.emptySpaces.add(configuration.height() * configuration.width() - configuration.initialNumberOfGrasses());

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

    public void updateStatistics(int numberOfAllAnimals, int numberOfAllPlants, int emptySpaces, List<String> mostPopularGenotypes, int averageEnergy, int averageLifespan, int averageNUmberOfChildren) {
        this.numberOfAllAnimals.add(numberOfAllAnimals);
        this.numberOfAllPlants.add(numberOfAllPlants);
        this.emptySpaces.add(emptySpaces);
        this.mostPopularGenotypes = mostPopularGenotypes;
        this.averageEnergy.add(averageEnergy);
        this.averageLifespan.add(averageLifespan);
        this.averageNUmberOfChildren.add(averageNUmberOfChildren);
        notifyObservers();
    }

    public void updateNumberOfAllAnimals(int numberOfAllAnimals) {
        this.numberOfAllAnimals.add(numberOfAllAnimals);
        notifyObservers();
    }

    public void updateNumberOfAllPlants(int numberOfAllPlants) {
        this.numberOfAllPlants.add(numberOfAllPlants);
        notifyObservers();
    }

    public void updateAverageEnergy(int averageEnergy) {
        this.averageEnergy.add(averageEnergy);
        notifyObservers();
    }

    public void updateAverageLifespan(int averageLifespan) {
        this.averageLifespan.add(averageLifespan);
        notifyObservers();
    }

    public void updateAverageNUmberOfChildren(int averageNUmberOfChildren) {
        this.averageNUmberOfChildren.add(averageNUmberOfChildren);
        notifyObservers();
    }

    public void updateMostPopularGenotypes(List<String> mostPopularGenotypes) {
        this.mostPopularGenotypes = mostPopularGenotypes;
        notifyObservers();
    }

    public void updateEmptySpaces(int emptySpaces) {
        this.emptySpaces.add(emptySpaces);
        notifyObservers();
    }

    public void updateHighlightedAnimal(int[] genome,
                                        int currentGene,
                                        int plantsEaten,
                                        int numberOfChildren,
                                        int numberOfDescendants,
                                        int age) {
        this.genome = Arrays.stream(genome).boxed().collect(Collectors.toList());
        this.currentGene = currentGene;
        this.plantsEaten = plantsEaten;
        this.numberOfChildren = numberOfChildren;
        this.numberOfDescendants = numberOfDescendants;
        this.age = age;
        notifyObservers();
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

    public void updateNumberOfChildren(int numberOfChildren) {
        this.numberOfChildren = numberOfChildren;
        notifyObservers();
    }

    public void updatePlantsEaten(int plantEaten) {
        this.plantsEaten = plantEaten;
    }

    public List<Integer> getNumberOfAllAnimals() {
        return numberOfAllAnimals;
    }

    public List<Integer> getNumberOfAllPlants() {
        return numberOfAllPlants;
    }

    public List<Integer> getEmptySpaces() {
        return emptySpaces;
    }

    public List<String> getMostPopularGenotypes() {
        return mostPopularGenotypes;
    }

    public List<Integer> getAverageEnergy() {
        return averageEnergy;
    }

    public List<Integer> getAverageLifespan() {
        return averageLifespan;
    }

    public List<Integer> getAverageNumberOfChildren() {
        return averageNUmberOfChildren;
    }
    public Integer getPlantsEaten() {
        return plantsEaten;
    }

    public Integer getNumberOfChildren() {
        return numberOfChildren;
    }

    public Integer getNumberOfDescendants() {
        return numberOfDescendants;
    }

    public Integer getAge() {
        return age;
    }

    public Integer getCurrentGene() {
        return currentGene;
    }

    public List<Integer> getGenome() {
        return genome;
    }
}
