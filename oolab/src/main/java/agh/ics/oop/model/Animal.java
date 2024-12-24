package agh.ics.oop.model;

import java.util.ArrayList;
import java.util.Random;

public class Animal implements WorldElement {

    private Vector2d position;
    private MapDirection direction;
    private int energy;
    private int[] genotype;
    private int currentGene;
    private int numberOfChildren;
    private int age;

    private static int GENOTYPE_LENGTH;
    private static int MIN_ENERGY_TO_REPRODUCE;
    private static int AGE_OF_BURDEN;

    public Animal(Vector2d position, int energy ) {

        this.position = position;
        this.direction = randomDirection();
        this.energy = energy;
        this.genotype = randomGenotype();
        this.currentGene = randomGene();
        this.numberOfChildren = 0;
        this.age = 0;

    }

    private int randomGene(){
        return new Random().nextInt(genotype.length);
    }

    private int[] randomGenotype(){
        Random random = new Random();
        int[] genotype = new int[GENOTYPE_LENGTH];
        for (int i = 0; i < GENOTYPE_LENGTH; i++) {
            genotype[i] = random.nextInt(MapDirection.values().length);
        }
        return genotype;
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


    public Vector2d getPosition() {
        return position;
    }

    @Override
    public String toString(){return "A";};

    public boolean isAt(Vector2d position) {
        return this.position.equals(position);
    }

    public void move(MoveValidator validator) {
        direction = direction.changeDirection(genotype[currentGene]);
        Vector2d newPosition = validator.canMoveTo(position.add(direction.toUnitVector()));
        if (newPosition != null) position = newPosition;
        currentGene = (currentGene + 1)%genotype.length;
    }

    public int[] getGenotype() {return genotype;}
    public int getCurrentGene() {return currentGene;}
    public Vector2d getDirection() {return this.direction.toUnitVector();}
}