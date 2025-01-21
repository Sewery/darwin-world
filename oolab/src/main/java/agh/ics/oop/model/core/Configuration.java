package agh.ics.oop.model.core;

public record Configuration(
        Integer height,
        Integer width,
        Integer initialNumberOfGrasses,
        Integer numberOfNewGrassesEachDay,
        Integer energyPerGrass,
        Integer initialNumberOfAnimals,
        Integer initialEnergyOfAnimals,
        Integer energyToReproduce,
        Integer minNumberOfMutations,
        Integer maxNumberOfMutations,
        Integer genotypeLength,
        MapStrategy mapStrategy,
        AnimalsBehaviourStrategy animalsBehaviourStrategy
) {

    public enum MapStrategy{
        GLOBE("Globe"),
        POLES("Poles");
        private final String text;
        MapStrategy(String text){
            this.text = text;
        }

        @Override
        public String toString() {
            return text;
        }
        public static MapStrategy fromString(String text){
            return switch(text){
                case "Globe" -> GLOBE;
                case "Poles" -> POLES;
                default -> throw new IllegalArgumentException("Invalid map strategy: " + text);
            };
        }

    }
    public enum AnimalsBehaviourStrategy{
        FORESTED_EQUATOR("Forested Equators"),
        AGE_OF_BURDEN("Age of Burden");
        private final String text;
        AnimalsBehaviourStrategy(String text){
            this.text = text;
        }

        @Override
        public String toString() {
            return text;
        }
        public static AnimalsBehaviourStrategy fromString(String text){
            return switch(text){
                case "Forested Equators" -> FORESTED_EQUATOR;
                case "Age of Burden" -> AGE_OF_BURDEN;
                default -> throw new IllegalArgumentException("Invalid animal behaviour strategy: " + text);
            };
        }
    }
}
