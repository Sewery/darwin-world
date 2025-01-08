package agh.ics.oop.model.animal_life;

import agh.ics.oop.model.MapDirection;
import agh.ics.oop.model.MoveValidator;
import agh.ics.oop.model.Vector2d;
import agh.ics.oop.model.WorldElement;
import agh.ics.oop.core.Configuration;

import java.util.Random;
import java.util.Set;

public class Animal implements WorldElement {


    private Vector2d position;
    private MapDirection direction;
    private int energy;
    private final int[] genotype;
    private int currentGene;
    private int numberOfChildren;
    private int age;
    private boolean alive = true;
    private int plantsEaten = 0;
    private final Set<Animal> ancestors;
    private int numberOfDescendants = 0;


    private final boolean ageOfBurden;
    private final int energyGivenByOneGrass;



    public Animal(
            Vector2d position,
            int[] genotype,
            Set<Animal> ancestors,
            int energyPerGrass,
            int initialEnergyOfAnimals,
            boolean ageOfBurden
            ) {
        this. ageOfBurden=ageOfBurden;
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

    private int randomGene(){
        return new Random().nextInt(genotype.length);
    }
    private MapDirection randomDirection(){
        MapDirection[] directions = MapDirection.values();
        return directions[new Random().nextInt(directions.length)];
    }

    public void setDead(){
        alive = false;
    }
    public void increaseAge(){
        age++;
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

    private boolean isGoingToMove(){
        if (!ageOfBurden) return true;
        double probabilityOfAnimalSkippingAMove = Math.min(0.01 * this.age, 0.8);
        return new Random().nextDouble() >= probabilityOfAnimalSkippingAMove;
    }

    public void move(MoveValidator validator, int energyMultiplier) {
        if (this.energy >= energyMultiplier) {  // animal on pole can have energy > 0 and not be able to move

            if (!isGoingToMove()) {
                System.out.println("skipped move");
                energy -= (int) (energyMultiplier);
            } // energy loss when animal is skipping a move because of it's age

            else {
                direction = direction.changeDirection(genotype[currentGene]);
                Vector2d newPosition = validator.canMoveTo(position.add(direction.toUnitVector()));
                if (newPosition != null) {
                    position = newPosition;
                    energy -= (int) (energyMultiplier);
                }
            }
        }
        currentGene = (currentGene + 1)%genotype.length;
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
    }

    public void reproduce(int energyLost){
        this.energy -= energyLost;
        this.numberOfChildren += 1;
    }

    Set<Animal> getAncestors() {return ancestors;}

    void increaseNumberOfDescendants(){
        this.numberOfDescendants++;
    }
}