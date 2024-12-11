package agh.ics.oop.model;

public class Animal implements WorldElement {

    private MapDirection orientation;
    private Vector2d position;
    static final private Vector2d[] borders = {new Vector2d(0,0), new Vector2d(4,4)};

    public Animal() {
        this.orientation = MapDirection.NORTH;
        this.position = new Vector2d(2, 2);
    }

    public Animal(Vector2d location) {
        this.position = location;
        this.orientation = MapDirection.NORTH;
    }

    public Vector2d getPosition() {
        return position;
    }

    public String toString(){
        return orientation.getSymbol();
    }

    public String toStringFull(){
        return "%s %s".formatted(position.toString(), orientation);
    }

    public String toStringPositionOnly(){
        return "%s".formatted(position.toString());
    }

    public boolean isAt(Vector2d position) {
        return this.position.equals(position);
    }

    public void move(MoveDirection direction, MoveValidator validator) {
        switch (direction) {
            case RIGHT -> orientation = orientation.next();
            case LEFT -> orientation = orientation.previous();
            case FORWARD -> {
                Vector2d expected = position.add(orientation.toUnitVector());
                if (validator.canMoveTo(expected)) position = expected;
            }
            case BACKWARD -> {
                Vector2d expected = position.subtract(orientation.toUnitVector());
                if (validator.canMoveTo(expected)) position = expected;
            }
        }

    }

}