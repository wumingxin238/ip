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
        Task task = getValidTaskForUnmark(tasks);

        tasks.markAsNotDone(index);
        saveTasks(storage, tasks);

        return buildMessage(task);
    }

    @Override
    public String undo(TaskList tasks, Ui ui, Storage storage) throws CherishException {
        Task task = getValidTaskForUndo(tasks);
        tasks.markAsDone(index);
        saveTasks(storage, tasks);
        return buildUndoMessage(task);
    }

    /* =========================
       Helper methods
       ========================= */

    /** Validates the task for an unmark operation. */
    private Task getValidTaskForUnmark(TaskList tasks) throws CherishException {
        Task task = getTaskByIndex(tasks);
        if (!task.isDone()) {
            throw new CherishException(
                    "Hmm~ this task is already not done "
            );
        }
        return task;
    }

    /** Validates the task for undoing an unmark operation. */
    private Task getValidTaskForUndo(TaskList tasks) throws CherishException {
        Task task = getTaskByIndex(tasks);
        if (task.isDone()) {
            throw new CherishException(
                    "Looks like this task is already done "
            );
        }
        return task;
    }

    /** Saves the current task list to persistent storage. */
    private void saveTasks(Storage storage, TaskList tasks) throws CherishException {
        storage.save(tasks.toArray());
    }

    /** Returns the task at index or throws if index is invalid. */
    private Task getTaskByIndex(TaskList tasks) throws CherishException {
        if (index < 0 || index >= tasks.size()) {
            throw new CherishException(
                    "Task number out of range! You have " + tasks.size() + " task(s)."
            );
        }
        return tasks.getByIndex(index);
    }

    /** Builds a user-friendly success message. */
    private String buildMessage(Task task) {
        return "No problem! This task is marked as not done \n  " + task;
    }

    /** Builds a user-friendly undo success message. */
    private String buildUndoMessage(Task task) {
        return "Done! I've marked it as completed again \n  " + task;
    }
}
