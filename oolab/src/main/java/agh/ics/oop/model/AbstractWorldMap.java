package agh.ics.oop.model;
import agh.ics.oop.model.util.Boundary;
import agh.ics.oop.model.util.ConsoleMapDisplay;
import agh.ics.oop.model.util.MapChangeListener;
import agh.ics.oop.model.util.MapVisualizer;

import java.util.*;

abstract class AbstractWorldMap implements WorldMap {

    protected final MapVisualizer mapVisualizer = new MapVisualizer(this);
    protected final Map<Vector2d, Animal> animals = new HashMap<>();

    protected final List<MapChangeListener> observers = new ArrayList<>();


    private static int mapsCount = 0;
    private final int mapID;

    AbstractWorldMap() {
        synchronized (this) {this.mapID = mapsCount++;}
    }

    @Override
    public int getID(){
        return mapID;
    }

    @Override
    public void addObserver(MapChangeListener mapChangeListener) {observers.add(mapChangeListener);}

    @Override
    public void removeObserver(MapChangeListener mapChangeListener) {observers.remove(mapChangeListener);}

    protected void notifyObservers(String message) {
        for (MapChangeListener observer : observers) {observer.mapChanged(this, message);}
    }

    @Override
    public boolean place(Animal animal) throws IncorrectPositionException {
        if (canMoveTo(animal.getPosition())) {
            animals.put(animal.getPosition(), animal);
            notifyObservers("Animal placed at " + animal.getPosition());
            return true;
        }
        throw new IncorrectPositionException(animal.getPosition());
    }

    @Override
    public void move(Animal animal, MoveDirection direction) {
        if (objectAt(animal.getPosition()) == animal) { // czy zwierzak jest na mapie

            Vector2d oldPosition = animal.getPosition();
            animal.move(direction, this);
            animals.remove(oldPosition);
            animals.put(animal.getPosition(), animal);
            if (!(animal.getPosition().equals(oldPosition))) notifyObservers("Animal moved from %s to %s".formatted(oldPosition, animal.getPosition()));
        }
    }

    @Override
    public boolean isOccupied(Vector2d position) {
        return objectAt(position) != null;
    }

    @Override
    public boolean canMoveTo(Vector2d position) {
        return !(objectAt(position) instanceof Animal);
    }

    @Override
    public List<WorldElement> getElements(){
        return new ArrayList<>(animals.values());
    }

    @Override
    public WorldElement objectAt(Vector2d position) {
        return animals.get(position);
    }

    @Override
    public abstract Boundary getCurrentBounds();

    @Override
    public String toString(){
        Boundary boundaries = getCurrentBounds();
        return mapVisualizer.draw(boundaries.lowerLeft(), boundaries.upperRight());
    }
}
