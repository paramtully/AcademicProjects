package model;

import org.json.JSONObject;
import persistence.Writable;

// represents a single message
public class Message implements Writable {
    private final String communication;
    private int personID;
    private Person person;

    // EFFECTS: initializes this message with communication and person
    public Message(String communication, Person person) {
        this.communication = communication;
        this.person = person;
    }

    // EFFECTS: initializes this message with communication and personID
    public Message(String communication, int personID) {
        this.communication = communication;
        this.personID = personID;
    }

    // getters
    public Person getPerson() {
        return person;
    }

    public String getCommunication() {
        return communication;
    }

    public int getPersonID() {
        return personID;
    }

    // setters
    public void setPerson(Person person) {
        this.person = person;
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("communication", communication);
        json.put("personID", person.hashCode());
        return json;
    }
}
