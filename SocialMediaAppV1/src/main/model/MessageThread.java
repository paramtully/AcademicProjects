package model;

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

import java.util.ArrayList;
import java.util.List;

// represents the message thread shared between multiple people
public class MessageThread implements Writable {
    private List<Message> messages;
    private List<Person> persons;
    private List<Integer> personIDs;

    // EFFECTS: initialized this with persons p1 and p2, and list of messages
    public MessageThread(Person p1, Person p2) {
        messages = new ArrayList<>();
        persons = new ArrayList<>();
        persons.add(p1);
        persons.add(p2);
        EventLog.getInstance().logEvent(new Event(p1.getName()
                + " created a message thread with " + p2.getName()));
    }

    // EFFECTS: initialized this empty message thread
    public MessageThread(List<Integer> personIDs) {
        messages = new ArrayList<>();
        persons = new ArrayList<>();
        this.personIDs = personIDs;
    }

    // getters
    public List<Message> getMessages() {
        return messages;
    }

    public List<Person> getPersons() {
        return persons;
    }

    public List<Integer> getPersonIDs() {
        return personIDs;
    }

    // MODIFIES: this
    // EFFECTS: adds message to this list of messages
    public void addMessage(Message message) {
        messages.add(message);
        EventLog.getInstance().logEvent(new Event(message.getPerson().getName()
                + " added a message to message thread between " + persons.get(0).getName()
                + " and " + persons.get(1).getName()));
    }

    // EFFECTS: returns this message thread as a JSONObject
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("messages", messageThreadToJson());
        json.put("personIDs", personsToJson());
        return json;
    }

    // EFFECTS: returns JsonArray of messages
    private JSONArray messageThreadToJson() {
        JSONArray jsonArray = new JSONArray();
        for (Message message : messages) {
            jsonArray.put(message.toJson());
        }
        return jsonArray;
    }

    // EFFECTS: returns personIDs as JSONArray
    private JSONArray personsToJson() {
        JSONArray jsonArray = new JSONArray();
        for (Person person : persons) {
            jsonArray.put(person.hashCode());
        }
        return jsonArray;
    }
}
