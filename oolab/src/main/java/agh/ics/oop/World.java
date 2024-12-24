package agh.ics.oop;

import agh.ics.oop.model.*;
import agh.ics.oop.model.util.ConsoleMapDisplay;
import javafx.application.Application;

import java.util.ArrayList;
import java.util.List;

public class World {

    public static void main(String[] args) {

        try {
            WorldMap map = new GrassField(10, 5, 5);
            Simulation simulation = new Simulation(map, 5, 10, 5, 4, 4);
            map.addObserver(new ConsoleMapDisplay());
            SimulationEngine engine = new SimulationEngine(List.of(simulation));
            engine.runASyncInThreadPool();
            engine.awaitSimulationsEnd();
        }
        catch (IllegalArgumentException | InterruptedException e) {
            System.out.println("Exception: " + e.getMessage());
        }


        /*
        try {
            List<MoveDirection> directions = OptionsParser.parse(args);
            List<Vector2d> positions = List.of(new Vector2d(2, 2), new Vector2d(3, 4));
            List<Simulation> simulations = new ArrayList<>();

            for (int i = 0; i < 10; i++) {

                WorldMap map = i%2 == 0 ? new RectangularMap(5, 5) :  new GrassField(10);
                map.addObserver(new ConsoleMapDisplay());

                simulations.add(new Simulation(positions, directions, map));
            }

            SimulationEngine engine = new SimulationEngine(simulations);
            engine.runASyncInThreadPool();
            engine.awaitSimulationsEnd();

            System.out.println("system zakończyl dzialanie");

        }
        catch (IllegalArgumentException | InterruptedException e) {
            System.out.println("Exception: " + e.getMessage());
        }

         */

        /*
        try {
            List<MoveDirection> directions = OptionsParser.parse(args);
            List<Vector2d> positions = List.of(new Vector2d(2, 2), new Vector2d(3, 4));

            WorldMap map = new RectangularMap(5, 5);
            map.addObserver(new ConsoleMapDisplay());

            Simulation simulation = new Simulation(positions, directions, map);

            WorldMap map2 = new GrassField(10);
            map2.addObserver(new ConsoleMapDisplay());
            List<Vector2d> positions2 = List.of(new Vector2d(2, 2), new Vector2d(3, 4));
            Simulation simulation2 = new Simulation(positions2, directions, map2);

            List<Simulation> simulations = List.of(simulation2, simulation);

            SimulationEngine engine = new SimulationEngine(simulations);
            engine.runAsync();

            System.out.println("system zakończył działanie");

        }
        catch (IllegalArgumentException e) {
            System.out.println("Exception: " + e.getMessage());
        }
        */

        /*
        Animal animal = new Animal();
        System.out.println(animal.toString());

        System.out.println("system wystartował");
        run(args);
        System.out.println("system zakończył działanie");

        Vector2d position1 = new Vector2d(1,2);
        System.out.println(position1);
        Vector2d position2 = new Vector2d(-2,1);
        System.out.println(position2);
        System.out.println(position1.add(position2));

        MapDirection move1 = MapDirection.NORTH;
        System.out.println(move1);
        MapDirection move2 = MapDirection.WEST;
        System.out.println(move2);
        System.out.println(move1.next());
        System.out.println(move2.previous());
        System.out.println(move1.toUnitVector());
       */
    }

    /*
    private static void run(String[] given_moves) {

        List<MoveDirection> moves = OptionsParser.parse(given_moves);
        for (int i = 0; i < moves.size(); i++) {

            String result = switch (moves.get(i)) {
                case FORWARD -> "Zwierzak idzie do przodu";
                case BACKWARD -> "Zwierzak idzie do tyłu";
                case RIGHT -> "Zwierzak skręca w prawo";
                case LEFT -> "Zwierzak skręca w lewo";
            };
            System.out.print(result);

            if (i <  moves.size() - 1) {
                System.out.print(", ");
            }
        }
        System.out.println();

    }

         */

}
