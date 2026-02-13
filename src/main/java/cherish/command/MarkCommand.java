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
        validateIndex(tasks);

        tasks.markAsDone(index);
        Task markedTask = tasks.get(index);

        saveTasks(storage, tasks);

        return "Great! I've marked this task as done:\n  " + markedTask;
    }

    /* =========================
       Helper methods
       ========================= */

    private void validateIndex(TaskList tasks) throws CherishException {
        if (index < 0 || index >= tasks.size()) {
            throw new CherishException(
                    "Task number out of range! You have "
                            + tasks.size()
                            + (tasks.size() == 1 ? " task." : " tasks.")
            );
        }
    }

    private void saveTasks(Storage storage, TaskList tasks) throws CherishException {
        storage.save(tasks.toArray());
    }
}
