package agh.ics.oop.model;

import agh.ics.oop.model.util.Boundary;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class RectangularMapTest {


    private final List<Vector2d> outOfBoundsPositions = List.of(
            new Vector2d(-1, -1),
            new Vector2d(5, 5),
            new Vector2d(-2, 5),
            new Vector2d(6, 2)
    );

    private final List<Vector2d> inBoundsPositions = List.of(
            new Vector2d(1, 0),
            new Vector2d(4, 4),
            new Vector2d(2, 1),
            new Vector2d(3, 4)
    );

    private final Animal animal1 = new Animal(outOfBoundsPositions.get(0));
    private final Animal animal2 = new Animal(outOfBoundsPositions.get(1));
    private final Animal animal3 = new Animal(outOfBoundsPositions.get(2));
    private final Animal animal4 = new Animal(outOfBoundsPositions.get(3));

    private final Animal animal5 = new Animal(inBoundsPositions.get(0));
    private final Animal animal6 = new Animal(inBoundsPositions.get(1));
    private final Animal animal7 = new Animal(inBoundsPositions.get(2));
    private final Animal animal8 = new Animal(inBoundsPositions.get(3));


    @Test
    void placeTest() {

        final RectangularMap map = new RectangularMap(5, 5);

        assertThrows(IncorrectPositionException.class, () -> map.place(animal1));
        assertThrows(IncorrectPositionException.class, () -> map.place(animal2));
        assertThrows(IncorrectPositionException.class, () -> map.place(animal3));
        assertThrows(IncorrectPositionException.class, () -> map.place(animal4));

        try { assertTrue(map.place(animal5));}
        catch (IncorrectPositionException e) {fail(e.getMessage());}
        try { assertTrue(map.place(animal6));}
        catch (IncorrectPositionException e) {fail(e.getMessage());}
        try { assertTrue(map.place(animal7));}
        catch (IncorrectPositionException e) {fail(e.getMessage());}
        try { assertTrue(map.place(animal8));}
        catch (IncorrectPositionException e) {fail(e.getMessage());}
    }

    @Test
    void isOccupiedTest() {

        final RectangularMap map = new RectangularMap(5, 5);
        try { assertTrue(map.place(animal5));}
        catch (IncorrectPositionException e) {fail(e.getMessage());}
        try { assertTrue(map.place(animal6));}
        catch (IncorrectPositionException e) {fail(e.getMessage());}

        assertTrue(map.isOccupied(inBoundsPositions.get(0)));
        assertTrue(map.isOccupied(inBoundsPositions.get(1)));

        assertFalse(map.isOccupied(inBoundsPositions.get(2)));
        assertFalse(map.isOccupied(inBoundsPositions.get(3)));

        assertFalse(map.isOccupied(outOfBoundsPositions.get(0)));
        assertFalse(map.isOccupied(outOfBoundsPositions.get(1)));

    }

    @Test
    void objectAtTest(){

        final RectangularMap map = new RectangularMap(5, 5);
        try { assertTrue(map.place(animal5));}
        catch (IncorrectPositionException e) {fail(e.getMessage());}
        try { assertTrue(map.place(animal6));}
        catch (IncorrectPositionException e) {fail(e.getMessage());}

        assertEquals(animal5, map.objectAt(inBoundsPositions.get(0)));
        assertEquals(animal6, map.objectAt(inBoundsPositions.get(1)));
        assertNull(map.objectAt(inBoundsPositions.get(2)));
        assertNull(map.objectAt(inBoundsPositions.get(3)));

        assertNull(map.objectAt(outOfBoundsPositions.get(2)));

    }

    @Test
    void canMoveToTest(){

        final RectangularMap map = new RectangularMap(5, 5);
        try { assertTrue(map.place(animal5));}
        catch (IncorrectPositionException e) {fail(e.getMessage());}
        try { assertTrue(map.place(animal6));}
        catch (IncorrectPositionException e) {fail(e.getMessage());}

        assertFalse(map.canMoveTo(inBoundsPositions.get(0)));
        assertFalse(map.canMoveTo(inBoundsPositions.get(1)));

        assertTrue(map.canMoveTo(inBoundsPositions.get(2)));
        assertTrue(map.canMoveTo(inBoundsPositions.get(3)));

        assertFalse(map.canMoveTo(outOfBoundsPositions.get(0)));
        assertFalse(map.canMoveTo(outOfBoundsPositions.get(1)));
    }

    @Test
    void moveTest(){
        final RectangularMap map = new RectangularMap(5, 5);


        try { assertTrue(map.place(animal5));}
        catch (IncorrectPositionException e) {fail(e.getMessage());}
        map.move(animal5, MoveDirection.FORWARD);
        assertEquals(animal5.toStringFull(), "(1,1) Północ");

        try { assertTrue(map.place(animal6));}
        catch (IncorrectPositionException e) {fail(e.getMessage());}
        map.move(animal6, MoveDirection.RIGHT);
        assertEquals(animal6.toStringFull(), "(4,4) Wschód");

        try { assertTrue(map.place(animal8));}
        catch (IncorrectPositionException e) {fail(e.getMessage());}
        map.move(animal8, MoveDirection.RIGHT);
        map.move(animal8, MoveDirection.FORWARD);
        assertEquals(animal8.toStringFull(), "(3,4) Wschód");
    }

    @Test
    void getElementsTest(){
        final RectangularMap map = new RectangularMap(5, 5);
        assertEquals(0, map.getElements().size());

        try { assertTrue(map.place(animal5));}
        catch (IncorrectPositionException e) {fail(e.getMessage());}
        try { assertTrue(map.place(animal6));}
        catch (IncorrectPositionException e) {fail(e.getMessage());}

        List<WorldElement> expectedElements = List.of(animal5, animal6);
        assertEquals(expectedElements, map.getElements());


    }

    @Test
    void toStringTest(){

        final RectangularMap map = new RectangularMap(5, 5);

        try { assertTrue(map.place(animal5));}
        catch (IncorrectPositionException e) {fail(e.getMessage());}
        map.move(animal5, MoveDirection.FORWARD);

        String expected =   " y\\x  0 1 2 3 4\n" +
                            "  5: -----------\n" +
                            "  4: | | | | | |\n" +
                            "  3: | | | | | |\n" +
                            "  2: | | | | | |\n" +
                            "  1: | |^| | | |\n" +
                            "  0: | | | | | |\n" +
                            " -1: -----------\n";
        assertEquals(expected, map.toString());

    }

    @Test
    void getCurrentBoundsTest(){
        final RectangularMap map = new RectangularMap(5, 5);

        Boundary expected = new Boundary ( new Vector2d(0,0), new Vector2d(4,4));
        assertEquals(expected, map.getCurrentBounds());
    }

}


