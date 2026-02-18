package cherish.command;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import cherish.model.Deadline;
import cherish.model.TaskList;
import cherish.model.Todo;
import cherish.storage.Storage;
import cherish.ui.Ui;

public class FindCommandTest {

    @Test
    void execute_keywordFound_returnsMatchingTasks() throws Exception {
        TaskList tasks = new TaskList();
        Ui ui = new Ui(true);
        Storage storage = new Storage("test.txt");

        tasks.add(new Todo("read book"));
        tasks.add(new Deadline("return book", "2026-02-01 1800"));
        tasks.add(new Todo("buy groceries"));

        FindCommand command = new FindCommand("book");

        String result = command.execute(tasks, ui, storage);

        assertTrue(result.contains("read book"));
        assertTrue(result.contains("return book"));
        assertFalse(result.contains("buy groceries"));
        assertEquals(3, tasks.size()); // no modification
    }

    @Test
    void execute_noMatch_returnsNoResultsMessage() throws Exception {
        TaskList tasks = new TaskList();
        Ui ui = new Ui(true);
        Storage storage = new Storage("test.txt");

        tasks.add(new Todo("do homework"));

        FindCommand command = new FindCommand("meeting");

        String result = command.execute(tasks, ui, storage);

        assertEquals("No tasks found containing 'meeting'.", result);
        assertEquals(1, tasks.size());
    }

    @Test
    void execute_caseInsensitiveMatch_findsTask() throws Exception {
        TaskList tasks = new TaskList();
        Ui ui = new Ui(true);
        Storage storage = new Storage("test.txt");

        tasks.add(new Todo("Read Lecture Notes"));

        FindCommand command = new FindCommand("read");

        String result = command.execute(tasks, ui, storage);

        assertTrue(result.contains("Read Lecture Notes"));
    }

    @Test
    void isExit_returnsFalse() {
        FindCommand command = new FindCommand("anything");

        assertFalse(command.isExit());
    }

    @Test
    void isUndoable_returnsFalse() {
        FindCommand command = new FindCommand("anything");

        assertFalse(command.isUndoable());
    }
}
