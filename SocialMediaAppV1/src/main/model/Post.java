package model;

import org.json.JSONObject;
import persistence.Writable;

import java.util.Date;

// represents a post that would exist within a profile
public class Post implements Writable {
    String post;
    String date;

    // initializes this post with post and time of post
    public Post(String post) {
        this.post = post;
        Date currentDate = new Date();
        date = currentDate.toString();
    }

    // initializes this post with post and time of post
    public Post(String post, String date) {
        this.post = post;
        this.date = date;
    }

    //getters
    public String getDate() {
        return date;
    }

    public String getPost() {
        return post;
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("post", post);
        json.put("date", date);
        return json;
    }
}
