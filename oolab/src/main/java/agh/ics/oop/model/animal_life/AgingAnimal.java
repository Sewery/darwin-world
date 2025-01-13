package agh.ics.oop.model.animal_life;

import agh.ics.oop.model.MoveValidator;
import agh.ics.oop.model.Vector2d;

import java.util.Random;
import java.util.Set;

public class AgingAnimal extends Animal {
    public AgingAnimal(Vector2d position, int[] genotype, Set<Animal> ancestors, int energyPerGrass, int initialEnergyOfAnimals) {
        super(position, genotype, ancestors, energyPerGrass, initialEnergyOfAnimals);
    }

    private boolean isGoingToMove(){
        double probabilityOfAnimalSkippingAMove = Math.min(0.01 * this.age, 0.8);
        return new Random().nextDouble() >= probabilityOfAnimalSkippingAMove;
    }

    @Override
    public void move(MoveValidator validator, int energyMultiplier) {
        if (this.energy >= energyMultiplier) {  // animal on pole can have energy > 0 and not be able to move

            if (!isGoingToMove()) {
                //System.out.println("skipped move");
                energy -= (int) (energyMultiplier);
            } // energy loss when animal is skipping a move because of it's age

            else {
                direction = direction.changeDirection(genotype[currentGene]);
                Vector2d newPosition = validator.canMoveTo(position.add(direction.toUnitVector()));
                if (newPosition != null) {
                    position = newPosition;
                    energy -= energyMultiplier;
                }
            }
        }
        currentGene = (currentGene + 1)%genotype.length;
    }
}
