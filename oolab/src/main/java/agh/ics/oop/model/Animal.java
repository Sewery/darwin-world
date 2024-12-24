package agh.ics.oop.model;

import java.util.ArrayList;
import java.util.Random;

public class Animal implements WorldElement {

    private MapDirection orientation;
    private Vector2d position;
    private int energy;
    private int[] genotype;
    private int currentGene;
    private final int MIN_ENERGY_TO_REPRODUCE;
    private int numberOfChildren;
    private int age;
    private final int AGE_OF_BURDEN;

    public Animal(Vector2d position, int initialEnergy, int genotypeLength, int MIN_ENERGY_TO_REPRODUCE, int AGE_OF_BURDEN ) {

        this.position = position;
        Random random = new Random();
        MapDirection[] directions = MapDirection.values();
        this.orientation = directions[random.nextInt(directions.length)];
        this.energy = initialEnergy;

        int[] genotype = new int[genotypeLength];

        for (int i = 0; i < genotypeLength; i++) {
            genotype[i] = random.nextInt(9);
        }
        this.currentGene = random.nextInt(genotypeLength);
        this.numberOfChildren = 0;
        this.age = 0;

        this.MIN_ENERGY_TO_REPRODUCE = MIN_ENERGY_TO_REPRODUCE;
        this.AGE_OF_BURDEN = AGE_OF_BURDEN;
    }

    public Animal(Vector2d location, MapDirection orientation, int initialEnergy, int genotypeLength, int MIN_ENERGY_TO_REPRODUCE, int AGE_OF_BURDEN) {
        this.position = location;
        this.orientation = orientation;
        this.energy = initialEnergy;
        Random random = new Random();
        int[] genotype = new int[genotypeLength];

        for (int i = 0; i < genotypeLength; i++) {
            genotype[i] = random.nextInt(9);
        }
        this.currentGene = random.nextInt(genotypeLength);
        this.numberOfChildren = 0;
        this.age = 0;
        this.MIN_ENERGY_TO_REPRODUCE = MIN_ENERGY_TO_REPRODUCE;
        this.AGE_OF_BURDEN = AGE_OF_BURDEN;
    }

    public Vector2d getPosition() {
        return position;
    }

    @Override
    public String toString(){return "A";};

    public String toStringFull(){
        return "%s %s".formatted(position.toString(), orientation);
    }

    public boolean isAt(Vector2d position) {
        return this.position.equals(position);
    }

    public void move(MoveValidator validator) {
        orientation.changeDirection(genotype[currentGene]);
        Vector2d expected = position.add(orientation.toUnitVector());
        if (validator.canMoveTo(expected)) position = expected;
        currentGene = (currentGene + 1)%genotype.length;
    }

}