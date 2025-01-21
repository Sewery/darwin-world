package agh.ics.oop;

import agh.ics.oop.model.core.Configuration;
import agh.ics.oop.model.core.Statistics;
import agh.ics.oop.model.GrassField;
import agh.ics.oop.model.WorldMap;
import agh.ics.oop.model.animal_life.Animal;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SimulationTest {
    //Config full of animals
    private final Configuration configuration1 = new Configuration(10, 10, 0, 1, 5, 100, 5, 1, 1, 1, 3, Configuration.MapStrategy.GLOBE , Configuration.AnimalsBehaviourStrategy.FORESTED_EQUATOR);
    //Normal config
    private final Configuration configuration2 = new Configuration(4, 7, 5, 1, 5, 8, 5, 1, 1, 1, 3, Configuration.MapStrategy.GLOBE , Configuration.AnimalsBehaviourStrategy.FORESTED_EQUATOR);
    //Normal config with long genotype
    private final Configuration configuration3 = new Configuration(5, 5, 9, 1, 5, 5, 5, 1, 1, 5, 7, Configuration.MapStrategy.GLOBE , Configuration.AnimalsBehaviourStrategy.FORESTED_EQUATOR);
    //Normal config with long genotype
    private final Configuration configuration4 = new Configuration(5, 5, 3, 1, 5, 5, 10, 3, 1, 1, 3, Configuration.MapStrategy.GLOBE , Configuration.AnimalsBehaviourStrategy.FORESTED_EQUATOR);
    //Normal config with fast dying animals
    private final Configuration configuration5 = new Configuration(5, 5, 0, 0, 5, 4, 6, 5, 1, 1, 3, Configuration.MapStrategy.GLOBE , Configuration.AnimalsBehaviourStrategy.FORESTED_EQUATOR);
    //Normal config full of grass
    private final Configuration configuration6 = new Configuration(5, 5, 25, 2, 5, 2, 5, 1, 1, 1, 3, Configuration.MapStrategy.GLOBE , Configuration.AnimalsBehaviourStrategy.FORESTED_EQUATOR);


    @Test
    void removeDeadAnimals() {

        //Set up
        Configuration baseConfig = configuration5;
        WorldMap map = new GrassField(baseConfig);
        Statistics statistics = new Statistics(baseConfig);
        Simulation simulation = new Simulation(map, baseConfig, statistics, false);
        List<Animal> animals = simulation.getAnimals();

        //Make animals move to death
        int animalMaxEnergy = simulation.getMaxEnergy();
        for(int i = 0; i < 2*animalMaxEnergy; i++) {
            simulation.moveAnimals();
        }
        simulation.removeDeadAnimals();
        // All animals should be removed when dead
        assertEquals(0, simulation.getNumberOfAllAnimals(), "All animals should be removed when dead");
    }

    @Test
    void moveAnimals() {
        //Set up
        Configuration baseConfig = configuration3;
        WorldMap map = new GrassField(baseConfig);
        Statistics statistics = new Statistics(baseConfig);
        Simulation simulation = new Simulation(map, baseConfig, statistics, false);
        List<Animal> animals = simulation.getAnimals();

        //move every animal
        simulation.moveAnimals();

        // Ensure all animals have moved to valid positions
        animals.forEach(animal -> {
            assertNotNull(animal.getPosition(), "Animal position should not be null after movement");
        });
    }

    @Test
    void consumePlants() {
        //Set up
        Configuration baseConfig = configuration6;
        WorldMap map = new GrassField(baseConfig);
        Statistics statistics = new Statistics(baseConfig);
        Simulation simulation = new Simulation(map, baseConfig, statistics, false);

        int initialPlants = simulation.getNumberOfAllPlants();

        // Simulate a day when animals consume plants
        simulation.consumePlants();
        int remainingPlants = simulation.getNumberOfAllPlants();
        //Number of plants should not increase after consumption
        assertTrue(remainingPlants <= initialPlants);
    }

    @Test
    void reproduce() {
        //Set up
        Configuration baseConfig = configuration1;
        WorldMap map = new GrassField(baseConfig);
        Statistics statistics = new Statistics(baseConfig);
        Simulation simulation = new Simulation(map, baseConfig, statistics, false);

        int initialAnimalCount = simulation.getNumberOfAllAnimals();
        simulation.moveAnimals();
        simulation.reproduce();
        int newAnimalCount = simulation.getNumberOfAllAnimals();

        assertTrue(newAnimalCount > initialAnimalCount, "Animal count should increase after reproduction");
    }

    @Test
    void growPlants() {
        Configuration baseConfig = configuration2;
        WorldMap map = new GrassField(baseConfig);
        Statistics statistics = new Statistics(baseConfig);
        Simulation simulation = new Simulation(map, baseConfig, statistics, false);

        int initialPlants = simulation.getNumberOfAllPlants();

        // Simulate plant growth
        simulation.growPlants();
        int newPlantCount = simulation.getNumberOfAllPlants();

        assertTrue(newPlantCount > initialPlants, "Number of plants should grow the same");
    }

    @Test
    void averageEnergy() {
        Configuration baseConfig = configuration2;
        WorldMap map = new GrassField(baseConfig);
        Statistics statistics = new Statistics(baseConfig);
        Simulation simulation = new Simulation(map, baseConfig, statistics, false);

        int averageEnergy = simulation.getAverageEnergy();

        //Average energy at start
        assertEquals(averageEnergy, (int) baseConfig.initialEnergyOfAnimals());

        //Moving and trying to reproduce animals
        moveAnimals();
        reproduce();

        averageEnergy = simulation.getAverageEnergy();
        assertTrue(averageEnergy >= 0 && averageEnergy<=baseConfig.initialEnergyOfAnimals(), "Average energy should be non-negative");
    }

    @Test
    void mostCommonGenotypes() {
        Configuration baseConfig = configuration2;
        WorldMap map = new GrassField(baseConfig);
        Statistics statistics = new Statistics(baseConfig);
        Simulation simulation = new Simulation(map, baseConfig, statistics, false);

        var genotypes = simulation.getMostCommonGenotypes();
        assertFalse(genotypes.isEmpty(), "There is always at least one most common genotype");

        //Run day
        removeDeadAnimals();
        moveAnimals();
        consumePlants();
        reproduce();
        growPlants();

        assertFalse(genotypes.isEmpty(), "There is always at least one most common genotype");
    }
}
