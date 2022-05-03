package model;

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

import java.util.ArrayList;
import java.util.List;

// represents the network in which the profiles exist; the top level of the hierarchy
public class Network implements Writable {

    List<Person> people;

    // EFFECTS: Initializes this network's list of people
    public Network() {
        people = new ArrayList<>();
    }

    // getter
    public List<Person> getPeople() {
        return people;
    }

    // MODIFIES: this
    // EFFECTS: adds person to this network
    public void addPerson(Person person) {
        people.add(person);
        EventLog.getInstance().logEvent(new Event(person.getName()
                + " was added to the network"));
    }

    // EFFECTS: returns list of person with name name
    public List<Person> searchPeople(String name) {
        List<Person> matches = new ArrayList<>();
        for (Person person : people) {
            if (person.getName().equals(name)) {
                matches.add(person);
            }
        }
        return matches;
    }

    // EFFECTS: returns person with matching ID if found, null otherwise
    public Person searchByID(int id) {
        Person match = null;
        for (Person person : people) {
            int personID = person.hashCode();
            if (id == personID) {
                match = person;
            }
        }
        return match;
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        for (Person person : people) {
            jsonArray.put(person.toJson());
        }
        json.put("people", jsonArray);
        return json;
    }


}
