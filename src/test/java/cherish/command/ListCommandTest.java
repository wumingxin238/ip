package cherish.command;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import cherish.model.TaskList;
import cherish.model.Todo;
import cherish.storage.Storage;
import cherish.ui.Ui;

public class ListCommandTest {

    @Test
    void execute_emptyTaskList_returnsEmptyMessage() {
        TaskList tasks = new TaskList();
        Ui ui = new Ui(true);
        Storage storage = new Storage("test.txt");

        ListCommand command = new ListCommand();

        String result = command.execute(tasks, ui, storage);

        assertEquals(
                "Your task list is empty! Add some tasks with 'todo', 'deadline', or 'event'.",
                result
        );
    }

    @Test
    void execute_nonEmptyTaskList_returnsFormattedList() throws Exception {
        TaskList tasks = new TaskList();
        Ui ui = new Ui(true);
        Storage storage = new Storage("test.txt");

        tasks.add(new Todo("read book"));
        tasks.add(new Todo("write report"));

        ListCommand command = new ListCommand();

        String result = command.execute(tasks, ui, storage);

        assertTrue(result.contains("Here are the tasks in your list:"));
        assertTrue(result.contains("read book"));
        assertTrue(result.contains("write report"));
    }

    @Test
    void isUndoable_returnsFalse() {
        ListCommand command = new ListCommand();

        assertFalse(command.isUndoable());
    }
}
