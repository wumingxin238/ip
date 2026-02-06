package cherish;

/**
 * Custom exception class for the Cherish application.
 * Used to handle errors specific to the application's logic, such as invalid user input
 * or problems during file operations.
 */
public class CherishException extends Exception {
    /**
     * Constructs a CherishException with the specified detail message.
     *
     * @param message The detail message explaining the reason for the exception.
     */
    public CherishException(String message) {
        super(message);
    }
}
