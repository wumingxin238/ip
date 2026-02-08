// src/main/java/cherish/ui/Ui.java
package cherish.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


/**
 * Handles the user interface for the Cherish application.
 * Manages user input and displays messages to the console or collects them for GUI.
 */
public class Ui {
    private Scanner scanner;
    private List<String> messagesForGui; // Stores messages for GUI retrieval
    private boolean isGuiMode; // Flag to determine behavior

    /**
     * Constructs a Ui object for console mode, initializing the scanner for user input.
     */
    public Ui() {
        this(false); // Default to console mode
    }

    /**
     * Constructs a Ui object, initializing the scanner for user input.
     * @param isGuiMode If true, messages are collected instead of printed.
     */
    public Ui(boolean isGuiMode) {
        this.scanner = new Scanner(System.in);
        this.messagesForGui = new ArrayList<>();
        this.isGuiMode = isGuiMode;
    }

    /**
     * Displays the welcome message to the user.
     * In GUI mode, adds the message to the internal collection.
     * In console mode, prints the message directly.
     */
    public void showWelcome() {
        String msg = "Hello! I'm Cherish\nWhat can I do for you?";
        if (isGuiMode) {
            messagesForGui.add(msg);
        } else {
            System.out.println(msg + "\n");
        }
    }

    /**
     * Displays the goodbye message to the user.
     * In GUI mode, adds the message to the internal collection.
     * In console mode, prints the message directly.
     */
    public void showBye() {
        String msg = "Bye. Hope to see you again soon!";
        if (isGuiMode) {
            messagesForGui.add(msg);
        } else {
            System.out.println(msg + "\n");
        }
    }

    /**
     * Displays an error message to the user.
     * In GUI mode, adds the message to the internal collection.
     * In console mode, prints the message directly.
     *
     * @param message The error message to be shown.
     */
    public void showError(String message) {
        String msg = "Oops! " + message;
        if (isGuiMode) {
            messagesForGui.add(msg);
        } else {
            System.out.println(msg + "\n");
        }
    }

    /**
     * Displays a warning message indicating that task data could not be loaded.
     * In GUI mode, adds the message to the internal collection.
     * In console mode, prints the message directly.
     */
    public void showLoadingError() {
        String msg = "Warning: Could not load task data. Starting with empty list.";
        if (isGuiMode) {
            messagesForGui.add(msg);
        } else {
            System.out.println(msg + "\n");
        }
    }

    /**
     * Displays a general message to the user.
     * In GUI mode, adds the message to the internal collection.
     * In console mode, prints the message directly.
     *
     * @param message The message to be shown.
     */
    public void showMessage(String message) {
        if (isGuiMode) {
            messagesForGui.add(message);
        } else {
            System.out.println(message + "\n");
        }
    }

    /**
     * Reads a line of input from the user.
     *
     * @return The string entered by the user.
     */
    public String readCommand() {
        return scanner.nextLine();
    }

    /**
     * Prints a blank line to the console for formatting.
     * In GUI mode, this has no effect.
     */
    public void showLine() {
        if (!isGuiMode) {
            System.out.println();
        }
    }

    /**
     * Retrieves and clears the collected messages intended for the GUI.
     * This should be called after each command execution in GUI mode.
     *
     * @return A single string containing all collected messages, separated by newlines.
     */
    public String getMessagesForGui() {
        StringBuilder sb = new StringBuilder();
        for (String msg : messagesForGui) {
            sb.append(msg).append("\n"); // Add newline back for display
        }
        messagesForGui.clear(); // Clear the buffer after retrieval
        return sb.toString().trim(); // Trim trailing newline
    }
    /**
     * Retrieves the currently collected messages intended for the GUI without clearing the buffer.
     *
     * @return A single string containing all collected messages, separated by newlines.
     */
    public String peekMessagesForGui() {
        StringBuilder sb = new StringBuilder();
        for (String msg : messagesForGui) {
            sb.append(msg).append("\n");
        }
        return sb.toString().trim(); // Trim trailing newline
    }

}
