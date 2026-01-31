package cherish.command;

import cherish.CherishException;
import cherish.storage.Storage;
import cherish.model.TaskList;
import cherish.ui.Ui;

/**
 * Abstract base class for all commands that can be executed by the Cherish application.
 * Concrete command classes must implement the execute method.
 */
public abstract class Command {
    /**
     * Executes the command using the provided task list, user interface, and storage.
     *
     * @param tasks The current list of tasks managed by the application.
     * @param ui The user interface for displaying messages or reading input.
     * @param storage The storage handler for saving the task list.
     * @return A string response message to be shown to the user, or null if no message is needed.
     * @throws CherishException If an error occurs during command execution.
     */
    public abstract String execute(TaskList tasks, Ui ui, Storage storage) throws CherishException;

    /**
     * Indicates whether this command should cause the application to exit.
     * By default, commands do not cause an exit.
     *
     * @return True if the application should terminate after executing this command, false otherwise.
     */
    public boolean isExit() {
        return false;
    }
}