package agh.ics.oop;

import agh.ics.oop.model.*;
import agh.ics.oop.model.util.Boundary;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Simulation implements Runnable {

    private final List<Animal> animals;
    private final WorldMap map;
    private int daysCount;


    public Simulation(WorldMap map, int initialNumberOfAnimals, int initialEnergy, int GENOTYPE_LENGTH, int AGE_OF_BURDEN, int MIN_ENERGY_TO_REPRODUCE) {

        this.map = map;

        this.animals = new ArrayList<>();
        this.daysCount = 0;

        Boundary boundary = map.getCurrentBounds();
        int width = boundary.upperRight().getX() - boundary.lowerLeft().getX();
        int height = boundary.upperRight().getY() - boundary.lowerLeft().getY();

        Animal.set_MIN_ENERGY_TO_REPRODUCE(MIN_ENERGY_TO_REPRODUCE);
        Animal.set_AGE_OF_BURDEN(AGE_OF_BURDEN);
        Animal.set_GENOTYPE_LENGTH(GENOTYPE_LENGTH);

        RandomPositionGenerator randomPositionGenerator = new RandomPositionGenerator(width, height, initialNumberOfAnimals);
        for(Vector2d animalPosition : randomPositionGenerator) {
            try
            {
                Animal animal = new Animal(animalPosition, initialEnergy);
                map.place(animal);
                animals.add(animal);
            } catch (IncorrectPositionException e) {
                System.out.println(e.getMessage());
            }
        }
    }


    public void run() {

        System.out.println("Simulation started as: ");
        System.out.println(map);
        if (animals.isEmpty())  return;

        // print genotypów
        for (int i = 0; i < animals.size(); i++) {
            System.out.printf("Zwierze %s: %s, start with: %s %s\n", i, Arrays.toString(animals.get(i).getGenotype()), animals.get(i).getCurrentGene(), animals.get(i).getDirection());
        }

        System.out.println();

        while (daysCount < 10) {

            System.out.printf("Day %s%n", daysCount);
            System.out.println(map);

            sleep();
            removeDeadAnimals();

            sleep();
            moveAnimals();

            sleep();
            // consumePlants();

            sleep();
            // reproduce();

            sleep();
            growPlants();

            daysCount += 1;
        }


    }

    private void removeDeadAnimals(){
        ArrayList<Animal> deadAnimals = new ArrayList<>();
        for (Animal animal : animals) {
            if (animal.getEnergy() == 0) {
                deadAnimals.add(animal);
            }
        }

        for (Animal deadAnimal : deadAnimals) {
            map.remove(deadAnimal);
            animals.remove(deadAnimal);
        }
        map.notifyObservers("Dzień %s: remove dead animals".formatted(daysCount));
    }

    private void moveAnimals(){
        for (Animal animal : animals) {
            map.move(animal);
        }
        map.notifyObservers("Dzień %s: move animals".formatted(daysCount));
    }

    private void growPlants(){
        map.growPlants(map.getNumberOfNewGrassesEachDay());
    }

    private void sleep(){
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            System.out.println("Exception: " + e.getMessage());
        }
    }
}
