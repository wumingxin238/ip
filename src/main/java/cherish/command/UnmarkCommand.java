package cherish.command;

import cherish.CherishException;
import cherish.model.Task;
import cherish.model.TaskList;
import cherish.storage.Storage;
import cherish.ui.Ui;

/**
 * Command to mark a task as not completed based on its index in the task list.
 * Validates the index and ensures the task is currently marked as done.
 */
public class UnmarkCommand extends Command {

    private final int index;

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
        Task task = getValidTask(tasks);

        tasks.markAsNotDone(index);
        saveTasks(storage, tasks);

        return buildMessage(task);
    }

    /* =========================
       Helper methods
       ========================= */

    /** Validates the index and ensures the task can be unmarked. */
    private Task getValidTask(TaskList tasks) throws CherishException {
        if (index < 0 || index >= tasks.size()) {
            throw new CherishException("Task number out of range! You have " + tasks.size() + " task(s).");
        }

        Task task = tasks.get(index);
        if (!task.isDone()) {
            throw new CherishException("This task is already marked as not done!");
        }

        return task;
    }

    /** Saves the current task list to persistent storage. */
    private void saveTasks(Storage storage, TaskList tasks) throws CherishException {
        storage.save(tasks.toArray());
    }

    /** Builds a user-friendly success message. */
    private String buildMessage(Task task) {
        return "OK! I've marked this task as not done:\n  " + task;
    }
}
