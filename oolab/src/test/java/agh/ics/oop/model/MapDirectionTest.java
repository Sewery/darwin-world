package agh.ics.oop.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MapDirectionTest {

    @Test
    void next() {
        assertEquals(MapDirection.NORTH.next(1), MapDirection.NORTHEAST);
        assertEquals(MapDirection.EAST.next(1), MapDirection.SOUTHEAST);
        assertEquals(MapDirection.SOUTH.next(1), MapDirection.SOUTHWEST);
        assertEquals(MapDirection.WEST.next(1), MapDirection.NORTHWEST);
    }

    @Test
    void previous() {
        assertEquals(MapDirection.NORTH.previous(), MapDirection.NORTHWEST);
        assertEquals(MapDirection.EAST.previous(), MapDirection.NORTHEAST);
        assertEquals(MapDirection.SOUTH.previous(), MapDirection.SOUTHEAST);
        assertEquals(MapDirection.WEST.previous(), MapDirection.SOUTHWEST);
    }

    @Test
    void changeDirection(){
        assertEquals(MapDirection.NORTH.changeDirection(1), MapDirection.NORTHEAST);
        assertEquals(MapDirection.EAST.changeDirection(2), MapDirection.SOUTH);
        assertEquals(MapDirection.SOUTH.changeDirection(7), MapDirection.SOUTHEAST);
        assertEquals(MapDirection.WEST.changeDirection(0), MapDirection.WEST);
    }

}

