package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MessageThreadTest {
    Person james;
    Person charles;
    MessageThread ms1;

    @BeforeEach
    public void setup() {
        james = new Person("James", "male", 22, "abcd");
        charles = new Person("Charles", "male", 20, "1234");
        ms1 = new MessageThread(james, charles);
    }

    @Test
    public void testConstructor() {
        List<Message> messages = ms1.getMessages();
        List<Person> persons = ms1.getPersons();

        assertEquals(0, messages.size());
        assertTrue(persons.contains(james));
        assertTrue(persons.contains(charles));
    }

    @Test
    public void testAddOneMessage() {
        Message message = new Message("Hi james!", charles);
        ms1.addMessage(message);
        List<Message> myMessages = ms1.getMessages();
        Message myMessage = ms1.getMessages().get(0);

        assertEquals(1, myMessages.size());
        assertTrue(myMessages.contains(myMessage));
        assertEquals(message, myMessage);
    }

    @Test
    public void testAddFewMessage() {
        Message message1 = new Message("Hi james!", charles);
        Message message2 = new Message("Hi charles!", james);
        Message message3 = new Message("What's up.", james);
        ms1.addMessage(message1);
        ms1.addMessage(message2);
        ms1.addMessage(message3);
        List<Message> myMessages = ms1.getMessages();
        Message m1 = myMessages.get(0);
        Message m2 = myMessages.get(1);
        Message m3 = myMessages.get(2);

        assertEquals(3, myMessages.size());
        assertEquals(message1, m1);
        assertEquals(message2, m2);
        assertEquals(message3, m3);
    }
}
