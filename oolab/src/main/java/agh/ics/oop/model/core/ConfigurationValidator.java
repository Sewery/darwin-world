package agh.ics.oop.model.core;

public class ConfigurationValidator {
    public static void validate( Configuration config) throws ConfigurationInvalidException {
        if (config.initialNumberOfAnimals() > config.width() * config.height()) {
            throw new ConfigurationInvalidException("Initial number of animals must be smaller than value width x height");
        }
        if (config.initialNumberOfGrasses() > config.width() * config.height()) {
            throw new ConfigurationInvalidException("Initial number of grasses must be smaller than value width x height");
        }
        if (config.numberOfNewGrassesEachDay() > config.width() * config.height()) {
            throw new ConfigurationInvalidException("Number of new grasses must be smaller than value width x heigh");
        }
        if (config.minNumberOfMutations() > config.maxNumberOfMutations()) {
            throw new ConfigurationInvalidException("Max number of mutations must  equal or lower than min number of mutations");
        }
        if (config.maxNumberOfMutations() > config.genotypeLength()) {
            throw new ConfigurationInvalidException("Max number of mutations must be equal or lower than min number of genotype length");
        }
        if (2*config.energyToReproduce() < config.initialEnergyOfAnimals()) {
            throw new ConfigurationInvalidException("Energy to reproduce should be at least the half of initial animal energy");
        }
    }
}
