package cherish.command;

import cherish.CherishException;
import cherish.model.TaskList;
import cherish.model.Todo;
import cherish.storage.Storage;
import cherish.ui.Ui;

/**
 * Command to add a new Todo task to the task list.
 * A Todo is a simple task without a specific date or time.
 */
public class TodoCommand extends Command {

    private final String description;

    /**
     * Constructs a TodoCommand with the given task description.
     *
     * @param description The description of the todo task.
     */
    public TodoCommand(String description) {
        this.description = description;
    }

    @Override
    public String execute(TaskList tasks, Ui ui, Storage storage) throws CherishException {
        Todo todo = new Todo(description);
        tasks.add(todo);

        saveTasks(storage, tasks);

        return buildMessage(todo, tasks);
    }

    @Override
    public String undo(TaskList tasks, Ui ui, Storage storage) throws CherishException {
        // Since TodoCommand always adds a task to the end of the list,
        // undoing means removing the last task added.
        Todo removedTodo = (Todo) tasks.pop(); // Cast because pop returns Task, but we know it was a Todo

        saveTasks(storage, tasks);

        return buildUndoMessage(removedTodo, tasks.size());
    }

    /* =========================
       Helper methods
       ========================= */

    /** Saves the current task list to persistent storage. */
    private void saveTasks(Storage storage, TaskList tasks) throws CherishException {
        storage.save(tasks.toArray());
    }

    /** Builds a user-friendly message for task addition. */
    private String buildMessage(Todo todo, TaskList tasks) {
        return "Got it! I've added this task:\n  " + todo
                + "\nNow you have " + tasks.size()
                + (tasks.size() == 1 ? " task" : " tasks") + " in your list.";
    }

    private String buildUndoMessage(Todo todo, int taskCount) {
        return "Great! You have undone adding this task:\n  " + todo
                + "\nNow you have " + taskCount
                + (taskCount == 1 ? " task" : " tasks") + " in your list.";
    }
}
