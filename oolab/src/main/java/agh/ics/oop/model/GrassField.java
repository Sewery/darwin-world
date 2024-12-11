package agh.ics.oop.model;

import agh.ics.oop.model.util.Boundary;

import java.util.*;

import static java.lang.Math.sqrt;

public class GrassField extends AbstractWorldMap {

    private final Map<Vector2d, Grass> grasses = new HashMap<>();

    public GrassField(int grassCount) {
        int maxWidth = (int) sqrt(grassCount * 10);
        int maxHeight = (int) sqrt(grassCount * 10);

        RandomPositionGenerator randomPositionGenerator = new RandomPositionGenerator(maxWidth, maxHeight, grassCount);
        for(Vector2d grassPosition : randomPositionGenerator) {
            grasses.put(grassPosition, new Grass(grassPosition));
        }

    }

    @Override
    public WorldElement objectAt(Vector2d position) {
        if (super.objectAt(position) == null) return grasses.get(position);
        return super.objectAt(position);
    }

    @Override
    public Boundary getCurrentBounds() {

        List<WorldElement> worldElements = getElements();

        Vector2d upperRight = worldElements.getFirst().getPosition();
        Vector2d lowerLeft = worldElements.getFirst().getPosition();

        for (WorldElement worldElement : worldElements) {
            upperRight = upperRight.upperRight(worldElement.getPosition());
            lowerLeft = lowerLeft.lowerLeft(worldElement.getPosition());
        }

        return new Boundary(lowerLeft, upperRight);

    }

    @Override
    public List<WorldElement> getElements(){
        List<WorldElement> elements = super.getElements();
        elements.addAll(grasses.values());
        return elements;
    }


}
