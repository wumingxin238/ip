// src/main/java/cherish/Cherish.java
package cherish;

import cherish.command.Command;
import cherish.model.TaskList;
import cherish.parser.Parser;
import cherish.storage.Storage;
import cherish.ui.Ui;

/**
 * The main class for the Cherish application.
 * It initializes the UI, storage, and task list, and runs the main application loop.
 */
public class Cherish {
    private static final String BYE_MESSAGE = "Bye. Hope to see you again soon!";
    private Storage storage;
    private TaskList tasks;
    private Ui ui;

    /**
     * Constructs a Cherish application instance for GUI mode.
     * Initializes the UI in GUI mode, storage, and loads the existing task list from the file.
     * If loading fails, it starts with an empty task list.
     *
     * @param filePath The path to the file where tasks are stored.
     */
    public Cherish(String filePath) {
        ui = new Ui(true); // Create UI in GUI mode
        storage = new Storage(filePath);
        try {
            tasks = new TaskList(storage.load());
        } catch (CherishException e) {
            ui.showLoadingError(); // This will be collected by UI
            tasks = new TaskList();
        }
    }

    /**
     * Returns the response string for the user input.
     * This method is used by the GUI to get the bot's reply.
     * It now uses the Ui's message collection mechanism.
     *
     * @param input The command string from the user.
     * @return A string response from Cherish.
     */
    public String getResponse(String input) {
        assert input != null : "Cherish.getResponse received a null input";
        try {
            // Clear any old messages from the UI buffer before processing
            ui.getMessagesForGui();

            // Parse the command
            Command c = Parser.parse(input);
            // Execute the command. The result will be sent to ui.showMessage/ui.showError internally.
            String resultFromExecute = c.execute(tasks, ui, storage);

            // If the command's execute method returns a string (e.g., for list, find),
            // we should also send it to the UI for collection.
            if (resultFromExecute != null && !resultFromExecute.isEmpty()) {
                ui.showMessage(resultFromExecute);
            }

            // Get the collected messages from the UI instance
            String response = ui.getMessagesForGui();
            // Return the collected result for the GUI. Handle potential nulls gracefully.
            return response != null ? response : "";

        } catch (CherishException e) {
            // Clear buffer first
            ui.getMessagesForGui();
            // Send the error message to the UI (which will collect it)
            ui.showError(e.getMessage());
            // Return the collected error message
            return ui.getMessagesForGui();
        }
    }

    /**
     * Gets the initial messages collected by the UI during Cherish construction (e.g., loading errors).
     * This should typically be called once after instantiation in GUI mode.
     *
     * @return The initial messages string.
     */
    public String getInitialMessages() {
        return ui.peekMessagesForGui(); // Use peek to get initial state without clearing
    }

    /**
     * Gets the standard goodbye message.
     * @return The goodbye message string.
     */
    public String getExitMessage() {
        return BYE_MESSAGE;
    }

    /**
     * Runs the main application loop for console mode.
     * Continuously reads user commands, parses them, executes the corresponding action,
     * handles errors, and exits when the 'bye' command is issued.
     */
    public void run() {
        ui.showWelcome();
        boolean isExit = false;
        while (!isExit) {
            try {
                String fullCommand = ui.readCommand();
                Command c = Parser.parse(fullCommand);
                String response = c.execute(tasks, ui, storage);
                if (response != null) {
                    ui.showMessage(response);
                }
                isExit = c.isExit();
            } catch (CherishException e) {
                ui.showError(e.getMessage());
            }
        }
        ui.showBye();
    }
}
