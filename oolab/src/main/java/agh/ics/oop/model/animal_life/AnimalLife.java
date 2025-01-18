package agh.ics.oop.model.animal_life;

public record AnimalLife(
        Integer initialEnergyOfAnimals,
        Integer energyPerGrass,
        Integer minNumberOfMutations,
        Integer maxNumberOfMutations,
        Integer genotypeLength,
        Boolean ageOfBurden
) {
}
