package cherish.command;

import cherish.CherishException;
import cherish.model.Task;
import cherish.model.TaskList;
import cherish.storage.Storage;
import cherish.ui.Ui;

/**
 * Command to mark a task as completed based on its index in the task list.
 */
public class MarkCommand extends Command {

    private final int index;

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
        Task task = getValidTaskForMark(tasks);

        tasks.markAsDone(index);
        saveTasks(storage, tasks);

        return buildMessage(task);
    }

    @Override
    public String undo(TaskList tasks, Ui ui, Storage storage) throws CherishException {
        Task task = getValidTaskForUndo(tasks);

        tasks.markAsNotDone(index);
        saveTasks(storage, tasks);

        return buildUndoMessage(task);
    }

    /* =========================
       Helper methods
       ========================= */

    /** Validates the task for a mark operation. */
    private Task getValidTaskForMark(TaskList tasks) throws CherishException {
        Task task = getTaskByIndex(tasks);

        if (task.isDone()) {
            throw new CherishException(
                    "Looks like this task is already done "
            );
        }

        return task;
    }

    /** Validates the task for undoing a mark operation. */
    private Task getValidTaskForUndo(TaskList tasks) throws CherishException {
        Task task = getTaskByIndex(tasks);

        if (!task.isDone()) {
            throw new CherishException(
                    "Hmm~ this task is already not done "
            );
        }

        return task;
    }

    /** Returns the task at index or throws if index is invalid. */
    private Task getTaskByIndex(TaskList tasks) throws CherishException {
        if (index < 0 || index >= tasks.size()) {
            throw new CherishException(
                    "Task number out of range! You have "
                            + tasks.size()
                            + (tasks.size() == 1 ? " task." : " tasks.")
            );
        }
        return tasks.getByIndex(index);
    }

    private void saveTasks(Storage storage, TaskList tasks) throws CherishException {
        storage.save(tasks.toArray());
    }

    /** Builds a user-friendly success message. */
    private String buildMessage(Task task) {
        return "Yay! This task is done:) \n  " + task;
    }

    /** Builds a user-friendly undo success message. */
    private String buildUndoMessage(Task task) {
        return "That's okay - I've marked it as not done again \n  " + task;
    }
}
