package agh.ics.oop.model;

import agh.ics.oop.model.animal_life.Animal;

import java.util.Comparator;

public class AnimalComparator implements Comparator<Animal> {

    @Override
    public int compare(Animal o1, Animal o2) {

        if(o1.getEnergy() - o2.getEnergy()!=0){
            return o1.getEnergy() - o2.getEnergy();
        }else if(o1.getAge() - o2.getAge()!=0){
            return o1.getAge() - o2.getAge();
        }
        return Integer.compare(o1.getNumberOfChildren(), o2.getNumberOfChildren());
//        if (o1.getEnergy() > o2.getEnergy()){
//            return 1;
//        }
//        else if (o1.getEnergy() < o2.getEnergy()){
//            return -1;
//        }
//        else{
//            if (o1.getAge() > o2.getAge()){
//                return 1;
//            }
//            else if (o1.getAge() < o2.getAge()){
//                return -1;
//            }
//            else{
//                return Integer.compare(o1.getNumberOfChildren(), o2.getNumberOfChildren());
//            }
//        }
    }
}
