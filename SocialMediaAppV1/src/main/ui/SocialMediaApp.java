package ui;

import model.*;
import persistence.JsonReader;
import persistence.JsonWriter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

// social media application with text-based UI
public class SocialMediaApp {
    private Network network;
    private Scanner input;
    private Person user;
    private JsonWriter writer;
    private JsonReader reader;

    // EFFECTS: runs the social media application
    public SocialMediaApp() throws IOException {
        runSocialMediaApp();
    }

    // MODIFIES: this
    // EFFECTS: processes user input
    private void runSocialMediaApp() throws IOException {
        boolean keepGoing = true;
        String command;

        init();

        while (keepGoing) {
            displaySaveMenu();
            command = input.next();
            command = command.toLowerCase();

            if (command.equals("q")) {
                keepGoing = false;
                printLog();
            } else {
                processSaveMenuCommand(command);
                runLogIn();
            }
        }
    }

    // EFFECTS: prints log of all events that occurred during session
    private void printLog() {
        for (Event e : EventLog.getInstance()) {
            System.out.println(e.toString());
        }
    }

    // EFFECTS: displays save menu
    private void displaySaveMenu() {
        System.out.println("\nSelect from:");
        System.out.println("\tl -> load last session");
        System.out.println("\tn -> new session");
        System.out.println("\tq -> quit");
    }

    // EFFECTS: processes user command
    private void processSaveMenuCommand(String command) throws IOException {
        if (command.equals("l")) {
            loadLastSession();
        } else if (command.equals("n")) {
            network = new Network();
        } else {
            System.out.println("Selection not valid...");
        }
    }

    // EFFECTS: loads last session from file
    private void loadLastSession() throws IOException {
        network = reader.read();
    }

    // EFFECTS: processes user input
    private void runLogIn() throws FileNotFoundException {
        boolean keepGoing = true;
        String command;

        while (keepGoing) {
            displayLogInMenu();
            command = input.next();
            command = command.toLowerCase();

            if (command.equals("q")) {
                keepGoing = false;
                runAskToSave();
            } else {
                user = processLogInMenuCommand(command);

                if (user != null) {
                    runEnterApp();
                }
            }
        }
    }

    // EFFECTS: processes user input
    private void runAskToSave() throws FileNotFoundException {
        boolean keepGoing = true;
        String command;

        while (keepGoing) {
            displayAskToSaveMenu();
            command = input.next();
            command = command.toLowerCase();

            if (command.equals("y")) {
                keepGoing = false;
                saveSession();
            } else if (command.equals("n")) {
                keepGoing = false;
                EventLog.getInstance().clear();
            } else {
                System.out.println("Selection not valid...");
            }

        }
    }

    // EFFECTS: displays menu of options for user
    private void displayAskToSaveMenu() {
        System.out.println("\nWould you like to save this session?");
        System.out.println("\ty -> yes");
        System.out.println("\tn -> no");
    }


    // EFFECTS: saves current session to JSON File
    private void saveSession() throws FileNotFoundException {
        writer.open();
        writer.write(network);
        writer.close();
    }

    // MODIFIES: this
    // EFFECTS: initializes social media app
    private void init() {
        input = new Scanner(System.in);
        input.useDelimiter("\n");
        reader = new JsonReader("./data/network.json");
        writer = new JsonWriter("./data/network.json");
    }

    // EFFECTS: displays menu of options to the user
    private void displayLogInMenu() {
        System.out.println("\nSelect from:");
        System.out.println("\tl -> log in");
        System.out.println("\tc -> create account");
        System.out.println("\tq -> go back");
    }

    // MODIFIES: this
    // EFFECTS: processes user command
    private Person processLogInMenuCommand(String command) {
        if (command.equals("l")) {
            user = login();
        } else if (command.equals("c")) {
            user = createUser();
        } else {
            System.out.println("Selection not valid...");
            return null;
        }
        return user;
    }

    // MODIFIES: this
    // EFFECTS: conducts log on process
    private Person login() {
        Person user = findMatchingUser("\nEnter your name: ");
        if (user == null) {
            System.out.println("User not found...");
            return null;
        }
        boolean loginSuccess = enterPassword(user);

        if (loginSuccess) {
            return user;
        } else {
            return null;
        }
    }

