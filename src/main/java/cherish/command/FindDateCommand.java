package cherish.command;

import cherish.CherishException;
import cherish.model.TaskList;
import cherish.storage.Storage;
import cherish.ui.Ui;

/**
 * Command to find and display all tasks scheduled on a specific date.
 * Delegates the search functionality to the TaskList.
 */
public class FindDateCommand extends Command {

    private String dateString;

    /**
     * Constructs a FindDateCommand with the specified date string.
     *
     * @param dateString The date string in the format "yyyy-MM-dd" (e.g., "2026-02-01").
     */
    public FindDateCommand(String dateString) {
        this.dateString = dateString;
    }

    @Override
    public String execute(TaskList tasks, Ui ui, Storage storage) throws CherishException {
        return tasks.findTasksOnDate(dateString);
    }
}
