package cherish.command;

import cherish.CherishException;
import cherish.storage.Storage;
import cherish.model.TaskList;
import cherish.ui.Ui;

public abstract class Command {

    public abstract String execute(TaskList tasks, Ui ui, Storage storage) throws CherishException;
    public boolean isExit() {
        return false;
    }

}