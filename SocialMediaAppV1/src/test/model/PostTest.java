package model;

import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PostTest {

    Post p1;

    @Test
    public void testConstructor() {
        p1 = new Post("I'm hungry");
        Date currentTime = new Date();

        assertEquals("I'm hungry", p1.getPost());
        assertEquals(currentTime.toString(), p1.getDate());
    }

    @Test
    public void testConstructor2() {
        Date currentTime = new Date();
        p1 = new Post("I'm hungry", currentTime.toString());

        assertEquals("I'm hungry", p1.getPost());
        assertEquals(currentTime.toString(), p1.getDate());
    }
}
