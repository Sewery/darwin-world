package agh.ics.oop.core;

import agh.ics.oop.Simulation;
import agh.ics.oop.model.WorldMap;
import agh.ics.oop.model.util.MapChangeListener;
import agh.ics.oop.model.util.StatisticsChangeListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Statistics {

    protected final List<StatisticsChangeListener> observers = new ArrayList<>();

    // statistics for the whole simulation
    private final List<Integer> numberOfAllAnimals;
    private final List<Integer> numberOfAllPlants;
    private final List<Integer> emptySpaces;
    private List<String> mostPopularGenotypes = new ArrayList<>();
    private final List<Integer> averageEnergy;
    private final List<Integer> averageLifespan;
    private final List<Integer> averageNUmberOfChildren;

    // statistics for chosen animal
    private int[] genome;
    private int currentGene;
    private int plantEaten;
    private int numberOfChildren;
    private int numberOfDescendants;
    private int age;

    public Statistics(Configuration configuration) {
        this.numberOfAllAnimals = new ArrayList<>();
        this.numberOfAllAnimals.add(configuration.initialNumberOfAnimals());
        this.numberOfAllPlants = new ArrayList<>();
        this.numberOfAllPlants.add(configuration.initialNumberOfGrasses());
        this.averageEnergy = new ArrayList<>();
        this.averageEnergy.add(configuration.initialEnergyOfAnimals());
        this.averageLifespan = new ArrayList<>();
        this.averageNUmberOfChildren = new ArrayList<>();
        this.averageNUmberOfChildren.add(0);
        this.emptySpaces = new ArrayList<>();
        this.emptySpaces.add(configuration.height()*configuration.width()- configuration.initialNumberOfGrasses());

    }

    public void addObserver(StatisticsChangeListener statisticsChangeListener) {observers.add(statisticsChangeListener);}
    public void removeObserver(StatisticsChangeListener statisticsChangeListener) {observers.remove(statisticsChangeListener);}
    public void notifyObservers() {
        for (StatisticsChangeListener observer : observers) {observer.statisticsChanged(this);}
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

    public List<Integer> getAverageNUmberOfChildren() {
        return averageNUmberOfChildren;
    }
}
