package ui;

import java.io.IOException;

public class MainText {
    public static void main(String[] args) {
        try {
            new SocialMediaApp();
        } catch (IOException e) {
            System.out.println("Unable to run application: file not found...");
        }
    }
}
