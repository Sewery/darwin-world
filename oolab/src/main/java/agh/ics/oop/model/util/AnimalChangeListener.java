package agh.ics.oop.model.util;

import agh.ics.oop.model.animal_life.Animal;

public interface AnimalChangeListener {
    void animalChanged(Animal animal,String message);
}
