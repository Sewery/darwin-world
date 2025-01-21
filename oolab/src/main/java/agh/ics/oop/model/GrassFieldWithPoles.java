package agh.ics.oop.model;

import agh.ics.oop.model.core.Configuration;
import agh.ics.oop.model.animal_life.Animal;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.max;

public class GrassFieldWithPoles extends GrassField {

    public GrassFieldWithPoles(Configuration config) {
        super(config);
    }

    @Override
    public void move(Animal animal) {

        List<Animal> oldList = animalsAt(animal.getPosition());
        if (oldList != null){
            if (oldList.contains(animal)) {
                remove(animal);
                animal.move(this, getPoleEffect(animal.getPosition()));
                super.animals.computeIfAbsent(animal.getPosition(), _ -> new ArrayList<>()).add(animal);

            }
        }
    }

    public int getPoleEffect(Vector2d position) {

        int max_pole_effect = equatorUpperBound-equatorLowerBound + 2;

        if (position.getY() < equatorLowerBound) {
            return max(max_pole_effect - position.getY(), 1);}
        else if (position.getY() > equatorUpperBound) {
            return max(max_pole_effect - (config.height()-1 - position.getY()), 1);}


        return 1;
    }
}
