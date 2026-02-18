package cherish.command;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.Test;

import cherish.model.TaskList;
import cherish.storage.Storage;
import cherish.ui.Ui;

public class UndoCommandTest {

    @Test
    void execute_returnsUndoMessage() throws Exception {
        UndoCommand command = new UndoCommand();
        TaskList tasks = new TaskList();
        Ui ui = new Ui(true);
        Storage storage = new Storage("test.txt");

        String result = command.execute(tasks, ui, storage);

        assertEquals("This is an undo command", result);
    }

    @Test
    void isUndoable_returnsFalse() {
        UndoCommand command = new UndoCommand();

        assertFalse(command.isUndoable());
    }

    @Test
    void isExit_returnsFalse() {
        UndoCommand command = new UndoCommand();

        assertFalse(command.isExit());
    }
}
