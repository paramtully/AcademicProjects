package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PersonTest {

    Person james;

    @BeforeEach
    public void setup() {
        james = new Person("James", "male", 22, "abcd");
        james.logout();
    }

    @Test
    public void testConstructor() {

        james.login("abcd");
        List<String> jamesHobbies = james.getHobbies();
        Profile jamesProfile = james.getProfile();
        List<MessageThread> messages = james.getMessageThreads();

        assertEquals("James", james.getName());
        assertEquals("male", james.getSex());
        assertEquals(22, james.getAge());
        assertTrue(james.isLoggedIn());
        assertEquals(0, jamesHobbies.size());
        assertNotNull(jamesProfile);
        assertEquals(0, james.getFriends().size());
        assertEquals(0, messages.size());
    }

    @Test
    public void testAddOneHobby() {
        james.addHobby("canoeing");
        List<String> hobbies = james.getHobbies();

        assertEquals(1, hobbies.size());
        assertTrue(hobbies.contains("canoeing"));
    }

    @Test
    public void testAddFewHobbies() {
        for(int i = 1; i <= 3; i++) {
            String hobby = Integer.toString(i);
            james.addHobby(hobby);
        }

        List<String> hobbies = james.getHobbies();
        assertEquals(3, hobbies.size());
        assertTrue(hobbies.contains(Integer.toString(1)));
        assertTrue(hobbies.contains(Integer.toString(2)));
        assertTrue(hobbies.contains(Integer.toString(3)));

    }

    @Test
    public void testAddManyHobbies() {
        List<String> hobbies = james.getHobbies();

        for(int i = 1; i <= 100; i++) {
            String hobby = Integer.toString(i);
            james.addHobby(hobby);

            assertEquals(i, hobbies.size());
            assertTrue(hobbies.contains(hobby));
        }
    }

    @Test
    public void testRemoveHobbyLength1() {
        james.addHobby("canoeing");
        List<String> hobbies = james.getHobbies();

        james.removeHobby("canoeing");
        assertEquals(0, hobbies.size());
    }

    @Test
    public void testRemoveOneHobbyLengthFew() {
        for(int i = 1; i <= 3; i++) {
            String hobby = Integer.toString(i);
            james.addHobby(hobby);
        }
        List<String> hobbies = james.getHobbies();
        james.removeHobby(Integer.toString(2));

        assertEquals(2, hobbies.size());
        assertTrue(hobbies.contains(Integer.toString(1)));
        assertTrue(hobbies.contains(Integer.toString(3)));

        james.removeHobby(Integer.toString(1));

        assertEquals(1, hobbies.size());
        assertTrue(hobbies.contains(Integer.toString(3)));

        james.removeHobby(Integer.toString(3));
        assertEquals(0, hobbies.size());

    }

    @Test
    public void testRemoveManyHobbies() {

        List<String> hobbies = james.getHobbies();

        for(int i = 1; i <= 100; i++) {
            String hobby = Integer.toString(i);
            james.addHobby(hobby);
        }

        for(int i = 100; i > 0; i--) {
            String hobby = Integer.toString(i);
            james.removeHobby(hobby);

            assertEquals(i - 1, hobbies.size());
            assertFalse(hobbies.contains(hobby));
        }
    }

    @Test
    public void testLoginAndLogout() {
        assertFalse(james.isLoggedIn());

        boolean result = james.login("abc");
        assertFalse(result);
        assertFalse(james.isLoggedIn());

        result = james.login("abcd");
        assertTrue(result);
        assertTrue(james.isLoggedIn());

        james.logout();
        assertFalse(james.isLoggedIn());
    }

    @Test
    public void testAddOneFriend() {
        Person charles = new Person("Charles", "male", 20, "1234");
        james.addFriend(charles);
        List<Person> jamesFriends = james.getFriends();
        List<Person> charlesFriends = charles.getFriends();

        assertEquals(1, jamesFriends.size());
        assertTrue(jamesFriends.contains(charles));
        assertEquals(1, charlesFriends.size());
        assertTrue(charlesFriends.contains(james));
    }

    @Test
    public void testAddFewFriend() {
        Person charles = new Person("Charles", "male", 20, "1234");
        Person jenny = new Person("Jenny", "female", 22, "acdc");
        Person abigale = new Person("Abigale", "female", 23, "bcda");

        james.addFriend(charles);
        james.addFriend(jenny);
        james.addFriend(abigale);

        List<Person> jamesFriends = james.getFriends();
        List<Person> charlesFriends = charles.getFriends();
        List<Person> jennyFriends = jenny.getFriends();
        List<Person> abigaleFriends = abigale.getFriends();

        assertEquals(3,jamesFriends.size());
        assertTrue(jamesFriends.contains(charles));
        assertTrue(jamesFriends.contains(jenny));
        assertTrue(jamesFriends.contains(abigale));

        assertEquals(1, charlesFriends.size());
        assertTrue(charlesFriends.contains(james));
        assertEquals(1, jennyFriends.size());
        assertTrue(jennyFriends.contains(james));
        assertEquals(1, abigaleFriends.size());
        assertTrue(abigaleFriends.contains(james));
    }

    @Test
    public void testRemoveOneFriend() {
        Person charles = new Person("Charles", "male", 20, "1234");
        james.addFriend(charles);
        james.removeFriend(charles);
        List<Person> jamesFriends = james.getFriends();
        List<Person> charlesFriends = charles.getFriends();

        assertEquals(0, jamesFriends.size());
        assertEquals(0, charlesFriends.size());
    }

    @Test
    public void testRemoveFewFriends() {
        Person charles = new Person("Charles", "male", 20, "1234");
        Person jenny = new Person("Jenny", "female", 22, "acdc");
        Person abigale = new Person("Abigale", "female", 23, "bcda");

        james.addFriend(charles);
        james.addFriend(jenny);
        james.addFriend(abigale);
        jenny.addFriend(charles);

        james.removeFriend((jenny));

        List<Person> jamesFriends = james.getFriends();
        List<Person> charlesFriends = charles.getFriends();
        List<Person> jennyFriends = jenny.getFriends();
        List<Person> abigaleFriends = abigale.getFriends();

        assertEquals(2,jamesFriends.size());
        assertTrue(jamesFriends.contains(charles));
        assertTrue(jamesFriends.contains(abigale));

        assertEquals(2, charlesFriends.size());
        assertTrue(charlesFriends.contains(james));
        assertTrue(charlesFriends.contains(jenny));

        james.removeFriend(charles);
        james.removeFriend(abigale);

        assertEquals(0, jamesFriends.size());

        assertEquals(1, charlesFriends.size());
        assertTrue(charlesFriends.contains(jenny));

        assertEquals(1, jennyFriends.size());
        assertTrue(jennyFriends.contains(charles));

        assertEquals(0, abigaleFriends.size());

    }

    @Test
    public void testSendFirstMessage() {
        Person charles = new Person("Charles", "male", 20, "1234");
        james.sendMessage(charles, "Hi charles!");
        Message m1 = james.getMessageThreads().get(0).getMessages().get(0);
        Message m2 = charles.getMessageThreads().get(0).getMessages().get(0);
        Message m3 = new Message("Hi charles!", james);

        assertEquals(1, james.getMessageThreads().size());
        assertEquals(1, charles.getMessageThreads().size());
        assertEquals(m1.getPerson(), m3.getPerson());
        assertEquals(m1.getCommunication(), m3.getCommunication());
        assertEquals(m2.getPerson(), m3.getPerson());
        assertEquals(m2.getCommunication(), m3.getCommunication());
    }

    @Test
    public void testSendSecondMessage() {
        Person charles = new Person("Charles", "male", 20, "1234");
        james.sendMessage(charles, "Hi charles!");
        james.sendMessage(charles, "How are you?");
        List<Message> messages = james.getMessageThreads().get(0).getMessages();
        Message m1 = james.getMessageThreads().get(0).getMessages().get(0);
        Message m2 = charles.getMessageThreads().get(0).getMessages().get(0);
        Message m3 = new Message("Hi charles!", james);
        Message m4 = james.getMessageThreads().get(0).getMessages().get(1);
        Message m5 = charles.getMessageThreads().get(0).getMessages().get(1);
        Message m6 = new Message("How are you?", james);

        assertEquals(1, james.getMessageThreads().size());
        assertEquals(1, charles.getMessageThreads().size());
        assertEquals(m1.getPerson(), m3.getPerson());
        assertEquals(m1.getCommunication(), m3.getCommunication());
        assertEquals(m2.getPerson(), m3.getPerson());
        assertEquals(m2.getCommunication(), m3.getCommunication());

        assertEquals(m4.getPerson(), m6.getPerson());
        assertEquals(m4.getCommunication(), m6.getCommunication());
        assertEquals(m5.getPerson(), m6.getPerson());
        assertEquals(m5.getCommunication(), m6.getCommunication());
    }

    @Test
    public void testSendMessagesToMultiplePeople() {
        Person charles = new Person("Charles", "male", 20, "1234");
        Person jenny = new Person("Jenny", "female", 22, "acdc");
        james.sendMessage(charles, "Hi charles!");
        jenny.sendMessage(james, "How are you?");

        Message m1 = james.getMessageThreads().get(1).getMessages().get(0);
        Message m2 = jenny.getMessageThreads().get(0).getMessages().get(0);
        Message m3 = new Message("How are you?", jenny);

        assertEquals(2, james.getMessageThreads().size());
        assertEquals(1, charles.getMessageThreads().size());
        assertEquals(1, jenny.getMessageThreads().size());
        assertEquals(m1.getPerson(), m3.getPerson());
        assertEquals(m1.getCommunication(), m3.getCommunication());
        assertEquals(m2.getPerson(), m3.getPerson());
        assertEquals(m2.getCommunication(), m3.getCommunication());
    }

    @Test
    public void testEqualsFails() {
       boolean expectFalse = james.equals(null);
        assertFalse(expectFalse);

        Integer diffClass = 5;
        expectFalse = james.equals(diffClass);
        assertFalse(expectFalse);
    }

}
