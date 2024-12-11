package agh.ics.oop.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GrassTest {

    private final Grass grass1 = new Grass(new Vector2d(1,1));

    @Test
    void getPositionTest(){
        assertEquals(new Vector2d(1,1), grass1.getPosition());
    }

    @Test
    void toStringTest(){
        assertEquals("*", grass1.toString());
    }
}
