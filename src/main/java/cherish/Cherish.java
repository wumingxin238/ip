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
    private Storage storage;
    private TaskList tasks;
    private Ui ui;

    /**
     * Constructs a Cherish application instance.
     * Initializes the UI, storage, and loads the existing task list from the file.
     * If loading fails, it starts with an empty task list.
     *
     * @param filePath The path to the file where tasks are stored.
     */
    public Cherish(String filePath) {
        ui = new Ui();
        storage = new Storage(filePath);
        try {
            tasks = new TaskList(storage.load());
        } catch (CherishException e) {
            ui.showLoadingError();
            tasks = new TaskList();
        }
    }

    /**
     * Runs the main application loop.
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
<<<<<<< HEAD
}

=======
}
>>>>>>> branch-A-JavaDoc
