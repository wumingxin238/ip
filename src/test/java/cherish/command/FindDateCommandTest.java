package cherish.command;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import cherish.CherishException;
import cherish.model.Deadline;
import cherish.model.Event;
import cherish.model.TaskList;
import cherish.model.Todo;
import cherish.storage.Storage;
import cherish.ui.Ui;

public class FindDateCommandTest {

    @Test
    void execute_deadlineOnDate_returnsTask() throws Exception {
        TaskList tasks = new TaskList();
        Ui ui = new Ui(true);
        Storage storage = new Storage("test.txt");

        tasks.add(new Deadline("submit report", "2026-02-01 1800"));
        tasks.add(new Todo("random task"));

        FindDateCommand command = new FindDateCommand("2026-02-01");

        String result = command.execute(tasks, ui, storage);

        assertTrue(result.contains("submit report"));
        assertFalse(result.contains("random task"));
    }

    @Test
    void execute_eventSpanningDate_returnsTask() throws Exception {
        TaskList tasks = new TaskList();
        Ui ui = new Ui(true);
        Storage storage = new Storage("test.txt");

        tasks.add(new Event(
                "conference",
                "2026-01-31 0900",
                "2026-02-02 1700"
        ));

        FindDateCommand command = new FindDateCommand("2026-02-01");

        String result = command.execute(tasks, ui, storage);

        assertTrue(result.contains("conference"));
    }

    @Test
    void execute_noTasksOnDate_returnsNoTasksMessage() throws Exception {
        TaskList tasks = new TaskList();
        Ui ui = new Ui(true);
        Storage storage = new Storage("test.txt");

        tasks.add(new Todo("do laundry"));

        FindDateCommand command = new FindDateCommand("2026-02-01");

        String result = command.execute(tasks, ui, storage);

        assertEquals("No tasks found on 2026-02-01.", result);
    }

    @Test
    void execute_invalidDateFormat_throwsException() {
        TaskList tasks = new TaskList();
        Ui ui = new Ui(true);
        Storage storage = new Storage("test.txt");

        FindDateCommand command = new FindDateCommand("01-02-2026");

        assertThrows(
                CherishException.class, () -> command.execute(tasks, ui, storage)
        );
    }

    @Test
    void isUndoable_returnsFalse() {
        FindDateCommand command = new FindDateCommand("2026-02-01");

        assertFalse(command.isUndoable());
    }
}
