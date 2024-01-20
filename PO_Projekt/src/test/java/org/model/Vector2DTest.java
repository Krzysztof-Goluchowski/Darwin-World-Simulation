package org.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class Vector2DTest {

    @Test
    public void testEquals(){
        Vector2D vector1 = new Vector2D(10, 13);
        Vector2D vector2 = new Vector2D(10, 13);
        Vector2D vector3 = new Vector2D(10, 14);

        assertEquals(vector1, vector2);
        assertNotEquals(vector1, vector3);
        assertNotEquals(vector3, vector2);
    }

    @Test
    public void testAdd(){
        Vector2D vector1 = new Vector2D(10, 2);
        Vector2D vector2 = new Vector2D(2, 10);
        Vector2D vector3 = new Vector2D(12, 12);
        Vector2D vector4 = new Vector2D(122, 0);

        Vector2D vector1PlusVector2 = vector1.add(vector2);

        assertEquals(vector1PlusVector2, vector3);
        assertNotEquals(vector1, vector2);
        assertNotEquals(vector3, vector2);
        assertNotEquals(vector4, vector2);
    }

    @Test
    public void testToString(){
        Vector2D vector1 = new Vector2D(10, 13);
        String positionOfVector1 = "Vector2D{x=10, y=13}";
        String positionOfNotVector1 = "Vector2D{x=10, y=12}";

        assertEquals(positionOfVector1, vector1.toString());
        assertNotEquals(positionOfNotVector1, vector1.toString());
    }
}
