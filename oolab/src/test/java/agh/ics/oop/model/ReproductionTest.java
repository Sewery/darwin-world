package agh.ics.oop.model;

import agh.ics.oop.core.Configuration;
import agh.ics.oop.model.animal_life.Animal;
import agh.ics.oop.model.animal_life.AnimalLife;
import agh.ics.oop.model.animal_life.Reproduction;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;


import static org.junit.jupiter.api.Assertions.*;

public class ReproductionTest {
    private final AnimalLife animalLife1 = new AnimalLife(6, 2, 0, 0, 4, false);

    private final List<Vector2d> positions = List.of(
            new Vector2d(1, 0),
            new Vector2d(4, 4)
    );

    int[] genotypeExample1 = {1, 2, 3, 4};
    int[] genotypeExample2 = {5, 5, 5, 5};


    private final Animal parent1_1 = new Animal (positions.get(0), genotypeExample1 , new HashSet<>(), 5, 6);
    private final Animal parent1_2 = new Animal(positions.get(0), genotypeExample2 , new HashSet<>(), 5, 12);
    private final Reproduction reproduction1 = new Reproduction(parent1_1, parent1_2, animalLife1);
    Animal child1 = reproduction1.createAChild();

    private final AnimalLife animalLife2 = new AnimalLife(8, 3, 1, 3, 8, false);
    int[] genotypeExample2_1 = {1, 2, 3, 4, 1, 2, 3, 4};
    int[] genotypeExample2_2 = {5, 5, 5, 5, 5, 5, 5, 5};

    private final Animal parent2_1 = new Animal(positions.get(0), genotypeExample2_1, new HashSet<>(), 4, 10);
    private final Animal parent2_2 = new Animal(positions.get(0), genotypeExample2_2, new HashSet<>(), 6, 30);
    private final Reproduction reproduction2 = new Reproduction(parent2_1, parent2_2, animalLife2);
    Animal child2 = reproduction2.createAChild();

    @Test
    void testChildGenotypeLength() {
        Animal child1 = reproduction2.createAChild();
        assertEquals(8, child1.getGenotype().length);
        Animal child2 = reproduction1.createAChild();
        assertEquals(4, child2.getGenotype().length);
    }

    @Test
    void testNoMutation() {
        final Animal parent3_1 = new Animal(positions.get(0), genotypeExample2_1, new HashSet<>(), 4, 10);
        final Animal parent3_2 = new Animal(positions.get(0), genotypeExample2_2, new HashSet<>(), 6, 30);

        AnimalLife animalLifeWithNoMutation = new AnimalLife(8, 3, 0, 0, 8, false);
        Reproduction reproductionWithNoMutation = new Reproduction(parent3_1, parent3_2, animalLifeWithNoMutation);

        int[] originalChildGenotype = reproductionWithNoMutation.createAChild().getGenotype();
        int[] expectedGenotype1 = new int[8];
        System.arraycopy(genotypeExample2_1, 0, expectedGenotype1, 0, 2);
        System.arraycopy(genotypeExample2_2, 0, expectedGenotype1, 2, 6);
        int[] expectedGenotype2 = new int[8];
        System.arraycopy(genotypeExample2_2, 0, expectedGenotype2, 0, 6);
        System.arraycopy(genotypeExample2_1, 0, expectedGenotype2, 6, 2);

        assertTrue(
                Arrays.equals(expectedGenotype1, originalChildGenotype) ||
                        Arrays.equals(expectedGenotype2, originalChildGenotype)
        );

    }

    @Test
    void testRandomMutation() {
        int[] childGenotype = child2.getGenotype();

        assertEquals(8, childGenotype.length);

        int changes = 0;
        for (int i = 0; i < genotypeExample1.length; i++) {
            if (childGenotype[i] != genotypeExample1[i] && childGenotype[i] != genotypeExample2[i]) {
                changes++;
            }
        }
        assertTrue(changes <= animalLife2.maxNumberOfMutations());
    }

    @Test
    void testEnergyTransfer() {
        assertEquals(3, parent1_1.getEnergy());
        assertEquals(9, parent1_2.getEnergy());
        assertEquals(6, child1.getEnergy());

        assertEquals(6, parent2_1.getEnergy());
        assertEquals(26, parent2_2.getEnergy());
        assertEquals(8, child2.getEnergy());
    }

    @Test
    void testDescendantsCount() {
        assertEquals(1, parent1_1.getNumberOfDescendants());
        assertEquals(1, parent1_2.getNumberOfDescendants());
    }

    @Test
    void testAncestors() {
        assertTrue(child1.getAncestors().contains(parent1_1));
        assertTrue(child1.getAncestors().contains(parent1_2));
    }

    @Test
    void testPositionInheritance() {
        assertEquals(parent1_1.getPosition(), child1.getPosition());
    }

}













