package agh.ics.oop.model;

public enum MapDirection {
    NORTH,
    NORTHEAST,
    EAST,
    SOUTHEAST,
    SOUTH,
    SOUTHWEST,
    WEST,
    NORTHWEST;

    static final Vector2d[] unitVectors = {
            new Vector2d(0,1),
            new Vector2d(1,1),
            new Vector2d(1,0),
            new Vector2d(-1,-1),
            new Vector2d(0,-1),
            new Vector2d(-1,-1),
            new Vector2d(-1,0),
            new Vector2d(-1,1),
    };


    public String toString() {
        return switch (this) {
            case NORTH -> "Północ";
            case NORTHEAST -> "Północny-Wschód";
            case NORTHWEST -> "Północny-Zachód";
            case SOUTHEAST -> "Południowy-Wschód";
            case SOUTHWEST -> "Południowy-Zachód";
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

    public MapDirection changeDirection(int n){

        if (n==0) {return this;}

        if (n <= 4) {
            for (int i = 0; i < n; i++){
                this.next();
            }
        }
        else {
            for (int i = 8; i > n; i--){
                this.previous();
            }
        }

        return this;
    }

    public Vector2d toUnitVector(){

        return switch (this) {
            case NORTH -> unitVectors[0];
            case NORTHEAST -> unitVectors[1];
            case EAST -> unitVectors[2];
            case SOUTHEAST -> unitVectors[3];
            case SOUTH -> unitVectors[4];
            case SOUTHWEST -> unitVectors[5];
            case WEST -> unitVectors[6];
            case NORTHWEST -> unitVectors[7];
        };
    }
}
