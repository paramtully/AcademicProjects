package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

class ProfileTest {

    Person james;
    Profile jamesProfile;

    @BeforeEach
    public void setup() {
        james = new Person("James", "male", 22, "abcd");
        jamesProfile = james.getProfile();
    }

    @Test
    public void testConstructor() {
        assertEquals(james, jamesProfile.getPerson());
        assertEquals("", jamesProfile.getBio());
        assertEquals(0, jamesProfile.getPosts().size());
    }

    @Test
    public void testChangeBio() {
        jamesProfile.changeBio("In School");

        assertEquals("In School", jamesProfile.getBio());
    }

    @Test
    public void testMakeOnePost() {
        jamesProfile.makePost("I have a basketball game this week. So excited!");
        Post p1 = new Post("I have a basketball game this week. So excited!");
        Post testPost = jamesProfile.getPosts().get(0);

        assertEquals(1, jamesProfile.getPosts().size());
        assertEquals(p1.getPost(), testPost.getPost());
        assertEquals(p1.getDate(), testPost.getDate());
    }

    @Test
    public void testMakeFewPost() {
        jamesProfile.makePost("I have a basketball game this week. So excited!");
        jamesProfile.makePost("We won the game!");
        jamesProfile.makePost("lots of traffic this morning");
        Post testPost1 = jamesProfile.getPosts().get(0);
        Post testPost2 = jamesProfile.getPosts().get(1);
        Post testPost3 = jamesProfile.getPosts().get(2);
        Post p1 = new Post("I have a basketball game this week. So excited!");
        Post p2 = new Post("We won the game!");
        Post p3 = new Post("lots of traffic this morning");

        assertEquals(3, jamesProfile.getPosts().size());
        assertEquals(p1.getPost(), testPost1.getPost());
        assertEquals(p1.getDate().toString(), testPost1.getDate());
        assertEquals(p2.getPost(), testPost2.getPost());
        assertEquals(p2.getDate().toString(), testPost2.getDate());
        assertEquals(p3.getPost(), testPost3.getPost());
        assertEquals(p3.getDate().toString(), testPost3.getDate());
    }
}