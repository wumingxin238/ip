package cherish.command;

import cherish.CherishException;
import cherish.storage.Storage;
import cherish.model.Task;
import cherish.model.TaskList;
import cherish.ui.Ui;

public class DeleteCommand extends Command {

    private int index;

    public DeleteCommand(int index) {
        this.index = index;
    }

    @Override
    public String execute(TaskList tasks, Ui ui, Storage storage) throws CherishException {
        // Validate index
        if (index < 0 || index >= tasks.size()) {
            throw new CherishException("Task number out of range! You have " + tasks.size() + " task(s).");
        }

        // Get the task to delete (for display)
        Task deletedTask = tasks.get(index);

        // Perform deletion
        tasks.remove(index);

        // Save updated list to file
        try {
            storage.save(tasks.toArray());
        } catch (CherishException e) {
            // Optional: log or warn, but don't crash
            // For now, we assume save is critical; if it fails, let exception propagate
            throw new CherishException("Failed to update task file after deletion.");
        }

        // Construct success message
        return "Noted! I've removed this task:\n  " + deletedTask +
                "\nNow you have " + tasks.size() + (tasks.size() == 1 ? " task" : " tasks") + " in your list.";
    }
}