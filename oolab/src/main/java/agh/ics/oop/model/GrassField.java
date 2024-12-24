package agh.ics.oop.model;

import agh.ics.oop.model.util.Boundary;
import agh.ics.oop.model.util.MapChangeListener;
import agh.ics.oop.model.util.MapVisualizer;

import java.util.*;

public class GrassField implements WorldMap {

    private final Map<Vector2d, Grass> grasses = new HashMap<>();
    private final Map<Vector2d, Animal> animals = new HashMap<>();

    private final Vector2d lowerLeft;
    private final Vector2d upperRight;
    private final Boundary boundary;

    private static int mapsCount = 0;
    private final int mapID;

    protected final List<MapChangeListener> observers = new ArrayList<>();
    protected final MapVisualizer mapVisualizer = new MapVisualizer(this);


    public GrassField(int grassCount, int width, int height) {
        this.lowerLeft = new Vector2d(0, 0);
        this.upperRight = new Vector2d(width-1, height-1);
        this.boundary = new Boundary(lowerLeft, upperRight);

        synchronized (this) {this.mapID = mapsCount++;}

        RandomPositionGenerator randomPositionGenerator = new RandomPositionGenerator(width, height, grassCount);
        for(Vector2d grassPosition : randomPositionGenerator) {
            grasses.put(grassPosition, new Grass(grassPosition));
        }

    }

    @Override
    public Boundary getCurrentBounds() {
        return boundary;
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
    public void move(Animal animal) {
        if (objectAt(animal.getPosition()) == animal) { // czy zwierzak jest na mapie
            Vector2d oldPosition = animal.getPosition();
            animal.move(this);
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
        return !(objectAt(position) instanceof Animal) && position.follows(lowerLeft) && position.precedes(upperRight);
    }

    @Override
    public List<WorldElement> getElements(){
        ArrayList<WorldElement> elements = new ArrayList<>(animals.values());
        elements.addAll(grasses.values());
        return elements;
    }

    @Override
    public WorldElement objectAt(Vector2d position) {
        WorldElement object = animals.get(position);
        if (object == null) return grasses.get(position);
        return object;
    }

    @Override
    public String toString(){
        Boundary boundaries = getCurrentBounds();
        return mapVisualizer.draw(boundaries.lowerLeft(), boundaries.upperRight());
    }


}
