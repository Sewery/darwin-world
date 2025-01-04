package agh.ics.oop.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MapDirectionTest {

    @Test
    void next() {
        assertEquals(MapDirection.NORTH.next(1), MapDirection.EAST);
        assertEquals(MapDirection.EAST.next(1), MapDirection.SOUTH);
        assertEquals(MapDirection.SOUTH.next(1), MapDirection.WEST);
        assertEquals(MapDirection.WEST.next(1), MapDirection.NORTH);
    }

    @Test
    void previous() {
        assertEquals(MapDirection.NORTH.previous(), MapDirection.WEST);
        assertEquals(MapDirection.EAST.previous(), MapDirection.NORTH);
        assertEquals(MapDirection.SOUTH.previous(), MapDirection.EAST);
        assertEquals(MapDirection.WEST.previous(), MapDirection.SOUTH);
    }

}

