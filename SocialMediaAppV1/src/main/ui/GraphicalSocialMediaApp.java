package ui;

import model.*;
import model.Event;
import persistence.*;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

// social media application with a graphical user interface
public class GraphicalSocialMediaApp implements ActionListener, ListSelectionListener {
    private static final int TEXTFIELD_HEIGHT = 20;
    private static final int TEXTFIELD_WIDTH = 70;
    private static final int HEIGHT_LOGIN = 200;
    private static final int WIDTH_LOGIN = 300;
    private static final int HEIGHT = 500;
    private static final int WIDTH = 700;
    private static final String CREATE_PANEL = "Create User Card";
    private static final String LOG_PANEL = "Log In Card";
    private static final String MALE = "male";
    private static final String FEMALE = "female";
    private static final String SPACE = "     ";

    private JFrame frame;
    private JPanel logInPanels;

    private JButton loadLastSave;
    private JButton newSession;

    private JButton newUser;
    private JButton logIn;
    private JButton quitButton;

    private JTextField nameTextField;
    private JPasswordField passwordField;
    private JLabel logInStatus;
    private JButton confirmLogIn;

    private JTextField createName;
    private JTextField createAge;
    private JTextField createSex;
    private JTextField createPassword;
    private JButton confirmCreateUser;
    private JLabel createUserStatus;

    private DefaultListModel<String> userPosts;
    private JTextField postField;
    private JButton confirmPost;
    private JButton returnToLogIn;

    private JFrame deletePostFrame;
    private JButton deletePost;
    private int firstIndex;
    private JList listPanel;

    private JButton save;
    private JButton noSave;

    private Network network;
    private Person user;
    private JsonReader reader;

    // EFFECTS: initializes app
    public GraphicalSocialMediaApp() {
        init();
        displayLoadMenu();
    }

    // MODIFIES: this
    // EFFECTS: initializes fields
    private void init() {
        user = null;
        reader = new JsonReader("./data/network.json");
    }

    // MODIFIES: this
    // EFFECTS: displays load menu
    private void displayLoadMenu() {
        frame = new JFrame("Load Menu");
        JPanel panel = new JPanel();

        addImage(panel);

        initializeLoadButtons(panel);
        frame.add(panel);
        initializeFrame(frame);
    }

    // MODIFIES: this
    // EFFECTS: adds image to panel
    private void addImage(JPanel panel) {
        ImageIcon logo = new ImageIcon("./data/messageIcon.png");
        JLabel logoLabel = new JLabel(logo);
        panel.add(logoLabel);
    }

    // MODIFIES: this
    // EFFECTS: adds buttons to Panel
    private void initializeLoadButtons(JPanel panel) {
        loadLastSave = new JButton("Load Last Save");
        initializeButton(loadLastSave, panel);

        newSession = new JButton("New Session");
        initializeButton(newSession, panel);
    }

