package agh.ics.oop;

import agh.ics.oop.model.*;
import agh.ics.oop.model.util.Boundary;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Simulation implements Runnable {

    private final List<Animal> animals;
    private final WorldMap map;


    public Simulation(WorldMap map, int initialNumberOfAnimals, int initialEnergy, int GENOTYPE_LENGTH, int AGE_OF_BURDEN, int MIN_ENERGY_TO_REPRODUCE) {

        this.map = map;
        this.animals = new ArrayList<>();

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

        int i = 0;
        while (i < 100) {

            for (Animal animal : animals) {
                try {
                    Thread.sleep(500);
                    map.move(animal);
                } catch (InterruptedException e) {
                    System.out.println("Exception: " + e.getMessage());
                }

            }

            map.notifyObservers("Dzień %s".formatted(i));
            i += 1;
        }


    }
}
