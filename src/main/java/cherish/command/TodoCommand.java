package cherish.command;

import cherish.CherishException;
import cherish.storage.Storage;
import cherish.model.TaskList;
import cherish.model.Todo;
import cherish.ui.Ui;

public class TodoCommand extends Command {

    private String description;

    public TodoCommand(String description) {
        this.description = description;
    }

    @Override
    public String execute(TaskList tasks, Ui ui, Storage storage) throws CherishException {
        Todo todo = new Todo(description);
        tasks.add(todo);
        String msg = "Got it! I've added this task:\n  " + todo +
                "\nNow you have " + tasks.size() + (tasks.size() == 1 ? " task" : " tasks") + " in your list.";
        // Save after adding
        try {
            storage.save(tasks.toArray());
        } catch (CherishException e) {
            // Optional: warn user save failed, but don't crash
        }
        return msg;
    }
}
