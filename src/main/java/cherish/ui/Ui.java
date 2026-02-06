package cherish.ui;

import java.util.Scanner;

/**
 * Handles the user interface for the Cherish application.
 * Manages user input and displays messages to the console.
 */
public class Ui {
    private Scanner scanner;

    /**
     * Constructs a Ui object, initializing the scanner for user input.
     */
    public Ui() {
        this.scanner = new Scanner(System.in);
    }

    /**
     * Displays the welcome message to the user.
     */
    public void showWelcome() {
        System.out.println("Hello! I'm Cherish\n"
                + "What can I do for you?\n");
    }

    /**
     * Displays the goodbye message to the user.
     */
    public void showBye() {
        System.out.println(" Bye. Hope to see you again soon!\n");
    }

    /**
     * Displays an error message to the user.
     *
     * @param message The error message to be shown.
     */
    public void showError(String message) {
        System.out.println("Oops! " + message + "\n");
    }

    /**
     * Displays a warning message indicating that task data could not be loaded.
     */
    public void showLoadingError() {
        System.out.println("Warning: Could not load task data. Starting with empty list.\n");
    }

    /**
     * Displays a general message to the user.
     *
     * @param message The message to be shown.
     */
    public void showMessage(String message) {
        System.out.println(message + "\n");
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
     */
    public void showLine() {
        System.out.println();
    }
}
