package agh.ics.oop.model.AnimalLife;

import agh.ics.oop.model.MapDirection;
import agh.ics.oop.model.MoveValidator;
import agh.ics.oop.model.Vector2d;
import agh.ics.oop.model.WorldElement;

import java.util.Random;

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

    private static int GENOTYPE_LENGTH;
    private static int MIN_ENERGY_TO_REPRODUCE;
    private static int AGE_OF_BURDEN;
    private static int ENERGY_GIVEN_BY_ONE_GRASS;
    private static int INITIAL_ENERGY;



    public Animal(Vector2d position, int[] genotype) {

        this.position = position;
        this.direction = randomDirection();
        this.energy = INITIAL_ENERGY;
        this.genotype = genotype;
        this.currentGene = randomGene();
        this.numberOfChildren = 0;
        this.age = -1;

    }

    private int randomGene(){
        return new Random().nextInt(genotype.length);
    }
    private MapDirection randomDirection(){
        MapDirection[] directions = MapDirection.values();
        return directions[new Random().nextInt(directions.length)];
    }

    public static void set_MIN_ENERGY_TO_REPRODUCE(int energy) {
        MIN_ENERGY_TO_REPRODUCE = energy;
    }
    public static void set_AGE_OF_BURDEN(int age) {
        AGE_OF_BURDEN = age;
    }
    public static void set_GENOTYPE_LENGTH(int length) {
        GENOTYPE_LENGTH = length;
    }
    public static void set_ENERGY_GIVEN_BY_ONE_GRASS(int energy) {
        ENERGY_GIVEN_BY_ONE_GRASS = energy;
    }
    public static void set_INITIAL_ENERGY(int energy) {
        INITIAL_ENERGY = energy;
    }
    public static int getMinEnergyToReproduce() {
        return MIN_ENERGY_TO_REPRODUCE;
    }
    public static int getGenotypeLength() {
        return GENOTYPE_LENGTH;
    }
    public static int getInitialEnergy() {
        return INITIAL_ENERGY;
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

    public void move(MoveValidator validator) {
        energy -= 1;
        direction = direction.changeDirection(genotype[currentGene]);
        Vector2d newPosition = validator.canMoveTo(position.add(direction.toUnitVector()));
        if (newPosition != null) {
            position = newPosition;
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
        this.energy += ENERGY_GIVEN_BY_ONE_GRASS;
    }

    public void reproduce(int energyLost){
        this.energy -= energyLost;
        numberOfChildren += 1;
    }
}