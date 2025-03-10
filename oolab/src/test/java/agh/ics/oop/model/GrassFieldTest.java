package agh.ics.oop.model;

import agh.ics.oop.model.core.Configuration;
import agh.ics.oop.model.animal_life.Animal;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class GrassFieldTest {

    private final Configuration configuration1 = new Configuration(10, 10, 10, 1, 5, 5, 5, 1, 1, 1, 3, Configuration.MapStrategy.GLOBE , Configuration.AnimalsBehaviourStrategy.FORESTED_EQUATOR);
    private final Configuration configuration2 = new Configuration(3, 7, 0, 1, 5, 5, 5, 1, 1, 1, 3, Configuration.MapStrategy.GLOBE , Configuration.AnimalsBehaviourStrategy.FORESTED_EQUATOR);
    private final Configuration configuration3 = new Configuration(3, 3, 9, 1, 5, 5, 5, 1, 1, 1, 3, Configuration.MapStrategy.GLOBE , Configuration.AnimalsBehaviourStrategy.FORESTED_EQUATOR);
    private final Configuration configuration4 = new Configuration(3, 3, 9, 1, 5, 1, 5, 3, 1, 1, 3, Configuration.MapStrategy.GLOBE , Configuration.AnimalsBehaviourStrategy.FORESTED_EQUATOR);
    private final Configuration configuration5 = new Configuration(1, 1, 1, 1, 5, 2, 5, 3, 1, 1, 3, Configuration.MapStrategy.GLOBE , Configuration.AnimalsBehaviourStrategy.FORESTED_EQUATOR);
    private final Configuration configuration6 = new Configuration(3, 3, 9, 1, 5, 0, 5, 1, 1, 1, 3, Configuration.MapStrategy.GLOBE , Configuration.AnimalsBehaviourStrategy.FORESTED_EQUATOR);

    private final List<Vector2d> positions = List.of(
            new Vector2d(1, 0),
            new Vector2d(4, 4),
            new Vector2d(2, 1),
            new Vector2d(3, 4),
            new Vector2d(20, 20),
            new Vector2d(8, 2),
            new Vector2d(0, 0)
    );

    int[] genotypeExample = {0, 0, 0};

    private final Animal animal1 = new Animal (positions.get(0), genotypeExample , new HashSet<>(), 5, 5);
    private final Animal animal2 = new Animal(positions.get(1), genotypeExample , new HashSet<>(), 5, 5);
    private final Animal animal3 = new Animal(positions.get(2), genotypeExample , new HashSet<>(), 5, 5);
    private final Animal animal4 = new Animal(positions.get(3), genotypeExample , new HashSet<>(), 5, 5);
    private final Animal animal5 = new Animal(positions.get(4) , genotypeExample , new HashSet<>(), 5, 5);
    private final Animal animal6 = new Animal(positions.get(0) , genotypeExample , new HashSet<>(), 5, 5);
    private final Animal animal7 = new Animal(positions.get(1) , genotypeExample , new HashSet<>(), 5, 5);
    private final Animal animal8 = new Animal(positions.get(6) , genotypeExample , new HashSet<>(), 5, 5);


    @Test
    void placeTest(){

        final GrassField map1 = new GrassField(configuration1);
        try { assertTrue(map1.place(animal1));}
        catch (IncorrectPositionException e) {fail(e.getMessage());}
        try { assertTrue(map1.place(animal6));}
        catch (IncorrectPositionException e) {fail(e.getMessage());}
        try { assertTrue(map1.place(animal2));}
        catch (IncorrectPositionException e) {fail(e.getMessage());}
        try { assertTrue(map1.place(animal3));}
        catch (IncorrectPositionException e) {fail(e.getMessage());}
        try { assertTrue(map1.place(animal4));}
        catch (IncorrectPositionException e) {fail(e.getMessage());}

        assertThrows(IncorrectPositionException.class, () -> map1.place(animal5));
    }

    @Test
    void removeTest(){
        final GrassField map1 = new GrassField(configuration1);

        try { assertTrue(map1.place(animal1));}
        catch (IncorrectPositionException e) {fail(e.getMessage());}
        try { assertTrue(map1.place(animal6));}
        catch (IncorrectPositionException e) {fail(e.getMessage());}

        map1.remove(animal1);
        assertEquals(List.of(animal6), map1.animalsAt(animal1.getPosition()));

        map1.remove(animal1);
        assertEquals(List.of(animal6), map1.animalsAt(animal1.getPosition()));

        try { assertTrue(map1.place(animal2));}
        catch (IncorrectPositionException e) {fail(e.getMessage());}
        map1.remove(animal2);
        assertNull(map1.animalsAt(animal2.getPosition()));
    }


    @Test
    void moveTest(){
        final GrassField map1 = new GrassField(configuration1);

        try { assertTrue(map1.place(animal2));}
        catch (IncorrectPositionException e) {fail(e.getMessage());}

        Vector2d animal1OldDirection = animal2.getDirection();
        Vector2d animalOldPosition = animal2.getPosition();
        map1.move(animal2);

        assertNotNull(animal2.getPosition().add(animal1OldDirection));
        assertNull(map1.animalsAt(animalOldPosition));

    }


    @Test
    void canMoveToTest(){
        final GrassField map1 = new GrassField(configuration2);

        try { assertTrue(map1.place(animal1));}
        catch (IncorrectPositionException e) {fail(e.getMessage());}
        try { assertTrue(map1.place(animal3));}
        catch (IncorrectPositionException e) {fail(e.getMessage());}

        assertEquals(positions.get(0), map1.canMoveTo(positions.get(0)));
        assertEquals(positions.get(2), map1.canMoveTo(positions.get(2)));
        assertNull(map1.canMoveTo(positions.get(1)));
        assertNull(map1.canMoveTo(positions.get(3)));
        assertEquals(new Vector2d(0, positions.getLast().getY()), map1.canMoveTo(positions.getLast()));
    }


    @Test
    void getElementsTest(){
        final GrassField map = new GrassField(configuration1);
        try { assertTrue(map.place(animal1));}
        catch (IncorrectPositionException e) {fail(e.getMessage());}

        List<WorldElement> elements = map.getElements();

        assertEquals(11, elements.size());
        assertEquals(animal1, elements.getFirst());

        try { assertTrue(map.place(animal6));}
        catch (IncorrectPositionException e) {fail(e.getMessage());}

        List<WorldElement> elements2 = map.getElements();

        assertEquals(12, elements2.size());

    }

    @Test
    void animalsAtTest(){
        final GrassField map = new GrassField(configuration1);
        try { assertTrue(map.place(animal1));}
        catch (IncorrectPositionException e) {fail(e.getMessage());}
        try { assertTrue(map.place(animal6));}
        catch (IncorrectPositionException e) {fail(e.getMessage());}
        try { assertTrue(map.place(animal2));}
        catch (IncorrectPositionException e) {fail(e.getMessage());}

        assertNull(map.animalsAt(positions.get(2)));
        assertEquals(List.of(animal2), map.animalsAt(animal2.getPosition()));
        assertEquals(List.of(animal1, animal6), map.animalsAt(animal1.getPosition()));
    }


    @Test
    void grassAt(){
        final GrassField map = new GrassField(configuration3);
        assertNotNull(map.grassAt(new Vector2d(2,2)));

        final GrassField map2 = new GrassField(configuration2);
        assertNull(map2.grassAt(new Vector2d(2,2)));
    }

    @Test
    void consumePlants(){
        final GrassField map4 = new GrassField(configuration1);
        final GrassField map1 = new GrassField(configuration4);
        assertEquals(9, map1.getNumberOfGrasses());
        try { assertTrue(map1.place(animal8));}
        catch (IncorrectPositionException e) {fail(e.getMessage());}
        // 1 zwierze, trawa na kazdym polu
        map1.consumePlants();
        assertEquals(8, map1.getNumberOfGrasses());

        final GrassField map2 = new GrassField(configuration5);
        assertEquals(1, map2.getNumberOfGrasses());
        try { assertTrue(map2.place(animal8));}
        catch (IncorrectPositionException e) {fail(e.getMessage());}
        map2.consumePlants();
        assertEquals(0, map2.getNumberOfGrasses());

        final GrassField map3 = new GrassField(configuration6);
        assertEquals(9, map3.getNumberOfGrasses());
        map3.consumePlants();
        assertEquals(9, map3.getNumberOfGrasses());
    }

    @Test
    void reproduceTest(){
        final Animal animal13 = new Animal(positions.get(6) , genotypeExample , new HashSet<>(), 5, 5);
        final Animal animal9 = new Animal(positions.get(6) , genotypeExample , new HashSet<>(), 5, 5);
        final Animal animal10 = new Animal(positions.get(6) , genotypeExample , new HashSet<>(), 5, 5);


        final GrassField map1 = new GrassField(configuration5);
        assertEquals("[]", map1.reproduce().toString());

        try { assertTrue(map1.place(animal13));}
        catch (IncorrectPositionException e) {fail(e.getMessage());}
        assertEquals("[]", map1.reproduce().toString());

        try { assertTrue(map1.place(animal9));}
        catch (IncorrectPositionException e) {fail(e.getMessage());}
        assertEquals("[A]", map1.reproduce().toString());

        try { assertTrue(map1.place(animal10));}
        catch (IncorrectPositionException e) {fail(e.getMessage());}
        assertEquals("[A]", map1.reproduce().toString());

        final Animal animal14 = new Animal(positions.get(6) , genotypeExample , new HashSet<>(), 5, 5);
        final Animal animal15 = new Animal(positions.get(6) , genotypeExample , new HashSet<>(), 5, 5);
        final Animal animal11 = new Animal(positions.get(6) , genotypeExample , new HashSet<>(), 5, 5);
        final Animal animal12 = new Animal(positions.get(6) , genotypeExample , new HashSet<>(), 5, 1);

        final GrassField map2 = new GrassField(configuration5);
        try { assertTrue(map2.place(animal13));}
        catch (IncorrectPositionException e) {fail(e.getMessage());}
        try { assertTrue(map2.place(animal14));}
        catch (IncorrectPositionException e) {fail(e.getMessage());}
        try { assertTrue(map2.place(animal15));}
        catch (IncorrectPositionException e) {fail(e.getMessage());}
        try { assertTrue(map2.place(animal11));}
        catch (IncorrectPositionException e) {fail(e.getMessage());}
        assertEquals("[A]", map2.reproduce().toString());

        final GrassField map3 = new GrassField(configuration5);
        try { assertTrue(map3.place(animal12));}
        catch (IncorrectPositionException e) {fail(e.getMessage());}
        try { assertTrue(map3.place(animal11));}
        catch (IncorrectPositionException e) {fail(e.getMessage());}
        assertEquals("[]", map3.reproduce().toString());
    }

}
