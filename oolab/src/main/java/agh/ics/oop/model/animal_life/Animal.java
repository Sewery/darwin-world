package agh.ics.oop.model.animal_life;

import agh.ics.oop.model.MapDirection;
import agh.ics.oop.model.MoveValidator;
import agh.ics.oop.model.Vector2d;
import agh.ics.oop.model.WorldElement;
import lombok.Getter;

import java.util.Random;
import java.util.Set;

public class Animal implements WorldElement {

    protected AnimalChangeListener observer;
    @Getter
    protected Vector2d position;
    protected MapDirection direction;
    @Getter
    protected int energy;
    @Getter
    protected final int[] genotype;
    @Getter
    protected int currentGene;
    @Getter
    private int numberOfChildren;
    @Getter
    protected int age;
    @Getter
    private boolean alive = true;
    @Getter
    private int plantsEaten = 0;
    @Getter
    private final Set<Animal> ancestors;
    @Getter
    private int numberOfDescendants = 0;
    private Integer dayOfDeath;
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

    public void setDead(int dayOfDeath){
        alive = false;
        this.dayOfDeath=dayOfDeath;
        notifyObserver("isDead");
    }
    public Integer getDayOfDeath(){
        if(!alive){
            return dayOfDeath;
        }
        return -1;
    }
    public void increaseAge(){
        age++;
        notifyObserver("age");
    }

    @Override
    public String toString(){return "A";}

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

    public Vector2d getDirection() {return this.direction.toUnitVector();}

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

    void increaseNumberOfDescendants(){
        this.numberOfDescendants++;
        notifyObserver("numberOfDescendants");
    }


}