package cherish.command;

import cherish.CherishException;
import cherish.model.Task;
import cherish.model.TaskList;
import cherish.storage.Storage;
import cherish.ui.Ui;

/**
 * Command to mark a task as not completed based on its index in the task list.
 * Validates the index and checks if the task is already unmarked.
 */
public class UnmarkCommand extends Command {
    private int index;
    /**
     * Constructs an UnmarkCommand with the specified task index.
     *
     * @param index The zero-based index of the task to be marked as not done.
     */
    public UnmarkCommand(int index) {
        this.index = index;
    }

    @Override
    public String execute(TaskList tasks, Ui ui, Storage storage) throws CherishException {
        // Validate index
        if (index < 0 || index >= tasks.size()) {
            throw new CherishException("Task number out of range! You have " + tasks.size() + " task(s).");
        }

        Task task = tasks.get(index);

        // Check if already unmarked
        if (!task.isDone()) {
            throw new CherishException("This task is already marked as not done!");
        }

        // Unmark the task
        tasks.markAsNotDone(index);

        // Save updated list to file
        try {
            storage.save(tasks.toArray());
        } catch (CherishException e) {
            throw new CherishException("Failed to update task file after unmarking.");
        }

        // Return success message
        return "OK! I've marked this task as not done:\n  " + task;
    }
}
