import java.util.Scanner;

public class Ui {
    private Scanner scanner;

    public Ui() {
        this.scanner = new Scanner(System.in);
    }

    public void showWelcome() {
        System.out.println("Hello! I'm Cherish\n" +
                "What can I do for you?\n");
    }

    public void showBye() {
        System.out.println(" Bye. Hope to see you again soon!\n");
    }

    public void showError(String message) {
        System.out.println("Oops! " + message + "\n");
    }

    public void showLoadingError() {
        System.out.println("Warning: Could not load task data. Starting with empty list.\n");
    }

    public void showMessage(String message) {
        System.out.println(message + "\n");
    }

    public String readCommand() {
        return scanner.nextLine();
    }

    public void showLine() {
        System.out.println();
    }
}