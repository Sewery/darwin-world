package agh.ics.oop;

import agh.ics.oop.model.*;
import org.junit.jupiter.api.Test;

import java.util.List;

import static agh.ics.oop.OptionsParser.parse;
import static org.junit.jupiter.api.Assertions.*;

public class SimulationTest {

    /*
    private final List<MoveDirection> moves_expected = List.of(
            MoveDirection.LEFT,
            MoveDirection.RIGHT,
            MoveDirection.BACKWARD,
            MoveDirection.RIGHT,
            MoveDirection.FORWARD
    );
    private final String[] args = {"l", "r", "x", "x", "b", "r", "f"};
    private final String[] argsValid = {"l", "r", "b", "r", "f"};

    private final List<Vector2d> positions = List.of(   new Vector2d(2,1),
            new Vector2d(3,3),
            new Vector2d(0,4)
    );
    private final List<Vector2d> positions2 = List.of(      new Vector2d(0,0),
            new Vector2d(4,4),
            new Vector2d(0,4),
            new Vector2d(4,0)
    );

    private final List<Vector2d> outOfBoundsPositions = List.of(
            new Vector2d(-1,-1),
            new Vector2d(5,5),
            new Vector2d(-2,5),
            new Vector2d(6,2)
    );

    private final String[] args2 = {"b", "f", "l", "r", "b", "f", "f", "f"};
    private final String[] args3 = {"f", "b", "l", "l", "f", "f", "r", "r", "l", "f", "f", "f", "f", "f", "f", "f"};

    private final List<MoveDirection> change_orient = List.of(
            MoveDirection.LEFT,
            MoveDirection.RIGHT
    );

    private final RectangularMap map = new RectangularMap(5,5);

    @Test
    void testCreateAnimals() {
        Simulation simulation = new Simulation(positions, moves_expected, map);
        assertEquals(simulation.getAnimal(0).toStringFull(), "(2,1) Północ");
        assertEquals(simulation.getAnimal(1).toStringFull(), "(3,3) Północ");
        assertEquals(simulation.getAnimal(2).toStringFull(), "(0,4) Północ");
    }

    @Test
    void testCreateMoves() {
        assertThrows(IllegalArgumentException.class, () -> new Simulation(positions, parse(args), map));
        Simulation simulation = new Simulation(positions, parse(argsValid), map);
        assertEquals(moves_expected, simulation.getMoves());


    }

    @Test
    void testChangeOrientation() {
        Simulation simulation = new Simulation(positions, change_orient, map);

        simulation.run();
        assertEquals(simulation.getAnimal(0).toStringFull(), "(2,1) Zachód");
        assertEquals(simulation.getAnimal(1).toStringFull(), "(3,3) Wschód");

        simulation.run();
        assertEquals(simulation.getAnimal(0).toStringFull(), "(2,1) Południe");
        assertEquals(simulation.getAnimal(1).toStringFull(), "(3,3) Południe");

        simulation.run();
        assertEquals(simulation.getAnimal(0).toStringFull(), "(2,1) Wschód");
        assertEquals(simulation.getAnimal(1).toStringFull(), "(3,3) Zachód");

        simulation.run();
        assertEquals(simulation.getAnimal(0).toStringFull(), "(2,1) Północ");
        assertEquals(simulation.getAnimal(1).toStringFull(), "(3,3) Północ");

    }

    @Test
    void testMoveOutOfBounds() {
        Simulation simulation = new Simulation(positions2, parse(args2), map);
        simulation.run();

        assertEquals(simulation.getAnimal(0).toStringFull(), "(0,0) Północ");
        assertEquals(simulation.getAnimal(1).toStringFull(), "(4,4) Północ");
        assertEquals(simulation.getAnimal(2).toStringFull(), "(0,4) Zachód");
        assertEquals(simulation.getAnimal(3).toStringFull(), "(4,0) Wschód");

    }

    @Test
    void testRun(){
        Simulation simulation = new Simulation(positions, parse(args3), map);
        simulation.run();

        assertEquals(simulation.getAnimal(0).toStringFull(), "(2,4) Północ");
        assertEquals(simulation.getAnimal(1).toStringFull(), "(4,3) Wschód");
        assertEquals(simulation.getAnimal(2).toStringFull(), "(0,2) Południe");

        assertEquals(map.toString(), " y\\x  0 1 2 3 4\n" +
                "  5: -----------\n" +
                "  4: | | |^| | |\n" +
                "  3: | | | | |>|\n" +
                "  2: |v| | | | |\n" +
                "  1: | | | | | |\n" +
                "  0: | | | | | |\n" +
                " -1: -----------\n");
    }

     */

}
