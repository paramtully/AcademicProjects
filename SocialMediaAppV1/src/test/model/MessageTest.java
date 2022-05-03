package model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MessageTest {
    Message message;

    @Test
    public void testConstructor() {
        Person james = new Person("James", "male", 22, "abcd");
        message = new Message("What are you doing today", james);

        assertEquals("What are you doing today", message.getCommunication());
        assertEquals(james, message.getPerson());
    }
}
