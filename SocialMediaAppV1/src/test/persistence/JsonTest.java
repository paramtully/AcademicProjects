package persistence;

import model.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class JsonTest {

    protected void checkPerson(Person person, Person readPerson, String password) {
        assertEquals(person.getName(), readPerson.getName());
        assertEquals(person.getSex(), readPerson.getSex());
        assertEquals(person.getAge(), readPerson.getAge());
        assertNotEquals(null, readPerson.getProfile());
        assertEquals(person.getFriends().size(), readPerson.getFriends().size());
        assertEquals(person.getMessageThreads().size(), readPerson.getMessageThreads().size());
        assertEquals(person.getHobbies().size(), readPerson.getHobbies().size());

        List<String> personsHobbies = person.getHobbies();
        List<String> readPersonsHobbies = person.getHobbies();
        for(String hobby: personsHobbies) {
            assertTrue(readPersonsHobbies.contains(hobby));
        }

        int i = 0;
        List<MessageThread> MessageThreads = person.getMessageThreads();
        List<MessageThread> readMessageThreads = person.getMessageThreads();
        for(MessageThread mt : MessageThreads) {
            checkMessageThread(mt, readMessageThreads.get(i));
            i++;
        }

        assertFalse(readPerson.isLoggedIn());
        assertTrue(readPerson.login(password));
        assertTrue(readPerson.isLoggedIn());
    }

    protected void checkProfile(Profile profile, Profile readProfile) {
        assertEquals(profile.getBio(), readProfile.getBio());
        assertEquals(profile.getPerson(), readProfile.getPerson());
        assertEquals(profile.getPosts().size(), readProfile.getPosts().size());

        int i = 0;
        List<Post> posts = profile.getPosts();
        List<Post> readPosts = readProfile.getPosts();
        for (Post post: posts) {
            checkPost(post, readPosts.get(i));
            i++;
        }
    }

    protected void checkPost(Post post, Post readPost) {
        assertEquals(post.getPost(), readPost.getPost());
        assertEquals(post.getDate(), readPost.getDate());
    }

    protected void checkMessageThread(MessageThread messageThread, MessageThread readMessageThread) {
        assertEquals(messageThread.getMessages().size(), readMessageThread.getMessages().size());
        assertEquals(messageThread.getPersons().size(), readMessageThread.getPersons().size());

        int i = 0;
        List<Message> messages = messageThread.getMessages();
        List<Message> readMessages = readMessageThread.getMessages();
        for (Message message : messages) {
            checkMessage(message, readMessages.get(i));
            i++;
        }

        i = 0;
        List<Person> persons = messageThread.getPersons();
        List<Person> readPersons = readMessageThread.getPersons();
        for (Person person : persons) {
            assertEquals(person, readPersons.get(i));
            i++;
        }
    }

    protected void checkMessage(Message message, Message readMessage) {
        assertEquals(message.getCommunication(), readMessage.getCommunication());
        assertEquals(message.getPerson(), readMessage.getPerson());
    }

    protected void initGeneralTest(Person p1, Person p2) {
        p1.logout();
        p1.addHobby("basketball");
        p1.addHobby("watching movies");

        Profile pf1 = p1.getProfile();
        pf1.changeBio("Studying at UBC");
        Post post1 = new Post("I have 2 exams tomorrow", "Fri Oct 29 15:32:10 PDT 2021");
        Post post2 = new Post("I need more sleep", "Fri Oct 29 15:32:17 PDT 2021");
        pf1.getPosts().add(post1);
        pf1.getPosts().add(post2);

        p2.logout();
        p2.addFriend(p1);
        p2.addHobby("tennis");
        p2.sendMessage(p1, "Hi, how are you!");
        p1.sendMessage(p2, "I'm doing great, thanks for asking!");

        Profile pf2 = p2.getProfile();
        Post post3 = new Post("I need to wake up early tomorrow", "Fri Oct 27 11:32:10 PDT 2021");
        pf2.getPosts().add(post3);
    }
}
