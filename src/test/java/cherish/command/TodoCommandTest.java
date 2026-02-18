package cherish.command;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import cherish.CherishException;
import cherish.model.TaskList;
import cherish.storage.Storage;
import cherish.ui.Ui;

public class TodoCommandTest {

    @Test
    void execute_validDescription_addsTodo() throws Exception {
        TaskList tasks = new TaskList();
        Ui ui = new Ui(true);
        Storage storage = new Storage("test.txt");

        TodoCommand command = new TodoCommand("read book");

        String result = command.execute(tasks, ui, storage);

        assertEquals(1, tasks.size());
        assertEquals("read book", tasks.getByIndex(0).getDescription());
        assertTrue(result.contains("I've added this task"));
    }

    @Test
    void undo_afterExecute_removesTodo() throws Exception {
        TaskList tasks = new TaskList();
        Ui ui = new Ui(true);
        Storage storage = new Storage("test.txt");

        TodoCommand command = new TodoCommand("read book");

        command.execute(tasks, ui, storage);
        String undoResult = command.undo(tasks, ui, storage);

        assertEquals(0, tasks.size());
        assertTrue(undoResult.contains("I've undone adding this task"));
    }

    @Test
    void execute_emptyDescription_throwsException() {
        TaskList tasks = new TaskList();
        Ui ui = new Ui(true);
        Storage storage = new Storage("test.txt");

        TodoCommand command = new TodoCommand("   ");

        assertThrows(
                CherishException.class, () -> command.execute(tasks, ui, storage)
        );
    }

    @Test
    void isUndoable_returnsTrue() {
        TodoCommand command = new TodoCommand("anything");

        assertTrue(command.isUndoable());
    }

    @Test
    void isExit_returnsFalse() {
        TodoCommand command = new TodoCommand("anything");

        assertFalse(command.isExit());
    }
}
