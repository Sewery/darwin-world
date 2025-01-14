package agh.ics.oop.model.animal_life;

import agh.ics.oop.model.MapDirection;
import agh.ics.oop.model.MoveValidator;
import agh.ics.oop.model.Vector2d;
import agh.ics.oop.model.WorldElement;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class Animal implements WorldElement {

    protected AnimalChangeListener observer;
    protected Vector2d position;
    protected MapDirection direction;
    protected int energy;
    protected final int[] genotype;
    protected int currentGene;
    private int numberOfChildren;
    protected int age;
    private boolean alive = true;
    private int plantsEaten = 0;
    private final Set<Animal> ancestors;
    private int numberOfDescendants = 0;

    private final int energyGivenByOneGrass;

    public Animal(
            Vector2d position,
            int[] genotype,
            Set<Animal> ancestors,
            int energyPerGrass,
            int initialEnergyOfAnimals
            ) {

        this.energyGivenByOneGrass=energyPerGrass;

        this.position = position;
        this.direction = randomDirection();
        this.energy = initialEnergyOfAnimals;
        this.genotype = genotype;
        this.currentGene = randomGene();
        this.numberOfChildren = 0;
        this.age = -1;

        this.ancestors = ancestors;

    }
    public void addObserver(AnimalChangeListener statisticsChangeListener) {
        observer = statisticsChangeListener;
    }

    public void removeObserver(AnimalChangeListener statisticsChangeListener) {
        observer = null;
    }

    public void notifyObserver(String message) {
        if(observer != null) {
            observer.animalChanged(this,message);
        }
    }
    private int randomGene(){
        return new Random().nextInt(genotype.length);
    }
    private MapDirection randomDirection(){
        MapDirection[] directions = MapDirection.values();
        return directions[new Random().nextInt(directions.length)];
    }

    public void setDead(){
        alive = false;
        notifyObserver("isDead");
    }
    public void increaseAge(){
        age++;
        notifyObserver("age");
    }
    public boolean isAlive() {
        return alive;
    }

    public Vector2d getPosition() {
        return position;
    }
    @Override
    public String toString(){return "A";};
    public boolean isAt(Vector2d position) {
        return this.position.equals(position);
    }

    public void move(MoveValidator validator, int energyMultiplier) {
        if (this.energy >= energyMultiplier) {  // animal on pole can have energy > 0 and not be able to move

            direction = direction.changeDirection(genotype[currentGene]);
            Vector2d newPosition = validator.canMoveTo(position.add(direction.toUnitVector()));
            if (newPosition != null) {
                position = newPosition;
                energy -= energyMultiplier;
            }

        }
        currentGene = (currentGene + 1)%genotype.length;
        notifyObserver("currentGene");
    }

    public int[] getGenotype() {return genotype;}
    public int getCurrentGene() {return currentGene;}
    public Vector2d getDirection() {return this.direction.toUnitVector();}
    public int getEnergy(){return energy;}
    public int getAge(){return age;}
    public int getNumberOfChildren() {return numberOfChildren;}
    public int getPlantsEaten() {return plantsEaten;}

    public void eat(){
        this.plantsEaten++;
        this.energy += energyGivenByOneGrass;
        notifyObserver("plantsEaten");
    }

    public void reproduce(int energyLost){
        this.energy -= energyLost;
        this.numberOfChildren += 1;
        notifyObserver("numberOfChildren");
    }

    Set<Animal> getAncestors() {return ancestors;}

    void increaseNumberOfDescendants(){
        this.numberOfDescendants++;
        notifyObserver("numberOfDescendants");
    }

    public int getNumberOfDescendants() {
        return numberOfDescendants;
    }

}