package agh.ics.oop.core;

public interface ConfigurationManager {
    default void validate( Configuration config) throws ConfigurationInvalidException {
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
    }
    Configuration loadConfiguration();
}
