package agh.ics.oop.model;

import agh.ics.oop.core.Configuration;
import agh.ics.oop.model.animal_life.Animal;
import org.junit.jupiter.api.Test;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

public class AnimalTest {

    int[] genotypeExample = {0, 1, 2};
    private final Animal animal0 = new Animal(new Vector2d(3,4), genotypeExample, new HashSet<>(), 5, 5);
    private final Animal animal1 = new Animal(new Vector2d(1,2), genotypeExample, new HashSet<>(), 5, 5);
    private final Animal animal2 = new Animal(new Vector2d(3,4), genotypeExample, new HashSet<>(), 5, 5);
    private final Animal animal3 = new Animal(new Vector2d(3,3), genotypeExample, new HashSet<>(), 5, 5);
    private final Animal animal4 = new Animal(new Vector2d(3,3), genotypeExample, new HashSet<>(), 5, 1);
    private final Configuration configuration1 = new Configuration(10, 10, 10, 1, 5, 5, 5, 1, 1, 1, 3, Configuration.MapStrategy.GLOBE , Configuration.AnimalsBehaviourStrategy.FORESTED_EQUATOR);
    final GrassField map = new GrassField(configuration1);

    @Test
    void testIncreaseAge(){
        assertEquals(-1, animal0.getAge());
        animal0.increaseAge();
        assertEquals(0, animal0.getAge());
    }

    @Test
    void testIsAt(){
        assertTrue(animal0.isAt(new Vector2d(3, 4)));
        assertFalse(animal1.isAt(new Vector2d(3, 4)));
    }

     @Test
    void testMove(){
         try { assertTrue(map.place(animal3));}
         catch (IncorrectPositionException e) {fail(e.getMessage());}
         Vector2d oldPosition = animal3.getPosition();
         int currentGene = animal3.getCurrentGene();
         animal3.move(map, 1);
         assertEquals((currentGene+1)%genotypeExample.length, animal3.getCurrentGene());
         assertNotEquals(oldPosition, animal3.getPosition());
         assertEquals(4, animal3.getEnergy());

         try { assertTrue(map.place(animal4));}
         catch (IncorrectPositionException e) {fail(e.getMessage());}
         Vector2d oldPosition2 = animal4.getPosition();
         int currentGene2 = animal3.getCurrentGene();
         animal4.move(map, 3);
         assertEquals((currentGene2+1)%genotypeExample.length, animal4.getCurrentGene());
         assertEquals(oldPosition2, animal4.getPosition());
         assertEquals(1, animal4.getEnergy());

     }

     @Test
    void testEat(){
         assertEquals(0, animal1.getPlantsEaten());
         assertEquals(5, animal1.getEnergy());
         animal1.eat();
         assertEquals(1, animal1.getPlantsEaten());
         assertEquals(10, animal1.getEnergy());
     }

     @Test
    void testReproduce(){
         assertEquals(0, animal2.getNumberOfChildren());
         assertEquals(5, animal2.getEnergy());
         animal2.reproduce(2);
         assertEquals(1, animal2.getNumberOfChildren());
         assertEquals(3, animal2.getEnergy());
     }


}
