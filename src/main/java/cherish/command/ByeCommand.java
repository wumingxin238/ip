package cherish.command;

import cherish.storage.Storage;
import cherish.model.TaskList;
import cherish.ui.Ui;

public class ByeCommand extends Command {

    @Override
    public String execute(TaskList tasks, Ui ui, Storage storage) {
        return null; // no output needed
    }

    @Override
    public boolean isExit() {
        return true;
    }
}
