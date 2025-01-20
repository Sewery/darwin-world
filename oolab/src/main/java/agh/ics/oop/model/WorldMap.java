package agh.ics.oop.model;

import agh.ics.oop.model.animal_life.Animal;
import agh.ics.oop.model.util.Boundary;
import agh.ics.oop.model.util.MapChangeListener;

import java.util.List;

/**
 * The interface responsible for interacting with the map of the world.
 * Assumes that Vector2d and MoveDirection classes are defined.
 *
 * @author apohllo, idzik
 */
public interface WorldMap extends MoveValidator {

    boolean place(Animal animal) throws IncorrectPositionException;
    void remove(Animal animal);
    void move(Animal animal);

    List<Animal> animalsAt(Vector2d position);
    Grass grassAt(Vector2d position);
    List<WorldElement> getElements();

    Boundary getCurrentBounds();

    void addObserver(MapChangeListener mapChangeListener);
    void removeObserver(MapChangeListener mapChangeListener);
    void notifyObservers(String message);
    int getID();

    void growPlants(int grassCount);
    int getNumberOfNewGrassesEachDay();
    void consumePlants();

    List<Animal> reproduce();
    int getNumberOfGrasses();
    int getNumberOfEmptySpaces();
}