package model;

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

import java.util.ArrayList;
import java.util.List;

// represents a person's profile
public class Profile implements Writable {

    private String bio;
    private Person person;
    private List<Post> posts;

    // EFFECT: initializes this profile with person, empty bio, and list of posts
    public Profile(Person person) {
        this.person = person;
        this.bio = "";
        this.posts = new ArrayList<>();
    }

    // getters
    public String getBio() {
        return bio;
    }

    public Person getPerson() {
        return person;
    }

    public List<Post> getPosts() {
        return posts;
    }

    // REQUIRES: this profiles person is logged in
    // MODIFIES: this
    // EFFECT: adds bio to this profile
    public void changeBio(String bio) {
        this.bio = bio;
        EventLog.getInstance().logEvent(new Event(person.getName() + " changed their bio"));
    }

    // REQUIRES: this profiles person is logged in
    // MODIFIES: this
    // EFFECT: adds post to this profile's posts
    public void makePost(String post) {
        Post p1 = new Post(post);
        posts.add(p1);
        EventLog.getInstance().logEvent(new Event(person.getName()
                + " added post to list of posts"));
    }

    // REQUIRES: this profiles person is logged in
    // MODIFIES: this
    // EFFECT: removes post from this profile's posts
    public void deletePost(int index) {
        if (index >= 0 && index < posts.size()) {
            posts.remove(index);
            EventLog.getInstance().logEvent(new Event(person.getName()
                    + " removed a post from list of posts"));
        }
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("bio", bio);
        json.put("posts", postsToJson());
        return json;
    }

    // EFFECTS: returns posts as JSONArray
    private JSONArray postsToJson() {
        JSONArray jsonArray = new JSONArray();
        for (Post post : posts) {
            jsonArray.put(post.toJson());
        }
        return jsonArray;
    }
}
