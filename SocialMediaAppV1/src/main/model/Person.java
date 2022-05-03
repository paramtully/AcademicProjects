package model;

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

// represents a person
public class Person implements Writable {

    private final String name;
    private final String sex;
    private final int age;
    private final String password;
    private final List<String> hobbies;
    private boolean loggedIn;
    private final Profile profile;
    private List<Person> friends;
    private List<MessageThread> messageThreads;


    // EFFECT: Constructs this person with a name, age, sex, password,
    //         list of hobbies, list of friends, list of messages
    //         logs person in when creating person and creates a profile for them
    public Person(String name, String sex, int age, String password) {
        this.name = name;
        this.sex = sex;
        this.age = age;
        this.password = password;
        hobbies = new ArrayList<>();
        loggedIn = true;
        profile = new Profile(this);
        friends = new ArrayList<>();
        messageThreads = new ArrayList<>();
        EventLog.getInstance().logEvent(new Event(name + " created a new profile"));
    }

    // getters
    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public String getSex() {
        return sex;
    }

    public List<String> getHobbies() {
        return hobbies;
    }

    public List<Person> getFriends() {
        return friends;
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public Profile getProfile() {
        return profile;
    }

    // REQUIRES: logged in
    public List<MessageThread> getMessageThreads() {
        return messageThreads;
    }

    // REQUIRES: logged in
    // MODIFIES: this
    // EFFECTS: Adds hobby to this person's list of hobbies
    public void addHobby(String hobby) {
        hobbies.add(hobby);
        EventLog.getInstance().logEvent(new Event(
                name + " added a hobby to list of hobbies"));
    }

    // REQUIRES: logged in
    //           hobby is in person's list of hobbies
    // MODIFIES: this
    // EFFECTS: removes hobbies from this person's list of hobbies
    public void removeHobby(String hobby) {
        hobbies.remove(hobby);
        EventLog.getInstance().logEvent(new Event(
                name + " removed a hobby from list of hobbies"));
    }



    // REQUIRES: logged in,
    //           friend not already in this person's list of friends and vise versa
    // MODIFIES: this, friend
    // EFFECTS: adds friend to this person's friend, and adds this person to
    //         friend's list of friends
    public void addFriend(Person friend) {
        friends.add(friend);
        friend.getFriends().add(this);
        EventLog.getInstance().logEvent(new Event(name + " and " + friend.getName()
                + " were added to each other's list of friends"));
    }

    // REQUIRES: logged in,
    //           friend is in this person's list of friends and vise versa,
    // MODIFIES: this, friend
    // EFFECTS: removes friend from this person's friends, and removes this person
    //         from friend's friends
    public void removeFriend(Person friend) {
        friends.remove(friend);
        friend.getFriends().remove(this);
        EventLog.getInstance().logEvent(new Event(name + " and " + friend.getName()
                + " were removed from each other's list of friends"));
    }


    // EFFECTS: if password matches this person's password, logs this person in
    //         and returns true, otherwise returns false
    public boolean login(String password) {
        if (this.password.equals(password)) {
            loggedIn = true;
            EventLog.getInstance().logEvent(new Event(name + " logged in"));
        }
        return loggedIn;
    }

    // REQUIRES: logged in
    // MODIFIES: this
    // EFFECTS: sets this person to logged out
    public void logout() {
        loggedIn = false;
        EventLog.getInstance().logEvent(new Event(name + " logged out"));
    }

    // REQUIRES: logged in
    // MODIFIES: this
    // EFFECTS: adds message to messages between this person and person
    //          creates messages between this person and person if doesn't already exist
    public void sendMessage(Person person, String communication) {
        Message m0 = new Message(communication, this);
        boolean found = false;
        for (MessageThread m1 : messageThreads) {
            List<Person> persons = m1.getPersons();
            if (persons.contains(person)) {
                m1.addMessage(m0);
                found = true;   // changed this from return to boolean to pass coverage
            }
        }
        if (!found) {
            MessageThread m1 = new MessageThread(this, person);
            m1.addMessage(m0);
            this.messageThreads.add(m1);
            person.getMessageThreads().add(m1);
        }

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Person person = (Person) o;
        return age == person.age && name.equals(person.name) && sex.equals(person.sex)
                && password.equals(person.password) && hobbies.equals(person.hobbies);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, sex, age, password, hobbies);
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("name", name);
        json.put("sex", sex);
        json.put("age", age);
        json.put("password", password);
        json.put("hobbies", hobbiesToJson());
        json.put("profile", profile.toJson());
        json.put("friendIDs", friendsToJson());
        json.put("messageThreads", messageThreadsToJson());
        return json;
    }

    // EFFECTS: returns hobbies as JSONArray
    private JSONArray hobbiesToJson() {
        JSONArray jsonArray = new JSONArray();
        for (String hobby : hobbies) {
            jsonArray.put(hobby);
        }
        return jsonArray;
    }

    // EFFECTS: returns friendIDs as JSONArray
    private JSONArray friendsToJson() {
        JSONArray jsonArray = new JSONArray();
        for (Person friend : friends) {
            jsonArray.put(friend.hashCode());
        }
        return jsonArray;
    }

    // EFFECTS: returns messageThreads as JSONArray
    private JSONArray messageThreadsToJson() {
        JSONArray jsonArray = new JSONArray();
        for (MessageThread m1 : messageThreads) {
            jsonArray.put(m1.toJson());
        }
        return jsonArray;
    }
}