    // MODIFIES: this
    // EFFECTS: allows user to enter password to log in, returns true if successful
    private boolean enterPassword(Person user) {

        if (user != null) {
            while (true) {
                System.out.println("Enter password or q to return to menu:");
                String passwordAttempt = input.next();

                if (passwordAttempt.equalsIgnoreCase("q")) {
                    return false;
                } else {
                    boolean logInSuccess = user.login(passwordAttempt);

                    if (logInSuccess) {
                        return true;
                    }
                }
                System.out.println("Incorrect password...");
            }
        }
        System.out.println("Failed to sign in...");
        return false;
    }

    // EFFECTS: conducts user search
    protected Person findMatchingUser(String prompt) {
        System.out.println(prompt);
        String name = input.next();

        List<Person> people = network.searchPeople(name);
        int size = people.size();

        if (size == 1) {
            return people.get(0);
        } else if (size > 1) {
            return filterMatchingUsers(people);
        } else {
            return null;
        }
    }

    // EFFECTS: filters user search
    protected Person filterMatchingUsers(List<Person> people) {
        int size = people.size();
        int index;

        while (true) {
            displayNames(people);
            String stringIndex = input.next();

            if (stringIndex.equalsIgnoreCase("q")) {
                return null;
            }
            index = Integer.parseInt(stringIndex);

            if (index >= size) {
                System.out.println("Invalid person...");
            } else {
                return people.get(index);
            }
        }
    }

    // EFFECTS: displays matching users
    protected void displayNames(List<Person> people) {
        System.out.println("\nSelect from:");
        int i = 0;
        for (Person person : people) {
            System.out.println("\t" + i + " -> " + person.getName() + ", " + person.getSex() + ", " + person.getAge());
            i++;
        }
        System.out.println("\tq -> None of the above, back to menu");
    }

    // MODIFIES: this
    // EFFECTS: conducts creation of new user
    private Person createUser() {
        String name = getUserName();
        String sex = getUserSex();
        int age = getUserAge();
        String password = getUserPassword();

        Person newUser = new Person(name, sex, age, password);
        network.addPerson(newUser);
        return newUser;
    }

    // EFFECTS: gets name from user
    private String getUserName() {
        String name;

        while (true) {
            System.out.println("Enter your name:");
            name = input.next();

            if (name.length() == 0) {
                System.out.println("Invalid name...");
            } else {
                return name;
            }
        }
    }

    // EFFECTS: gets sex from user
    private String getUserSex() {

        while (true) {
            displaySex();
            String sex = input.next().toLowerCase();

            if (sex.equals("m")) {
                return "male";
            } else if (sex.equals("f")) {
                return "female";
            } else {
                System.out.println("Invalid entry...");
            }
        }
    }

    // EFFECTS: dispays sex options
    private void displaySex() {
        System.out.println("\nSelect from:");
        System.out.println("\tm -> male");
        System.out.println("\tf -> female");
    }

    // EFFECTS: gets age from user
    private int getUserAge() {
        int age;

        while (true) {
            System.out.println("Enter your age: ");
            String stringAge = input.next();
            age = Integer.parseInt(stringAge);

            if (age > 0 && age <= 125) {
                return age;
            } else {
                System.out.println("Invalid age...");
            }
        }
    }

