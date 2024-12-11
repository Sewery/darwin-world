package agh.ics.oop.model;

public enum MapDirection {
    NORTH,
    EAST,
    SOUTH,
    WEST;

    static final Vector2d[] unitVectors = {     new Vector2d(0,1),
            new Vector2d(0,-1),
            new Vector2d(-1,0),
            new Vector2d(1,0)};

    public String getSymbol() {
        return switch (this) {
            case NORTH -> "^";
            case SOUTH -> "v";
            case WEST -> "<";
            case EAST -> ">";
        };
    }

    public String toString() {
        return switch (this) {
            case NORTH -> "Północ";
            case SOUTH -> "Południe";
            case WEST -> "Zachód";
            case EAST -> "Wschód";
        };
    }

    public MapDirection next(){
        return MapDirection.values()[(this.ordinal() + 1) % MapDirection.values().length];
    }

    public MapDirection previous(){
        return MapDirection.values()[(this.ordinal() + MapDirection.values().length - 1) % MapDirection.values().length];}

    public Vector2d toUnitVector(){

        return switch (this) {
            case NORTH -> unitVectors[0];
            case SOUTH -> unitVectors[1];
            case WEST -> unitVectors[2];
            case EAST -> unitVectors[3];
        };
    }
}
