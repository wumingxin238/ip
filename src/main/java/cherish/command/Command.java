package cherish.command;

import cherish.CherishException;
import cherish.model.TaskList;
import cherish.storage.Storage;
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
     * Performs the undo operation for this command.
     * This reverts the state change made by the execute() method.
     *
     * @param tasks The list of tasks to revert state on.
     * @param ui The UI instance to interact with the user during undo.
     * @param storage The storage instance to save/revert data during undo.
     * @return A string response message indicating the result of the undo operation.
     * @throws CherishException If the undo operation fails.
     */
    public String undo(TaskList tasks, Ui ui, Storage storage) throws CherishException {
        return "";
    }
    /**
     * Indicates whether this command should cause the application to exit.
     * By default, commands do not cause an exit.
     *
     * @return True if the application should terminate after executing this command, false otherwise.
     */
    public boolean isExit() {
        return false;
    }
    /**
     * Indicates whether this command is undoable
     * By default, commands is undoable
     *
     * @return True if the command is undoable
     */
    public boolean isUndoable() {
        return true;
    }
}