    // EFFECTS: gets a password from user
    private String getUserPassword() {
        String password;

        while (true) {
            System.out.println("Enter a password:");
            password = input.next();

            if (password.length() == 0) {
                System.out.println("Invalid password...");
            } else {
                return password;
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: processes user input
    private void runEnterApp() {
        boolean keepGoing = true;
        String command;

        while (keepGoing) {
            displayInAppMenu();
            command = input.next();
            command = command.toLowerCase();

            if (command.equals("q")) {
                keepGoing = false;
            } else {
                try {
                    processInAppMenuCommand(command);
                } catch (NumberFormatException e) {
                    System.out.println("Not a valid input...");
                }
            }
        }
    }


    // EFFECTS: displays menu options to user
    private void displayInAppMenu() {
        System.out.println("\nSelect from:");
        System.out.println("\tp -> My profile");
        System.out.println("\to -> View others' profile");
        System.out.println("\ta -> Add friend to friends list");
        System.out.println("\tr -> remove friend from friends list");
        System.out.println("\tm -> View messages");
        System.out.println("\tq -> logout");
    }

    // MODIFIES: this
    // EFFECTS: processes user input
    private void processInAppMenuCommand(String command) throws NumberFormatException {
        switch (command) {
            case "p":
                runMyProfile();
                break;
            case "o":
                runOtherProfile();
                break;
            case "a":
                addNewFriend();
                break;
            case "r":
                removeFriend();
                break;
            case "m":
                runMyMessages();
                break;
            default:
                System.out.println("Selection not valid...");
                break;
        }
    }

    // MODIFIES: this
    // EFFECTS: conducts friending process
    private void addNewFriend() {
        Person friend = findMatchingUser("\nEnter their name: ");
        if (friend != null && friend != user) {
            user.addFriend(friend);
        } else {
            System.out.println("User was not found...");
        }
    }

    // MODIFIES: this
    // EFFECTS: conducts friend removal process
    private void removeFriend() throws NumberFormatException {
        if (user.getFriends().size() > 0) {
            displayFriends();
            String stringIndex = input.next();

            int index = Integer.parseInt(stringIndex);
            if (index >= 0 && index < user.getFriends().size()) {
                user.removeFriend(user.getFriends().get(index));
            }
        } else {
            System.out.println("You have no friends...");
        }
    }

    // EFFECTS: displays user's friends list
    private void displayFriends() {
        int i = 0;
        System.out.println("\nSelect From: ");
        for (Person friend : user.getFriends()) {
            System.out.println("\t" + i + " -> " + friend.getName());
            i++;
        }
    }

    // MODIFIES: this
    // EFFECTS: processes user input
    private void runMyProfile() {
        boolean keepGoing = true;
        String command;


        while (keepGoing) {
            displayMyProfileOptions();
            command = input.next();
            command = command.toLowerCase();

            if (command.equals("q")) {
                keepGoing = false;
            } else {
                processMyProfileCommand(command);
            }
        }
    }

    // EFFECTS: displays options to user
    private void displayMyProfileOptions() {
        System.out.println("\nSelect from:");
        System.out.println("\tb -> Change bio");
        System.out.println("\th -> Add hobby");
        System.out.println("\tm -> Make post");
        System.out.println("\tv -> View profile");
        System.out.println("\tq -> Go back");
    }

    // MODIFIES: this
    // EFFECTS: processes user input
    private void processMyProfileCommand(String command) {
        switch (command) {
            case "b":
                changeMyBio();
                break;
            case "h":
                addToMyHobbies();
                break;
            case "m":
                makeAPost();
                break;
            case "v":
                viewProfile(user);
                break;
            default:
                System.out.println("Selection not valid...");
                break;
        }
    }

    // MODIFIES: this
    // EFFECTS: allows user to changes bio of this user's profile
    private void changeMyBio() {
        System.out.println("Enter your bio:");
        String myPost = input.next();
        user.getProfile().changeBio(myPost);
    }

    // MODIFIES: this
    // EFFECTS: allows user to add hobby to this user
    private void addToMyHobbies() {
        System.out.println("Enter a hobby:");
        String myPost = input.next();
        if (myPost.length() > 0) {
            user.addHobby(myPost);
        } else {
            System.out.println("Nothing was written...");
        }
    }

    // MODIFIES: this
    // EFFECTS: allows user to post to this user's profile
    private void makeAPost() {
        System.out.println("Enter your post: ");
        String myPost = input.next();
        if (myPost.length() > 0) {
            user.getProfile().makePost(myPost);
        } else {
            System.out.println("Nothing was written...");
        }
    }

    // EFFECTS: displays user profile
    protected void viewProfile(Person person) {
        printGeneralInfo(person);
        printBio(person);
        printHobbies(person);
        printPosts(person);
    }

    // EFFECTS: displays user information
    protected void printGeneralInfo(Person person) {
        System.out.println("\nName: " + person.getName());
        System.out.println("Sex: " + person.getSex());
        System.out.println("Age: " + person.getAge());
    }

    // EFFECTS: displays user's bio
    protected void printBio(Person person) {
        System.out.println("\nBio:");
        System.out.println("\t" + person.getProfile().getBio());
    }

    // EFFECTS: displays user's hobbies
    protected void printHobbies(Person person) {
        System.out.println("\nHobbies:");
        for (String hobby : person.getHobbies()) {
            System.out.println("\t" + hobby);
        }
    }

    // EFFECTS: displays user's posts
    protected void printPosts(Person person) {
        System.out.println("\nPosts:");
        for (Post post : person.getProfile().getPosts()) {
            System.out.println("\t" + post.getDate() + ": " + post.getPost());
        }
    }


    // EFFECTS: processes user input
    private void runOtherProfile() {
        boolean keepGoing = true;
        String command;
        Person other;

        while (keepGoing) {
            displayOtherProfileOptions();
            command = input.next();

            if (command.equals("q")) {
                keepGoing = false;
            } else {
                other = findMatchingUser("\nEnter their name: ");
                if (other == user) {
                    System.out.println("This is your profile...");
                } else if (other != null) {
                    viewProfile(other);
                } else {
                    System.out.println("User not found...");
                }
            }
        }
    }


    // EFFECTS: displays user options
    private void displayOtherProfileOptions() {
        System.out.println("\nSelect from:");
        System.out.println("\tp -> Search for person");
        System.out.println("\tq -> Go back");
    }

    // MODIFIES: this
    // EFFECTS: processes user input
    private void runMyMessages() {
        boolean keepGoing = true;
        String command;

        while (keepGoing) {
            displayMessageMenu();
            command = input.next();
            command = command.toLowerCase();

            if (command.equals("q")) {
                keepGoing = false;
            } else {
                processMessageCommand(command);
            }
        }
    }

    // EFFECTS: displays menu options to user
    private void displayMessageMenu() {
        System.out.println("\nSelect from:");
        System.out.println("\tv -> View a message thread");
        System.out.println("\ts -> Send a message");
        System.out.println("\tq -> Go back");
    }

    // MODIFIES: this
    // EFFECTS: processes user input
    private void processMessageCommand(String command) {
        if (command.equals("v")) {
            viewMessageThread();
        } else if (command.equals("s")) {
            sendMessage();
        } else {
            System.out.println("Selection not valid...");
        }
    }

    // EFFECTS: displays user message thread
    private void viewMessageThread() {
        boolean keepGoing = true;
        String command;

        while (keepGoing) {
            displayUsersMessaged();
            command = input.next();

            if (command.equalsIgnoreCase("q")) {
                keepGoing = false;
            } else {
                processUsersMessaged(command, user.getMessageThreads().size());
            }
        }
    }

    // EFFECTS: displays people user has messages
    private void displayUsersMessaged() {
        int i = 0;
        System.out.println("\nSelect from: ");
        for (MessageThread messageThread : user.getMessageThreads()) {
            Person other;
            for (Person person : messageThread.getPersons()) {
                if (person != user) {
                    other = person;
                    System.out.println("\t" + i + " -> " + other.getName());
                }
            }
            i++;
        }
        System.out.println("\tq -> Go back");
    }

    // EFFECTS: processes user input
    private void processUsersMessaged(String command, int size) {
        int index = Integer.parseInt(command);
        if (index >= 0 && index < size) {
            displayMessageThread(user.getMessageThreads().get(index));
        } else {
            System.out.println("Selection not valid...");
        }
    }

    // EFFECTS: displays user message thread
    private void displayMessageThread(MessageThread messageThread) {
        for (Message message : messageThread.getMessages()) {
            System.out.println(message.getPerson().getName() + ": " + message.getCommunication());
        }
    }

    // MODIFIES: this
    // EFFECTS: conducts sending of message
    private void sendMessage() {
        Person other = findMatchingUser("\nEnter their name: ");
        if (other != null && other != user) {
            System.out.println("Enter the message: ");
            String message = input.next();
            if (message.length() > 0) {
                user.sendMessage(other, message);
            } else {
                System.out.println("Nothing was written...");
            }
        } else {
            System.out.println("User was not found...");
        }
    }
}
