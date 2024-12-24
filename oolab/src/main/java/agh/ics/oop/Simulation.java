package agh.ics.oop;

import agh.ics.oop.model.*;
import agh.ics.oop.model.util.Boundary;

import java.util.ArrayList;
import java.util.List;

public class Simulation implements Runnable {

    private final List<Animal> animals;
    private final WorldMap map;

    public Simulation(WorldMap map, int initialNumberOfAnimals, int initialEnergy, int genotypeLength, int AGE_OF_BURDEN, int MIN_ENERGY_TO_REPRODUCE) {

        this.map = map;
        this.animals = new ArrayList<>();

        Boundary boundary = map.getCurrentBounds();
        int width = boundary.upperRight().getX() - boundary.lowerLeft().getX();
        int height = boundary.upperRight().getY() - boundary.lowerLeft().getY();

        RandomPositionGenerator randomPositionGenerator = new RandomPositionGenerator(width, height, initialNumberOfAnimals);
        for(Vector2d animalPosition : randomPositionGenerator) {
            try
            {
                Animal animal = new Animal(animalPosition, initialEnergy, genotypeLength, MIN_ENERGY_TO_REPRODUCE, AGE_OF_BURDEN);
                map.place(animal);
                animals.add(animal);
            } catch (IncorrectPositionException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public Animal getAnimal(int index) {
        return animals.get(index);
    }


    public void run() {

        System.out.println(map);
        if (animals.isEmpty())  return;


        for (Animal animal: animals) {
            map.move(animal);
            /*
            try {
                Thread.sleep(500);
            }
            catch (InterruptedException e) {
                System.out.println("Exception: " + e.getMessage());
            }
             */
            //System.out.printf("Zwierzę %s : %s%n", i % animals.size(), animals.get(i % animals.size()).toStringPositionOnly());
            System.out.println(map);
        }

    }
}
