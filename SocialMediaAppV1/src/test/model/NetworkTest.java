package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class NetworkTest {

    Network network;
    Person james;
    Person charles;

    @BeforeEach
    public void setup() {
        network = new Network();
        james = new Person("James", "male", 22, "abcd");
        charles = new Person("Charles", "male", 20, "1234");
        network.addPerson(james);
        network.addPerson(charles);
    }

    @Test
    public void testConstructor() {
        Network nw = new Network();
        assertEquals(0, nw.getPeople().size());
    }

    @Test
    public void testAddPerson() {

        assertEquals(2, network.getPeople().size());
        assertTrue(network.getPeople().contains(james));
        assertTrue(network.getPeople().contains(charles));
    }

    @Test
    public void testSearchPersonOneReturned() {
        List<Person> returnedList = network.searchPeople("James");

        assertEquals(1, returnedList.size());
        assertTrue(returnedList.contains(james));
    }

    @Test
    public void testSearchPersonFewReturned() {
        Person jamesD1 = new Person("James Doe", "male", 25, "abcd");
        Person jamesD2 = new Person("James Doe", "male", 22, "abcd");
        network.addPerson(jamesD1);
        network.addPerson(jamesD2);
        List<Person> returnedList = network.searchPeople("James Doe");

        assertEquals(2, returnedList.size());
        assertTrue(returnedList.contains(jamesD1));
        assertTrue(returnedList.contains(jamesD2));
    }

    @Test
    public void testSearchByID() {
        Person john = new Person("John Doe", "male", 25, "abcd");

        Person notFound = network.searchByID(john.hashCode());
        assertNull(notFound);

        Person foundJames = network.searchByID(james.hashCode());
        assertEquals(james, foundJames);
        Person foundCharles = network.searchByID(charles.hashCode());
        assertEquals(charles, foundCharles);
    }

}
