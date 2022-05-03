package persistence;

import model.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

// Represents a reader that reads network JSON data stored in file
/*
    CREDIT: JsonSerializationDemo - UBC CPSC210
    - Used general structure of reader

*/
public class JsonReader {
    private String source;

    // EFFECTS: constructs reader to read from source file
    public JsonReader(String source) {
        this.source = source;
    }

    // EFFECTS: reads network from file and returns it;
    //          throws IOException if an error occurs reading data from file
    public Network read() throws IOException {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        return parseNetwork(jsonObject);
    }

    // EFFECTS: reads source file as string and returns it
    private String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s));
        }

        return contentBuilder.toString();
    }

    // EFFECTS: parses network from JSON object and returns it
    private Network parseNetwork(JSONObject jsonObject) {
        Network nw = new Network();
        addPeople(nw, jsonObject);
        return nw;
    }

    // MODIFIES: nw
    // EFFECTS: parses people from JSON file and adds it to application
    private void addPeople(Network nw, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("people");
        for (Object json : jsonArray) {
            JSONObject person = (JSONObject) json;
            addPerson(nw, person);
        }

        setPersonInMessages(nw);
    }

    // MODIFIES: nw
    // EFFECTS: sets person that sent each message
    private void setPersonInMessages(Network nw) {
        List<Person> people = nw.getPeople();
        for (Person person : people) {
            for (MessageThread mt : person.getMessageThreads()) {
                for (Message message : mt.getMessages()) {
                    message.setPerson(nw.searchByID(message.getPersonID()));
                }
            }
        }
    }

    // MODIFIES: nw
    // EFFECTS: parses person from JSON file and adds it to application
    private void addPerson(Network nw, JSONObject jsonObject) {
        String name = jsonObject.getString("name");
        String sex = jsonObject.getString("sex");
        int age = jsonObject.getInt("age");
        String password = jsonObject.getString("password");

        Person person = new Person(name, sex, age, password);

        addHobbies(person, jsonObject);
        setProfile(person.getProfile(), jsonObject.getJSONObject("profile"));
        addMessageThreads(nw, person, jsonObject);
        addFriends(nw, person, jsonObject);

        person.logout();
        nw.addPerson(person);
    }

    // MODIFIES: nw, person
    // EFFECTS: retrieves person's friends from JSON file and adds it to application
    private void addFriends(Network nw, Person person, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("friendIDs");
        for (Object json : jsonArray) {
            int friendID = (int) json;
            Person friend = nw.searchByID(friendID);
            if (friend != null) {
                person.addFriend(friend);
            }
        }
    }

    // MODIFIES: nw, person
    // EFFECTS: parses hobbies from JSON file and adds it to application
    private void addHobbies(Person person, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("hobbies");

        for (Object json : jsonArray) {
            String hobby = (String) json;
            person.addHobby(hobby);
        }
    }

    // MODIFIES: nw, profile
    // EFFECTS: parses profile from JSON file and adds it to application
    private void setProfile(Profile profile, JSONObject jsonObject) {
        String bio = jsonObject.getString("bio");
        profile.changeBio(bio);

        JSONArray jsonArray = jsonObject.getJSONArray("posts");
        for (Object json : jsonArray) {
            JSONObject post = (JSONObject) json;
            addPost(profile, post);
        }
    }

    // MODIFIES: nw, profile
    // EFFECTS: parses post from JSON file and adds it to application
    private void addPost(Profile profile, JSONObject jsonObject) {
        String post = jsonObject.getString("post");
        String date =  jsonObject.getString("date");
        Post p1 = new Post(post, date);
        profile.getPosts().add(p1);
    }

    // MODIFIES: nw, person
    // EFFECTS: parses messageThreads from JSON file and adds it to application
    private void addMessageThreads(Network nw, Person person, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("messageThreads");
        for (Object json : jsonArray) {
            JSONObject messageThread = (JSONObject) json;
            addMessageThread(nw, person, messageThread);
        }
    }

    // MODIFIES: nw, person
    // EFFECTS: parses a messageThread from JSON file and adds it to application
    private void addMessageThread(Network nw, Person person, JSONObject jsonObject) {
        Person foundOther = searchPersonJsonIDs(nw, person, "personIDs", jsonObject);

        if (foundOther != null) {
            addExistingMessageThread(person, foundOther);
        } else {
            List<Integer> personIDs = getPersonIDs(jsonObject);
            MessageThread messageThread = new MessageThread(personIDs);
            messageThread.getPersons().add(person);

            addMessages(messageThread, jsonObject);
            person.getMessageThreads().add(messageThread);
        }
    }

    // MODIFIES: nw
    // EFFECTS: searches for person by their ID, returns person if found,
    //                                            otherwise returns null
    private Person searchPersonJsonIDs(Network nw, Person person, String key, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray(key);
        for (Object json : jsonArray) {
            Person other = nw.searchByID((int)json);
            if (other != null && other != person) {
                return other;
            }
        }
        return null;
    }

    // MODIFIES: nw
    // EFFECTS: searches for existing messageThread and adds to person's
    //          message thread if found
    private void addExistingMessageThread(Person me, Person other) {
        List<MessageThread> otherMT = other.getMessageThreads();
        for (MessageThread mt : otherMT) {
            List<Integer> personIDs =  mt.getPersonIDs();

            int myId = me.hashCode();
            for (int id : personIDs) {
                if (id == myId) {
                    mt.getPersons().add(me);
                    me.getMessageThreads().add(mt);
                }
            }
        }
    }

    // MODIFIES: nw
    // EFFECTS: parses personIDs from JSON file and returns it
    private List<Integer> getPersonIDs(JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("personIDs");
        List<Integer> personIDs = new ArrayList<>();
        for (Object json : jsonArray) {
            int personID = (int) json;
            personIDs.add(personID);
        }
        return personIDs;
    }

    // MODIFIES: nw
    // EFFECTS: parses messages from JSON file and adds it to application
    private void addMessages(MessageThread messageThread, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("messages");
        for (Object json : jsonArray) {
            JSONObject message = (JSONObject) json;
            addMessage(messageThread, message);
        }
    }

    // MODIFIES: nw
    // EFFECTS: parses message from JSON file and adds it to application
    private void addMessage(MessageThread messageThread, JSONObject jsonObject) {
        String communication = jsonObject.getString("communication");
        int personID = jsonObject.getInt("personID");

        Message message = new Message(communication, personID);
        messageThread.getMessages().add(message);
    }

}
