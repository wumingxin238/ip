package cherish.command;

import cherish.CherishException;
import cherish.storage.Storage;
import cherish.model.TaskList;
import cherish.ui.Ui;

/**
 * Command to mark a task as completed based on its index in the task list.
 * Validates the index before attempting to mark the task.
 */
public class MarkCommand extends Command {

    private int index;

    /**
     * Constructs a MarkCommand with the specified task index.
     *
     * @param index The zero-based index of the task to be marked as done.
     */
    public MarkCommand(int index) {
        this.index = index;
    }

    @Override
    public String execute(TaskList tasks, Ui ui, Storage storage) throws CherishException {
        if (index >= tasks.size()) {
            throw new CherishException("Task number out of range! You have " + tasks.size() + " tasks.");
        }
        tasks.markAsDone(index);
        return "Great! I've marked this task as done:\n  " + tasks.get(index);
    }
}