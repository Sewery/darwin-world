package agh.ics.oop;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class OptionsParserTest {

    @Test
    void testForward() {
        String[] move = {"f"};
        MoveDirection[] expected = {MoveDirection.FORWARD};
        assertEquals(expected.length, OptionsParser.parse(move).size());
        assertEquals(expected[0], OptionsParser.parse(move).getFirst());
    }

    @Test
    void testBackward() {
        String[] move = {"b"};
        MoveDirection[] expected = {MoveDirection.BACKWARD};
        assertEquals(expected.length, OptionsParser.parse(move).size());
        assertEquals(expected[0], OptionsParser.parse(move).getFirst());
    }

    @Test
    void testRight() {
        String[] move = {"r"};
        MoveDirection[] expected = {MoveDirection.RIGHT};
        assertEquals(expected.length, OptionsParser.parse(move).size());
        assertEquals(expected[0], OptionsParser.parse(move).getFirst());
    }

    @Test
    void testLeft() {
        String[] move = {"l"};
        MoveDirection[] expected ={MoveDirection.LEFT};
        assertEquals(expected.length, OptionsParser.parse(move).size());
        assertEquals(expected[0], OptionsParser.parse(move).getFirst());
    }

    @Test
    void testUnknown() {
        String[] move = {"x"};
        assertThrows(IllegalArgumentException.class, () -> OptionsParser.parse(move));
    }

    @Test
    void testMoreArguments() {
        String[] moves = {"l", "r", "b", "l"};
        MoveDirection[] expected = {MoveDirection.LEFT, MoveDirection.RIGHT, MoveDirection.BACKWARD, MoveDirection.LEFT};
        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i], OptionsParser.parse(moves).get(i));
        }
        assertEquals(expected.length, OptionsParser.parse(moves).size());
    }
}
