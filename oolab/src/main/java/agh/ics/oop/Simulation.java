package agh.ics.oop;

import agh.ics.oop.model.*;

import java.util.ArrayList;
import java.util.List;

public class Simulation implements Runnable {

    private final List<Animal> animals;
    private final List<MoveDirection> moves;
    private final WorldMap map;

    public Simulation(List<Vector2d> positions, List<MoveDirection> directions, WorldMap map) {
        this.moves = directions;
        this.map = map;
        this.animals = new ArrayList<>();

        for (Vector2d position : positions)
        {
            try
            {
                Animal animal = new Animal(position);
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

    public List<MoveDirection> getMoves() {
        return moves;
    }

    public void run() {

        if (animals.isEmpty())  return;
        //System.out.println(map);

        for (int i = 0; i < moves.size(); i++) {
            map.move(animals.get(i % animals.size()), moves.get(i));
            try {
                Thread.sleep(500);
            }
            catch (InterruptedException e) {
                System.out.println("Exception: " + e.getMessage());
            }
            //System.out.printf("Zwierzę %s : %s%n", i % animals.size(), animals.get(i % animals.size()).toStringPositionOnly());
            //System.out.println(map);
        }
    }
}
