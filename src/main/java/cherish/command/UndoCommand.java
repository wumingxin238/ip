package cherish.command;

import cherish.CherishException;
import cherish.model.TaskList;
import cherish.storage.Storage;
import cherish.ui.Ui;

/**
 * Command to undo other commands
 * Not really doing the undo here
 * Exist as a signal for undo
 */
public class UndoCommand extends Command {
    @Override
    public String execute(TaskList tasks, Ui ui, Storage storage) throws CherishException {
        return "This is an undo command";
    }
    @Override
    public boolean isUndoable() {
        return false;
    }
}
