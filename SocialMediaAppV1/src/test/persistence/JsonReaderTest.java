package persistence;

import model.Network;
import model.Person;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class JsonReaderTest extends JsonTest{

    @Test
    public void testReaderNonExistentFile() {
        JsonReader reader = new JsonReader("./data/noSuchFile.json");
        try {
            Network network = reader.read();
            fail("IOException expected");
        } catch(IOException e) {
            // pass
        }
    }

    @Test
    public void testReaderEmptyNetwork() {
        JsonReader reader = new JsonReader("./data/testReaderEmptyNetwork.json");
        try {
            Network network = reader.read();
            assertEquals(0, network.getPeople().size());
        } catch(IOException e) {
            fail("Couldn't read from file");
        }
    }

    @Test
    public void testReaderGeneralNetwork() {
        JsonReader reader = new JsonReader("./data/testReaderGeneralNetwork.json");
        try {
            Network nw = reader.read();

            Person p1 = new Person("John", "male", 20, "123");
            Person p2 = new Person("Doe", "female", 20, "321");

            initGeneralTest(p1, p2);
            List<Person> readPeople = nw.getPeople();
            assertEquals(2, readPeople.size());

            checkPerson(p1, nw.getPeople().get(0), "123");
            checkProfile(p1.getProfile(),
                    readPeople.get(0).getProfile());

            checkPerson(p2, nw.getPeople().get(1), "321");
            checkProfile(p2.getProfile(),
                    readPeople.get(1).getProfile());

        } catch(IOException e) {
            fail("Couldn't read from file");
        }
    }
}
