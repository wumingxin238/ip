package cherish.command;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import cherish.CherishException;
import cherish.model.Deadline;
import cherish.model.TaskList;
import cherish.storage.Storage;
import cherish.ui.Ui;

public class DeadlineCommandTest {

    @Test
    void execute_validInput_addsDeadlineTask() throws Exception {
        Ui ui = new Ui(true);
        TaskList tasks = new TaskList();
        Storage storage = new Storage("test.txt");

        DeadlineCommand command =
                new DeadlineCommand("submit report", "2026-01-31 1800");

        String result = command.execute(tasks, ui, storage);

        assertEquals(1, tasks.size());
        assertTrue(tasks.getByIndex(0) instanceof Deadline);
        assertTrue(result.contains("I've added this task"));
    }

    @Test
    void undo_afterExecute_removesLastTask() throws Exception {
        Ui ui = new Ui(true);
        TaskList tasks = new TaskList();
        Storage storage = new Storage("test.txt");

        DeadlineCommand command =
                new DeadlineCommand("submit report", "2026-01-31 1800");

        command.execute(tasks, ui, storage);
        String undoResult = command.undo(tasks, ui, storage);

        assertEquals(0, tasks.size());
        assertTrue(undoResult.contains("I've undone adding this task"));
    }

    @Test
    void execute_invalidDateFormat_throwsException() {
        Ui ui = new Ui(true);
        TaskList tasks = new TaskList();
        Storage storage = new Storage("test.txt");

        DeadlineCommand command =
                new DeadlineCommand("submit report", "31-01-2026 1800");

        assertThrows(
                CherishException.class, () -> command.execute(tasks, ui, storage)
        );
    }

    @Test
    void isExit_returnsFalse() {
        DeadlineCommand command =
                new DeadlineCommand("task", "2026-01-31 1800");

        assertFalse(command.isExit());
    }

    @Test
    void isUndoable_returnsTrue() {
        DeadlineCommand command =
                new DeadlineCommand("task", "2026-01-31 1800");

        assertTrue(command.isUndoable());
    }
}
