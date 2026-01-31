package cherish.command;

import cherish.storage.Storage;
import cherish.model.TaskList;
import cherish.ui.Ui;

public class ListCommand extends Command {
    @Override
    public String execute(TaskList tasks, Ui ui, Storage storage) {
        return tasks.getListString();
    }
}