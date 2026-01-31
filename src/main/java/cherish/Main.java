// src/main/java/cherish/Main.java
package cherish;

/**
 * The main entry point for the Cherish application.
 * This class initializes the application and starts its execution loop.
 */
public class Main {

    /**
     * The main method that serves as the entry point for the JVM to execute the program.
     *
     * @param args Command-line arguments passed to the application (currently unused by Cherish).
     */
    public static void main(String[] args) {
        // Initialize and start the application logic
        Cherish app = new Cherish("data/cherish.txt"); // Assuming CherishApp is your main app controller
        app.run(); // Call the run method to start the interaction loop
    }
}