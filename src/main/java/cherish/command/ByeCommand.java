package cherish.command;

import cherish.storage.Storage;
import cherish.model.TaskList;
import cherish.ui.Ui;

/**
 * Command to terminate the Cherish application.
 * When executed, it signals the application loop to stop.
 */
public class ByeCommand extends Command {

    @Override
    public String execute(TaskList tasks, Ui ui, Storage storage) {
        return null; // No output needed from the command itself, handled by ui.showGoodbye()
    }

    @Override
    public boolean isExit() {
        return true;
    }
}