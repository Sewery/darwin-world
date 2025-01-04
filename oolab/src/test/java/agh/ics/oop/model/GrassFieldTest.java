package agh.ics.oop.model;

import agh.ics.oop.model.util.Boundary;
import org.junit.jupiter.api.Test;

import java.util.List;

import static java.lang.Math.sqrt;
import static org.junit.jupiter.api.Assertions.*;

public class GrassFieldTest {
    /*
    private final List<Vector2d> positions = List.of(
            new Vector2d(1, 0),
            new Vector2d(4, 4),
            new Vector2d(2, 1),
            new Vector2d(3, 4)
    );

    private final Animal animal1 = new Animal(positions.get(0));
    private final Animal animal2 = new Animal(positions.get(1));
    private final Animal animal3 = new Animal(positions.get(2));
    private final Animal animal4 = new Animal(positions.get(3));

    @Test
    void placeTest(){
        final GrassField map = new GrassField(10);

        try { assertTrue(map.place(animal1));}
        catch (IncorrectPositionException e) {fail(e.getMessage());}
        try { assertTrue(map.place(animal2));}
        catch (IncorrectPositionException e) {fail(e.getMessage());}
        try { assertTrue(map.place(animal3));}
        catch (IncorrectPositionException e) {fail(e.getMessage());}
        try { assertTrue(map.place(animal4));}
        catch (IncorrectPositionException e) {fail(e.getMessage());}

        assertThrows(IncorrectPositionException.class, () -> map.place(animal1));
    }


    @Test
    void moveTest(){
        final GrassField map = new GrassField(10);

        try { assertTrue(map.place(animal4));}
        catch (IncorrectPositionException e) {fail(e.getMessage());}
        map.move(animal4, MoveDirection.RIGHT);
        assertEquals(animal4.toStringFull(), "(3,4) Wschód");

        try { assertTrue(map.place(animal2));}
        catch (IncorrectPositionException e) {fail(e.getMessage());}

        map.move(animal4, MoveDirection.FORWARD);
        assertEquals(animal4.toStringFull(), "(3,4) Wschód");

        map.move(animal2, MoveDirection.BACKWARD);
        assertEquals(animal2.toStringFull(), "(4,3) Północ");

        map.move(animal2, MoveDirection.LEFT);
        assertEquals(animal2.toStringFull(), "(4,3) Zachód");

    }

    @Test
    void isOccupiedTest(){
        final GrassField map = new GrassField(1);

        assertFalse(map.isOccupied(positions.get(3)));

        try { assertTrue(map.place(animal1));}
        catch (IncorrectPositionException e) {fail(e.getMessage());}
        assertTrue(map.isOccupied(positions.get(0)));
    }

    @Test
    void canMoveToTest(){
        final GrassField map = new GrassField(10);

        try { assertTrue(map.place(animal1));}
        catch (IncorrectPositionException e) {fail(e.getMessage());}

        assertFalse(map.canMoveTo(positions.get(0)));
        assertTrue(map.canMoveTo(positions.get(1)));
        assertTrue(map.canMoveTo(positions.get(3)));
    }

    @Test
    void getElementsTest(){
        final GrassField map = new GrassField(2);
        try { assertTrue(map.place(animal1));}
        catch (IncorrectPositionException e) {fail(e.getMessage());}

        List<WorldElement> elements = map.getElements();

        assertEquals(3, elements.size());
        assertEquals(animal1, elements.getFirst());
        for (int i = 1; i < elements.size(); i++) assertEquals("*", elements.get(i).toString());

    }

    @Test
    void objectAtTest(){
        final GrassField map = new GrassField(1);

        assertNull(map.objectAt(positions.get(3)));

        try { assertTrue(map.place(animal1));}
        catch (IncorrectPositionException e) {fail(e.getMessage());}
        assertEquals(animal1, map.objectAt(positions.get(0)));
    }

    @Test
    void getCurrentBoundsTest() {
        Vector2d minBound = new Vector2d(0, 0);

        final GrassField map1 = new GrassField(1);
        int maxMapSize1 = (int) sqrt(1 * 10);
        Vector2d maxBound1 = new Vector2d(maxMapSize1, maxMapSize1);
        Boundary boundaries1 = map1.getCurrentBounds();
        assertTrue(boundaries1.lowerLeft().follows(minBound) && boundaries1.upperRight().precedes(maxBound1));

        final GrassField map2 = new GrassField(10);
        int maxMapSize2 = (int) sqrt(10 * 10);
        Vector2d maxBound2 = new Vector2d(maxMapSize2, maxMapSize2);
        Boundary boundaries2 = map2.getCurrentBounds();
        assertTrue(boundaries2.lowerLeft().follows(minBound) && boundaries2.upperRight().precedes(maxBound2));
    }

     */


}
