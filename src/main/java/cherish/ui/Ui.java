package cherish.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Handles user interface for Cherish application.
 * Supports both console and GUI modes.
 */
public class Ui {
    private static final String WELCOME_MSG = "Hello! I'm Cherish\nWhat can I do for you?";
    private static final String BYE_MSG = "Bye. Hope to see you again soon!";
    private static final String LOADING_ERROR_MSG = "Warning: Could not load task data. Starting with empty list.";
    private static final String ERROR_PREFIX = "Oops! ";

    private final Scanner scanner;
    private final List<String> messagesForGui;
    private final boolean isGuiMode;

    /** Default constructor, console mode. */
    public Ui() {
        this(false);
    }

    /**
     * Constructor with GUI mode flag.
     * @param isGuiMode true for GUI mode; false for console
     */
    public Ui(boolean isGuiMode) {
        this.scanner = new Scanner(System.in);
        this.messagesForGui = new ArrayList<>();
        this.isGuiMode = isGuiMode;
    }

    public void showWelcome() {
        display(WELCOME_MSG);
    }

    public void showBye() {
        display(BYE_MSG);
    }

    public void showLoadingError() {
        display(LOADING_ERROR_MSG);
    }

    public void showError(String message) {
        display(ERROR_PREFIX + message);
    }

    public void showMessage(String message) {
        display(message);
    }

    /**
     * Print Line
     */
    public void showLine() {
        if (!isGuiMode) {
            System.out.println();
        }
    }

    public String readCommand() {
        return scanner.nextLine();
    }

    /**
     * Retrieves and clears messages for GUI.
     */
    public String getMessagesForGui() {
        String messages = String.join("\n", messagesForGui);
        messagesForGui.clear();
        return messages;
    }

    /**
     * Retrieves messages for GUI without clearing.
     */
    public String peekMessagesForGui() {
        return String.join("\n", messagesForGui);
    }

    /**
     * Handles output for both console and GUI modes.
     */
    private void display(String message) {
        if (isGuiMode) {
            messagesForGui.add(message);
        } else {
            System.out.println(message + "\n");
        }
    }
}
