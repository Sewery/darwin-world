package agh.ics.oop.model.animal_life;

import agh.ics.oop.model.MoveValidator;
import agh.ics.oop.model.Vector2d;

public interface AnimalInterface {

    void setDead(int dayOfDeath);
    Integer getDayOfDeath();
    void increaseAge();
    boolean isAt(Vector2d position);
    Vector2d getDirection();

    void move(MoveValidator moveValidator, int energyMultiplier);
    void eat();
    void reproduce(int lostEnergy);
}
