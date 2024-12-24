package agh.ics.oop.model;

import agh.ics.oop.model.util.Boundary;
import agh.ics.oop.model.util.MapChangeListener;
import agh.ics.oop.model.util.MapVisualizer;

import java.util.*;

import static java.lang.Math.max;
import static java.lang.Math.min;

public class GrassField implements WorldMap {

    private final Map<Vector2d, Grass> grasses = new HashMap<>();
    private final Map<Vector2d, Animal> animals = new HashMap<>();
    private final int newGrassesEachDay;

    private final Vector2d lowerLeft;
    private final Vector2d upperRight;
    private final Boundary boundary;

    private static int mapsCount = 0;
    private final int mapID;

    protected final List<MapChangeListener> observers = new ArrayList<>();
    protected final MapVisualizer mapVisualizer = new MapVisualizer(this);

    private ArrayList<Vector2d> emptyEquatorGrassPositions = new ArrayList<>();
    private ArrayList<Vector2d> emptyOtherGrassPositions = new ArrayList<>();


    public GrassField(int initialGrassCount, int width, int height, int newGrassesEachDay) {
        this.lowerLeft = new Vector2d(0, 0);
        this.upperRight = new Vector2d(width-1, height-1);
        this.boundary = new Boundary(lowerLeft, upperRight);

        synchronized (this) {this.mapID = mapsCount++;}

        this.newGrassesEachDay = newGrassesEachDay;
        int equatorWidth = height/5;
        this.emptyEquatorGrassPositions = generateEmptyEquatorGrassPositions(width, height, equatorWidth);
        this.emptyOtherGrassPositions = generateOtherEmptyGrassPositions(width, height, equatorWidth);
        growPlants(initialGrassCount);

    }

    private ArrayList<Vector2d> generateOtherEmptyGrassPositions(int width, int height, int equator) {
        ArrayList<Vector2d> all_possible_positions = new ArrayList<>();
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height/2-equator+1; y++) all_possible_positions.add(new Vector2d(x, y));
        }
        for (int x = 0; x < width; x++) {
            for (int y = height/2+equator; y < height; y++) all_possible_positions.add(new Vector2d(x, y));
        }

        Collections.shuffle(all_possible_positions, new Random());
        return all_possible_positions;
    }

    private ArrayList<Vector2d> generateEmptyEquatorGrassPositions(int width, int height, int equator) {
        ArrayList<Vector2d> all_possible_positions = new ArrayList<>();
        for (int x = 0; x < width; x++) {
            for (int y = height/2-equator+1; y < height/2+equator; y++) all_possible_positions.add(new Vector2d(x, y));
        }

        Collections.shuffle(all_possible_positions, new Random());
        return all_possible_positions;
    }

    public int getNumberOfNewGrassesEachDay(){
        return newGrassesEachDay;
    }

    public void growPlants(int grassCount) {

        grassCount = min(grassCount, emptyOtherGrassPositions.size() + emptyEquatorGrassPositions.size());

        for (int i = 0; i < grassCount; i++){

            ArrayList<Vector2d> selectedArea = randomSelect(emptyEquatorGrassPositions, emptyOtherGrassPositions);

            Random random = new Random();
            Vector2d randomGrassPosition = selectedArea.get(random.nextInt(selectedArea.size()));
            grasses.put(randomGrassPosition, new Grass(randomGrassPosition));
            selectedArea.remove(randomGrassPosition);
        }

    }

    private ArrayList<Vector2d> randomSelect(ArrayList<Vector2d> areaOne, ArrayList<Vector2d> areaTwo) {

        if (areaOne.isEmpty()) return areaTwo;
        if (areaTwo.isEmpty()) return areaOne;

        Random random = new Random();
        double randomValue = random.nextDouble();
        return randomValue < 0.8 ? areaOne : areaTwo;
    }

    private void growPlantOnASpecificArea(ArrayList<Vector2d> area) {
        Random random = new Random();
        Vector2d randomGrassPosition = area.get(random.nextInt(area.size()));
        grasses.put(randomGrassPosition, new Grass(randomGrassPosition));
        area.remove(randomGrassPosition);
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

    @Override
    public void notifyObservers(String message) {
        for (MapChangeListener observer : observers) {observer.mapChanged(this, message);}
    }

    @Override
    public boolean place(Animal animal) throws IncorrectPositionException {

        Vector2d position = canMoveTo(animal.getPosition());
        if (position != null) {
            animals.put(position, animal);
            return true;
        }
        throw new IncorrectPositionException(animal.getPosition());
    }

    @Override
    public void remove(Animal animal) {

        if (objectAt(animal.getPosition()) == animal) {
            animals.remove(animal.getPosition());
        }
    }

    @Override
    public void move(Animal animal) {
        if (objectAt(animal.getPosition()) == animal) { // czy zwierzak jest na mapie
            Vector2d oldPosition = animal.getPosition();
            animal.move(this);
            animals.remove(oldPosition);
            animals.put(animal.getPosition(), animal);
        }
    }

    @Override
    public boolean isOccupied(Vector2d position) {
        return objectAt(position) != null;
    }

    @Override
    public Vector2d canMoveTo(Vector2d position) {

        if (!(objectAt(position) instanceof Animal)) {

            if (position.follows(lowerLeft) && position.precedes(upperRight)) {
                return position;
            }

            if (lowerLeft.getY() <= position.getY() && position.getY() <= upperRight.getY()) {
                if (position.getX() < lowerLeft.getX()) {
                    return new Vector2d(upperRight.getX(), position.getY());
                }

                if (position.getX() > upperRight.getX()) {
                    return new Vector2d(lowerLeft.getX(), position.getY());
                }
            }
        }

        return null;
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