    // MODIFIES: this
    // EFFECTS: initializes frame
    private void initializeFrame(JFrame frame) {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    // MODIFIES: button, panel
    // EFFECTS: adds ActionListener to button, then adds to panel
    private void initializeButton(JButton button, JPanel panel) {
        button.addActionListener(this);
        panel.add(button);
    }

    // MODIFIES: this
    // EFFECTS: loads last session from file
    private void loadLastSession() throws IOException {
        network = reader.read();
    }

    // MODIFIES: this
    // EFFFECTS: creates and runs Log In Menu
    private void runLogInMenu() {
        frame = new JFrame("Log In Menu");
        frame.setLayout(new BorderLayout());
        JPanel buttonPanel = new JPanel();

        setUpLogInButtons(buttonPanel);
        frame.add(buttonPanel, BorderLayout.NORTH);

        logInPanels = new JPanel(new CardLayout());
        initializeLogInPanels();
        frame.add(logInPanels, BorderLayout.CENTER);

        JLabel logInStatus = new JLabel("");
        frame.add(logInStatus, BorderLayout.SOUTH);


        initializeFrame(frame);
        frame.setMinimumSize(new Dimension(WIDTH_LOGIN, HEIGHT_LOGIN));
    }

    // MODIFIES: panel
    // EFFECTS: creates buttons, then adds them to panel
    private void setUpLogInButtons(JPanel panel) {
        newUser = new JButton("Create Account");
        initializeButton(newUser, panel);

        logIn = new JButton("Log In");
        initializeButton(logIn, panel);

        quitButton = new JButton("Quit");
        initializeButton(quitButton, panel);
    }

    // MODIFIES: this
    // EFFECTS: adds panels to Log In Menu
    private void initializeLogInPanels() {
        JPanel logInPanel = new JPanel();
        createLogInPanel(logInPanel);

        JPanel createUserPanel = new JPanel();
        createCreateUserPanel(createUserPanel);

        logInPanels.add(createUserPanel, CREATE_PANEL);
        logInPanels.add(logInPanel, LOG_PANEL);
    }

    // MODIFIES: panel
    // EFFECTS: initializes panel
    private void createLogInPanel(JPanel panel) {
        nameTextField = new JTextField();
        setTextFieldWithLabel(panel, nameTextField, "Name:");
        passwordField = new JPasswordField();
        setTextFieldWithLabel(panel, passwordField, "Password:");

        confirmLogIn = new JButton("Confirm");
        initializeButton(confirmLogIn, panel);

        logInStatus = new JLabel("Log in failed...");
        logInStatus.setForeground(Color.red);
        logInStatus.setVisible(false);
        panel.add(logInStatus);
    }

    // MODIFIES: panel
    // EFFECTS: initializes panel
    private void createCreateUserPanel(JPanel panel) { // still need to finish this :)
        createName = new JTextField();
        setTextFieldWithLabel(panel, createName, "Name:");
        createAge = new JTextField();
        setTextFieldWithLabel(panel, createAge, "Age:");
        createSex = new JTextField();
        setTextFieldWithLabel(panel, createSex, "Sex (male or female):");
        createPassword = new JTextField();
        setTextFieldWithLabel(panel, createPassword, "Password:");

        confirmCreateUser = new JButton("Confirm");
        initializeButton(confirmCreateUser, panel);

        createUserStatus = new JLabel("Invalid information...");
        createUserStatus.setForeground(Color.red);
        panel.add(createUserStatus);
        createUserStatus.setVisible(false);
    }

    // MODIFIES: panel
    // EFFECTS: initializes panel
    private void setTextFieldWithLabel(JPanel panel, JTextField field, String label) {
        JLabel nameLabel = new JLabel(label);
        field.setPreferredSize(new Dimension(TEXTFIELD_WIDTH, TEXTFIELD_HEIGHT));
        panel.add(nameLabel);
        panel.add(field);
    }

    // MODIFIES: this
    // EFFECTS: processes ActionEvent ev
    @Override
    public void actionPerformed(ActionEvent ev) {
        if (ev.getSource() == loadLastSave || ev.getSource() == newSession) {
            processLoad(ev);
        } else if (ev.getSource() == newUser || ev.getSource() == logIn || ev.getSource() == quitButton) {
            processLogIn(ev);
        } else if (ev.getSource() == confirmLogIn) {
            logInUser();
        } else if (ev.getSource() == confirmCreateUser) {
            createUser();
        } else if (ev.getSource() == save || ev.getSource() == noSave) {
            try {
                processSave(ev);
            } catch (FileNotFoundException e) {
                System.out.println("Save failed");
            }
        } else if (ev.getSource() == returnToLogIn) {
            frame.dispose();
            deletePostFrame.dispose();
            runLogInMenu();
        } else if (ev.getSource() == confirmPost) {
            processPost();
        } else {
            processDeletePost(ev);
        }
    }

    // MODIFIES: this
    // EFFECTS: processes user input from Load Menu
    private void processLoad(ActionEvent ev) {
        if (ev.getSource() == loadLastSave) {
            try {
                loadLastSession();
            } catch (IOException ex) {
                System.out.println("Failed to load last save");
                return; // fail case
            }
        } else if (ev.getSource() == newSession) {
            network = new Network();
        }
        frame.dispose();
        runLogInMenu();
    }

    // MODIFIES: this
    // EFFECTS: processes user input from Log In Menu
    private void processLogIn(ActionEvent ev) {
        CardLayout cl = (CardLayout) logInPanels.getLayout();
        if (ev.getSource() == quitButton) {
            frame.dispose();
            askToSave();
        } else if (ev.getSource() == newUser) {
            cl.show(logInPanels, CREATE_PANEL);
        } else if (ev.getSource() == logIn) {
            cl.show(logInPanels, LOG_PANEL);
        } else if (ev.getSource() == confirmLogIn) {
            logInUser();
        }
    }

    // MODIFIES: this
    // EFFECTS: filters matching users, then logs in user;
    private void logInUser() {
        String name = nameTextField.getText();
        filterUserAndLogIn(network.searchPeople(name));
        if (user == null) {
            logInStatus.setVisible(true);
        } else {
            frame.dispose();
            runInAppMenu();
        }
    }

    // MODIFIES: this
    // EFFECTS: logs in user
    private void filterUserAndLogIn(List<Person> people) {
        boolean success;
        String password = passwordField.getText();
        for (Person person : people) {
            success = person.login(password);

            if (success) {
                user = person;
                return;
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: creates new user
    private void createUser() {
        String name = createName.getText();
        String password = createPassword.getText();
        String sex = createSex.getText().toLowerCase();

        if (!sex.equals(MALE) && !sex.equals(FEMALE)) {
            createUserStatus.setVisible(true);
            return;
        }

        try {
            int age = Integer.parseInt(createAge.getText());
            user = new Person(name, sex, age, password);
            network.addPerson(user);
            frame.dispose();
            runInAppMenu();
        } catch (NumberFormatException e) {
            createUserStatus.setVisible(true);
        }
    }

    // MODIFIES: this
    // EFFECTS: prompts user to save with save menu
    private void askToSave() {
        frame = new JFrame("Save Session");
        createSaveMenu();
        initializeFrame(frame);
    }

    // MODIFIES: this
    // EFFECTS: creates save menu
    private void createSaveMenu() {
        JPanel panel = new JPanel();
        JLabel askToSave = new JLabel("Save Session?");
        panel.add(askToSave);

        save = new JButton("Yes");
        initializeButton(save, panel);

        noSave = new JButton("No");
        initializeButton(noSave, panel);

        frame.add(panel);
    }

    // MODIFIES: this
    // EFFECTS: processes ActionEvent ev, then closes save menu
    private void processSave(ActionEvent ev) throws FileNotFoundException {
        if (ev.getSource() == save) {
            saveSession();
        } else {
            EventLog.getInstance().clear();
        }
        frame.dispose();
        printLog();
    }


    // EFFECTS: saves current session to JSON File
    private void saveSession() throws FileNotFoundException {
        JsonWriter writer = new JsonWriter("./data/network.json");
        writer.open();
        writer.write(network);
        writer.close();
    }

    // MODIFIES: this
    // EFFECTS: creates post and adds it to displayed list
    private void processPost() {
        Profile myProfile = user.getProfile();

        String post = postField.getText();
        user.getProfile().makePost(post);

        List<Post> posts = myProfile.getPosts();
        addPostToJList(posts.get(posts.size() - 1));
    }

    // MODIFIES: this
    // EFFECTS: processes ActionEvent ev, then hides prompt frame
    private void processDeletePost(ActionEvent ev) {
        if (ev.getSource() == deletePost) {
            user.getProfile().deletePost(firstIndex);
            userPosts.remove(firstIndex);
        }
        listPanel.clearSelection();
        deletePostFrame.setVisible(false);
    }

    // MODIFIES: this
    // EFFECTS: creates and runs in app menu
    private void runInAppMenu() {
        frame = new JFrame();
        frame.setLayout(new GridLayout(0, 1));

        createUserGeneralInfoPanel();
        setUpProfilePanel();
        createInteractionPanel();

        initializeFrame(frame);
        frame.setMinimumSize(new Dimension(WIDTH, HEIGHT));

        setUpDeletePostFrame();
    }

    // MODIFIES: this
    // EFFECTS: creates panel showing general user info, then adds it to frame
    private void createUserGeneralInfoPanel() {
        JPanel infoPanel = new JPanel();
        addGeneralInfo(infoPanel, "Name:", user.getName());
        addGeneralInfo(infoPanel, "Sex:", user.getSex());
        addGeneralInfo(infoPanel, "Age:", Integer.toString(user.getAge()));

        JPanel bioPanel = new JPanel();
        addGeneralInfo(bioPanel, "Bio:", user.getProfile().getBio());

        frame.add(infoPanel);
        frame.add(bioPanel);
    }

    // MODIFIES: panel
    // EFFECTS: initializes panel
    private void addGeneralInfo(JPanel panel, String identifier, String info) {
        JLabel nameLabel = new JLabel(identifier);
        JLabel name = new JLabel(info + SPACE);
        panel.add(nameLabel);
        panel.add(name);
    }

    // MODIFIES: this
    // EFFECTS: adds panel displaying posts to frame
    private void setUpProfilePanel() {
        setUpUserPosts();
        listPanel = new JList(userPosts);
        listPanel.addListSelectionListener(this);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setViewportView(listPanel);
        listPanel.setLayoutOrientation(JList.VERTICAL);

        frame.add(scrollPane);
    }

    // MODIFIES: this
    // EFFECTS: sets up posts to be displays
    private void setUpUserPosts() {
        List<Post> posts = user.getProfile().getPosts();
        userPosts = new DefaultListModel<>();
        for (Post post : posts) {
            addPostToJList(post);
        }
    }

    // MODIFIES: this
    // EFFECTS: formats post to be displayed
    private void addPostToJList(Post post) {
        String strPost = post.getDate() + ": " + post.getPost();
        userPosts.addElement(strPost);
    }

    // MODIFIES: this
    // EFFECTS: creates interaction panel, then adds to frame
    private void createInteractionPanel() {
        JPanel interactionPanel = new JPanel();

        postField = new JTextField();
        setTextFieldWithLabel(interactionPanel, postField, "Post: ");
        postField.setPreferredSize(new Dimension(WIDTH_LOGIN, TEXTFIELD_HEIGHT));

        confirmPost = new JButton("Make post");
        initializeButton(confirmPost, interactionPanel);

        returnToLogIn = new JButton("Go back");
        initializeButton(returnToLogIn, interactionPanel);

        frame.add(interactionPanel);
    }

    // MODIFIES: this
    // EFFECTS: saves position of selected list item, them prompts user to delete item
    @Override
    public void valueChanged(ListSelectionEvent ev) {
        firstIndex = ev.getFirstIndex();
        deletePostFrame.setVisible(true);
    }

    // MODIFIES: this
    // EFFECTS: sets up frame prompting user to delete a post
    private void setUpDeletePostFrame() {
        deletePostFrame = new JFrame("Delete Post?");
        JPanel panel = new JPanel();

        deletePost = new JButton("Yes");
        initializeButton(deletePost, panel);

        JButton noDeletePost = new JButton("No");
        initializeButton(noDeletePost, panel);

        deletePostFrame.add(panel);
        initializeFrame(deletePostFrame);
        deletePostFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        deletePostFrame.setVisible(false);
    }

    // EFFECTS: prints log of all events that occurred during session
    private void printLog() {
        for (Event ev : EventLog.getInstance()) {
            System.out.println(ev.toString());
        }
    }

    // EFFECTS: runs app
    public static void main(String[] args) {
        new GraphicalSocialMediaApp();
    }
}
