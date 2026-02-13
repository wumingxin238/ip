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

        Task deletedTask = tasks.get(index);
        tasks.remove(index);

        saveTasks(storage, tasks);

        return buildSuccessMessage(deletedTask, tasks.size());
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
        return "Noted! I've removed this task:\n  "
                + deletedTask
                + "\nNow you have "
                + taskCount
                + (taskCount == 1 ? " task" : " tasks")
                + " in your list.";
    }
}
