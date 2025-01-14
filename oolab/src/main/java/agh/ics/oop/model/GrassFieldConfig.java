package agh.ics.oop.model;

public record GrassFieldConfig(
        Integer width,
        Integer height,
        Integer numberOfNewGrassesEachDay,
        Integer energyToReproduce,
        Boolean poles
) {
}
