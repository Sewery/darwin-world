package agh.ics.oop.model;

import agh.ics.oop.model.AnimalLife.Animal;
import agh.ics.oop.model.AnimalLife.Reproduction;
import agh.ics.oop.model.util.Boundary;
import agh.ics.oop.model.util.MapChangeListener;

import java.util.*;

import static java.lang.Math.max;
import static java.lang.Math.min;

public class GrassField implements WorldMap {

    private final Map<Vector2d, Grass> grasses = new HashMap<>();
    private final Map<Vector2d, List<Animal>> animals = new HashMap<>();
    private final int newGrassesEachDay;

    private final Vector2d lowerLeft;
    private final Vector2d upperRight;
    private final Boundary boundary;

    private static int mapsCount = 0;
    private final int mapID;

    protected final List<MapChangeListener> observers = new ArrayList<>();

    private ArrayList<Vector2d> emptyEquatorGrassPositions = new ArrayList<>();
    private ArrayList<Vector2d> emptyOtherGrassPositions = new ArrayList<>();
    private final int equatorLowerBound;
    private final int equatorUpperBound;


    public GrassField(int initialGrassCount, int width, int height, int newGrassesEachDay) {
        this.lowerLeft = new Vector2d(0, 0);
        this.upperRight = new Vector2d(width-1, height-1);
        this.boundary = new Boundary(lowerLeft, upperRight);

        synchronized (this) {this.mapID = mapsCount++;}

        this.newGrassesEachDay = newGrassesEachDay;
        int equatorWidth = height/5;
        this.equatorLowerBound = height/2-equatorWidth+1;
        this.equatorUpperBound = height/2+equatorWidth;
        this.emptyEquatorGrassPositions = generateEmptyEquatorGrassPositions(width);
        this.emptyOtherGrassPositions = generateOtherEmptyGrassPositions(width, height);
        growPlants(initialGrassCount);

    }

    private ArrayList<Vector2d> generateOtherEmptyGrassPositions(int width, int height) {
        ArrayList<Vector2d> all_possible_positions = new ArrayList<>();
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < equatorLowerBound; y++) all_possible_positions.add(new Vector2d(x, y));
        }
        for (int x = 0; x < width; x++) {
            for (int y = equatorUpperBound; y < height; y++) all_possible_positions.add(new Vector2d(x, y));
        }

        Collections.shuffle(all_possible_positions, new Random());
        return all_possible_positions;
    }

    private ArrayList<Vector2d> generateEmptyEquatorGrassPositions(int width) {
        ArrayList<Vector2d> all_possible_positions = new ArrayList<>();
        for (int x = 0; x < width; x++) {
            for (int y = equatorLowerBound; y < equatorUpperBound; y++) all_possible_positions.add(new Vector2d(x, y));
        }

        Collections.shuffle(all_possible_positions, new Random());
        return all_possible_positions;
    }

    @Override
    public int getNumberOfNewGrassesEachDay(){
        return newGrassesEachDay;
    }

    @Override
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
            animals.computeIfAbsent(position, _ -> new ArrayList<>()).add(animal);
            return true;
        }
        throw new IncorrectPositionException(animal.getPosition());
    }

    @Override
    public void remove(Animal animal) {

        List<Animal> oldList = animalsAt(animal.getPosition());
        if (oldList != null){
            if (oldList.contains(animal)) {
                oldList.remove(animal);
                if (oldList.isEmpty()) {
                    animals.remove(animal.getPosition());
                }
            }
        }
    }

    @Override
    public void move(Animal animal) {

        List<Animal> oldList = animalsAt(animal.getPosition());
        if (oldList != null){
            if (oldList.contains(animal)) {
                remove(animal);
                animal.move(this);
                animals.computeIfAbsent(animal.getPosition(), _ -> new ArrayList<>()).add(animal);
            }
        }
    }

    @Override
    public Vector2d canMoveTo(Vector2d position) {


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

        return null;
    }

    @Override
    public List<WorldElement> getElements(){
        List<WorldElement> worldElements = new ArrayList<>();
        animals.values().forEach(worldElements::addAll);
        worldElements.addAll(grasses.values());
        return worldElements;
    }

    @Override
    public List<Animal> animalsAt(Vector2d position) {
        return animals.get(position);
    }

    @Override
    public Grass grassAt(Vector2d position){
        return grasses.get(position);
    }

    private Animal resolveFoodConflict(List<Animal> conflictedAnimals) {
        conflictedAnimals.sort(new AnimalComparator());
        return conflictedAnimals.getLast();
    }

    @Override
    public void consumePlants(){

        ArrayList<Grass> grassesToRemove = new ArrayList<>();

        for (Vector2d position : grasses.keySet()) {

            if (animals.containsKey(position)) {

                List<Animal> conflictedAnimals = animalsAt(position);

                if (conflictedAnimals.size() == 1) {
                    conflictedAnimals.getFirst().eat();
                }

                else{
                    Animal winner = resolveFoodConflict(conflictedAnimals);
                    winner.eat();
                }

                grassesToRemove.add(grasses.get(position));
            }
        }

        for (Grass grass : grassesToRemove) {
            grasses.remove(grass.getPosition());
            addEmptyGrassPosition(grass.getPosition());
        }
    }

    private void addEmptyGrassPosition(Vector2d position) {
        if (equatorLowerBound <= position.getY() && position.getY() < equatorUpperBound) {
            emptyEquatorGrassPositions.add(position);
        }
        else {emptyOtherGrassPositions.add(position);}
    }

    private List<Animal> resolveReproductionConflict(List<Animal> conflictedAnimals) {
        conflictedAnimals.sort(new AnimalComparator());
        return List.of(conflictedAnimals.getLast(), conflictedAnimals.get(conflictedAnimals.size()-2));
    }

    private List<Animal> filterReproductiveAnimals(List<Animal> allAnimals){
        List<Animal> reproductiveAnimals = new ArrayList<>();

        for (Animal animal : allAnimals) {
            if (animal.getEnergy() >= Animal.getMinEnergyToReproduce()){
                reproductiveAnimals.add(animal);
            }
        }

        if (reproductiveAnimals.size() < 2) {return null;}
        return reproductiveAnimals;
    }

    public List<Animal> reproduce() {

        List<Animal> newAnimals = new ArrayList<>();

        for (Vector2d position : animals.keySet()) {

            List<Animal> parents = filterReproductiveAnimals(animalsAt(position));

            if (parents == null) {
                continue;
            }

            if (parents.size() > 2) {
                parents = resolveReproductionConflict(parents);
            }

            Reproduction reproduction = new Reproduction(parents.getFirst(), parents.getLast());
            newAnimals.add(reproduction.createAChild());

        }

        return newAnimals;
    }

}
