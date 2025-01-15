package agh.ics.oop.core;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Statistics {

    protected final List<StatisticsChangeListener> observers = new ArrayList<>();
    @Getter
    @Setter
    private int numberOfDay;
    // statistics for the whole simulation
    private final List<Integer> numberOfAllAnimals;
    private final List<Integer> numberOfAllPlants;
    private final List<Integer> emptySpaces;
    private final List<Integer> averageEnergy;
    private final List<Integer> averageLifespan;
    private final List<Integer> averageNumberOfChildren;
    private List<String> mostPopularGenotypes = new ArrayList<>();
    // statistics for chosen animal
    private List<Integer> genome;
    private Integer currentGene;
    private Integer currentEnergy;
    private Integer plantsEaten;
    private Integer numberOfChildren;
    private Integer numberOfDescendants;
    private Integer age;
    private Integer dayOfDeath;

    public Statistics(Configuration configuration) {
        this.numberOfAllAnimals = new ArrayList<>();
        this.numberOfAllAnimals.add(configuration.initialNumberOfAnimals());
        this.numberOfAllPlants = new ArrayList<>();
        this.numberOfAllPlants.add(configuration.initialNumberOfGrasses());
        this.averageEnergy = new ArrayList<>();
        this.averageEnergy.add(configuration.initialEnergyOfAnimals());
        this.averageLifespan = new ArrayList<>();
        this.averageLifespan.add(0);
        this.averageNumberOfChildren = new ArrayList<>();
        this.averageNumberOfChildren.add(0);
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
    public void updateNumberOfDay(int numberOfDay) {
        this.numberOfDay = numberOfDay;
        notifyObservers();
    }
    public void updateStatistics(int numberOfAllAnimals, int numberOfAllPlants, int emptySpaces, List<String> mostPopularGenotypes, int averageEnergy, int averageLifespan, int averageNUmberOfChildren) {
        this.numberOfAllAnimals.add(numberOfAllAnimals);
        this.numberOfAllPlants.add(numberOfAllPlants);
        this.emptySpaces.add(emptySpaces);
        this.mostPopularGenotypes = mostPopularGenotypes;
        this.averageEnergy.add(averageEnergy);
        this.averageLifespan.add(averageLifespan);
        this.averageNumberOfChildren.add(averageNUmberOfChildren);
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

    public void updateAverageNumberOfChildren(int averageNUmberOfChildren) {
        this.averageNumberOfChildren.add(averageNUmberOfChildren);
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
        this.genome =  null;
        this.currentGene =  null;
        this.currentEnergy =  null;
        this.plantsEaten = null;
        this.numberOfChildren = null;
        this.numberOfDescendants =  null;
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
        return averageNumberOfChildren;
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
    public Integer getCurrentEnergy() {
        return currentEnergy;
    }
    public Integer getDayOfDeath() {
        return dayOfDeath;
    }
}
