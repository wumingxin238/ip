package cherish.command;

import cherish.CherishException;
import cherish.storage.Storage;
import cherish.model.TaskList;
import cherish.ui.Ui;

public class FindDateCommand extends Command {
    private String dateString;

    public FindDateCommand(String dateString) {
        this.dateString = dateString;
    }

    @Override
    public String execute(TaskList tasks, Ui ui, Storage storage) throws CherishException {
        return tasks.findTasksOnDate(dateString);
    }
}