package cherish.command;

import cherish.CherishException;
import cherish.model.Task;
import cherish.model.TaskList;
import cherish.storage.Storage;
import cherish.ui.Ui;

/**
 * Command to delete a task from the task list.
 */
public class DeleteCommand extends Command {

    private final int index;

    private Task removedTask;
    private int removedIndex;
    /**
     * Creates a DeleteCommand.
     *
     * @param index Zero-based index of the task to delete.
     */
    public DeleteCommand(int index) {
        this.index = index;
    }

    @Override
    public String execute(TaskList tasks, Ui ui, Storage storage) throws CherishException {
        validateIndex(tasks);

        Task deletedTask = tasks.getByIndex(index);
        removedTask = deletedTask;
        removedIndex = index;
        tasks.remove(index);
        saveTasks(storage, tasks);

        return buildSuccessMessage(deletedTask, tasks.size());
    }

    @Override
    public String undo(TaskList tasks, Ui ui, Storage storage) throws CherishException {
        // Check if execute was called successfully
        if (removedTask == null) {
            throw new CherishException("Cannot undo DeleteCommand: no task was removed during execution.");
        }

        // Re-add the stored task at its original index using the new method
        tasks.addByIndex(removedIndex, removedTask);

        saveTasks(storage, tasks);

        return buildUndoMessage(removedTask, tasks.size());
    }

    /* =========================
       Helper methods
       ========================= */

    private void validateIndex(TaskList tasks) throws CherishException {
        if (index < 0 || index >= tasks.size()) {
            throw new CherishException(
                    "Task number out of range! You have " + tasks.size() + " task(s)."
            );
        }
    }

    private void saveTasks(Storage storage, TaskList tasks) throws CherishException {
        storage.save(tasks.toArray());
    }

    private String buildSuccessMessage(Task deletedTask, int taskCount) {
        return "Alright, I've removed this task for you \n  "
                + deletedTask
                + "\nNow you have "
                + taskCount
                + (taskCount == 1 ? " task" : " tasks")
                + " in your list.";
    }

    private String buildUndoMessage(Task task, int taskCount) {
        return "It's back! I've restored this task \n  "
                + task
                + "\nNow you have "
                + taskCount
                + (taskCount == 1 ? " task" : " tasks")
                + " in your list.";
    }
}
