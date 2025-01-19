package agh.ics.oop.model;

import agh.ics.oop.core.Configuration;
import agh.ics.oop.model.animal_life.Animal;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNull;

public class GrassFieldWithPolesTest {

    private final Configuration configuration1 = new Configuration(10, 10, 10, 1, 5, 5, 5, 1, 1, 1, 3, Configuration.MapStrategy.POLES , Configuration.AnimalsBehaviourStrategy.FORESTED_EQUATOR);
    private final Configuration configuration2 = new Configuration(5, 5, 10, 1, 5, 5, 5, 1, 1, 1, 3, Configuration.MapStrategy.POLES , Configuration.AnimalsBehaviourStrategy.FORESTED_EQUATOR);

    private final List<Vector2d> positions = List.of(
            new Vector2d(1, 1), // pole effect in 1st, no pole effect in 2nd
            new Vector2d(4, 4), // equator in 1st, pole in 2nd
            new Vector2d(2, 2)  // no pole effect in 1st, equator in 2nd
    );

    int[] genotypeExample = {0, 0, 0};

    private final Animal animal1 = new Animal (positions.get(0), genotypeExample , new HashSet<>(), 5, 5);
    private final Animal animal4 = new Animal (positions.get(0), genotypeExample , new HashSet<>(), 5, 1);
    private final Animal animal2 = new Animal(positions.get(1), genotypeExample , new HashSet<>(), 5, 5);
    private final Animal animal3 = new Animal(positions.get(2), genotypeExample , new HashSet<>(), 5, 5);

    private final Animal animal5 = new Animal (positions.get(0), genotypeExample , new HashSet<>(), 5, 5);
    private final Animal animal6 = new Animal(positions.get(1), genotypeExample , new HashSet<>(), 5, 1);

    @Test
    void moveTest(){
        final GrassField map1 = new GrassFieldWithPoles(configuration1);

        try { assertTrue(map1.place(animal1));}
        catch (IncorrectPositionException e) {fail(e.getMessage());}
        try { assertTrue(map1.place(animal2));}
        catch (IncorrectPositionException e) {fail(e.getMessage());}
        try { assertTrue(map1.place(animal3));}
        catch (IncorrectPositionException e) {fail(e.getMessage());}
        try { assertTrue(map1.place(animal4));}
        catch (IncorrectPositionException e) {fail(e.getMessage());}

        Vector2d animal1OldPosition = animal1.getPosition();
        Vector2d animal2OldPosition = animal2.getPosition();
        Vector2d animal3OldPosition = animal3.getPosition();
        Vector2d animal4OldPosition = animal4.getPosition();

        map1.move(animal1);
        map1.move(animal2);
        map1.move(animal3);
        map1.move(animal4);

        assertEquals(3, animal1.getEnergy());
        assertEquals(4, animal2.getEnergy());
        assertEquals(4, animal3.getEnergy());
        assertEquals(1, animal4.getEnergy());

        assertNotEquals(animal1OldPosition, animal1.getPosition());
        assertNotEquals(animal2OldPosition, animal2.getPosition());
        assertNotEquals(animal3OldPosition, animal3.getPosition());
        assertEquals(animal4OldPosition, animal4.getPosition());

        final GrassField map2 = new GrassFieldWithPoles(configuration2);

        try { assertTrue(map2.place(animal5));}
        catch (IncorrectPositionException e) {fail(e.getMessage());}
        try { assertTrue(map2.place(animal6));}
        catch (IncorrectPositionException e) {fail(e.getMessage());}

        Vector2d animal5OldPosition = animal5.getPosition();
        Vector2d animal6OldPosition = animal6.getPosition();

        map2.move(animal5);
        map2.move(animal6);

        assertEquals(4, animal5.getEnergy());
        assertEquals(1, animal6.getEnergy());

        assertNotEquals(animal5OldPosition, animal5.getPosition());
        assertEquals(animal6OldPosition, animal6.getPosition());

    }

}
