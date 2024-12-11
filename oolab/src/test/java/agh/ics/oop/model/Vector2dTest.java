package agh.ics.oop.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class Vector2dTest {

    final Vector2d v1 = new Vector2d(1,2);
    final Vector2d v2 = new Vector2d(-1,-2);
    final Vector2d v3 = new Vector2d(-1,2);
    final Vector2d v4 = new Vector2d(0,0);

    @Test
    void testToString(){
        assertEquals("(1,2)",v1.toString());
        assertEquals("(-1,-2)",v2.toString());
        assertEquals("(-1,2)",v3.toString());
        assertEquals("(0,0)",v4.toString());
    }
    @Test
    void precedes(){
        assertFalse(v3.precedes(v4));
        assertFalse(v3.precedes(v2));
        assertTrue(v3.precedes(v1));
        assertTrue(v4.precedes(v1));
        assertTrue(v2.precedes(v1));
        assertFalse(v1.precedes(v2));
        assertFalse(v1.precedes(v3));
        assertFalse(v1.precedes(v4));
        assertTrue(v2.precedes(v3));
        assertTrue(v2.precedes(v4));
        assertFalse(v4.precedes(v2));
        assertFalse(v4.precedes(v3));
        assertTrue(v4.precedes(v4));
    }
    @Test
    void follows(){
        assertFalse(v3.follows(v4));
        assertTrue(v3.follows(v2));
        assertFalse(v3.follows(v1));
        assertFalse(v4.follows(v1));
        assertFalse(v2.follows(v1));
        assertTrue(v1.follows(v2));
        assertTrue(v1.follows(v3));
        assertTrue(v1.follows(v4));
        assertFalse(v2.follows(v3));
        assertFalse(v2.follows(v4));
        assertTrue(v4.follows(v2));
        assertFalse(v4.follows(v3));
        assertTrue(v4.follows(v4));
    }
    @Test
    public void upperRight(){
        assertEquals(new Vector2d(1,2), v1.upperRight(v2));
        assertEquals(new Vector2d(-1,2), v2.upperRight(v3));
        assertEquals(new Vector2d(0,0), v2.upperRight(v4));
        assertEquals(new Vector2d(0,2), v3.upperRight(v4));
        assertEquals(new Vector2d(1,2), v1.upperRight(v4));
    }
    @Test
    public void lowerLeft(){
        assertEquals(new Vector2d(-1,-2), v1.lowerLeft(v2));
        assertEquals(new Vector2d(-1,-2), v2.lowerLeft(v3));
        assertEquals(new Vector2d(-1,-2), v2.lowerLeft(v4));
        assertEquals(new Vector2d(-1,0), v3.lowerLeft(v4));
        assertEquals(new Vector2d(0,0), v1.lowerLeft(v4));
    }
    @Test
    public void add(){
        assertEquals(new Vector2d(0,0), v1.add(v2));
        assertEquals(new Vector2d(-2,0), v2.add(v3));
        assertEquals(new Vector2d(-1,-2), v4.add(v2));
        assertEquals(new Vector2d(-1,2), v4.add(v3));
        assertEquals(new Vector2d(0,0), v4.add(v4));
    }
    @Test
    public void subtract(){
        assertEquals(new Vector2d(2,4), v1.subtract(v2));
        assertEquals(new Vector2d(0,-4), v2.subtract(v3));
        assertEquals(new Vector2d(1,2), v4.subtract(v2));
        assertEquals(new Vector2d(1,-2), v4.subtract(v3));
        assertEquals(new Vector2d(0,0), v4.subtract(v4));
    }
    @Test
    public void opposite(){
        assertEquals(v1, v2.opposite());
        assertEquals(v2, v1.opposite());
        assertEquals(v4, v4.opposite());
        assertEquals(new Vector2d(1,-2), v3.opposite());
    }
    @Test
    void testEquals() {
        assertFalse(v1.equals(v2));
        assertTrue(v1.equals(v1));
    }
}


