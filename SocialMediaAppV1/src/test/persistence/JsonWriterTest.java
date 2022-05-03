package persistence;

import model.*;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class JsonWriterTest extends JsonTest {

    @Test
    public void testWriterInvalidFile() {
        try {
            Network nw = new Network();
            JsonWriter writer = new JsonWriter("./data/my\0illegal:fileName.json");
            writer.open();
            writer.write(nw);
            writer.close();
            fail("Expected FileNotFoundException");
        } catch (FileNotFoundException e) {
           // pass
        }
    }

    @Test
    public void testWriterEmptyNetwork() {
        try {
            Network nw = new Network();
            JsonWriter writer = new JsonWriter("./data/testWriterEmptyNetwork.json");
            writer.open();
            writer.write(nw);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterEmptyNetwork.json");
            nw = reader.read();
            assertEquals(0, nw.getPeople().size());
        } catch (FileNotFoundException e) {
            fail("Caught FileNotFoundException");
        } catch (IOException e) {
           fail("Caught IOException");
        }
    }

    @Test
    public void testWriterGeneralNetwork() {
        try {
            Network nw = new Network();
            Person p1 = new Person("John", "male", 20, "123");
            Person p2 = new Person("Doe", "female", 20, "321");
            initGeneralTest(p1, p2);
            nw.addPerson(p1);
            nw.addPerson(p2);
            int p1ID = p1.hashCode();
            int p2ID = p2.hashCode();

            JsonWriter writer = new JsonWriter("./data/testWriterGeneralNetwork.json");
            writer.open();
            writer.write(nw);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterGeneralNetwork.json");
            nw = reader.read();
            List<Person> readPeople = nw.getPeople();
            assertEquals(2, readPeople.size());

            checkPerson(p1, nw.getPeople().get(0), "123");
            checkProfile(p1.getProfile(),
                    readPeople.get(0).getProfile());

            checkPerson(p2, nw.getPeople().get(1), "321");
            checkProfile(p2.getProfile(),
                    readPeople.get(1).getProfile());

        } catch (FileNotFoundException e) {
            fail("Caught FileNotFoundException");
        } catch (IOException e) {
            fail("Caught IOException");
        }
    }

}
