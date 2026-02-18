package cherish.command;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import cherish.CherishException;
import cherish.model.TaskList;
import cherish.model.Todo;
import cherish.storage.Storage;
import cherish.ui.Ui;

public class DeleteCommandTest {

    @Test
    void execute_validIndex_removesTask() throws Exception {
        Ui ui = new Ui(true);
        TaskList tasks = new TaskList();
        Storage storage = new Storage("test.txt");

        tasks.add(new Todo("task one"));
        tasks.add(new Todo("task two"));

        DeleteCommand command = new DeleteCommand(0);

        String result = command.execute(tasks, ui, storage);

        assertEquals(1, tasks.size());
        assertTrue(result.contains("I've removed this task"));
        assertEquals("task two", tasks.getByIndex(0).getDescription());
    }

    @Test
    void undo_afterExecute_restoresTaskAtSameIndex() throws Exception {
        Ui ui = new Ui(true);
        TaskList tasks = new TaskList();
        Storage storage = new Storage("test.txt");

        tasks.add(new Todo("task one"));
        tasks.add(new Todo("task two"));

        DeleteCommand command = new DeleteCommand(1);

        command.execute(tasks, ui, storage);
        String undoResult = command.undo(tasks, ui, storage);

        assertEquals(2, tasks.size());
        assertEquals("task two", tasks.getByIndex(1).getDescription());
        assertTrue(undoResult.contains("I've restored this task"));
    }

    @Test
    void execute_invalidIndex_throwsException() throws CherishException {
        Ui ui = new Ui(true);
        TaskList tasks = new TaskList();
        Storage storage = new Storage("test.txt");

        tasks.add(new Todo("only task"));

        DeleteCommand command = new DeleteCommand(5);

        assertThrows(
                CherishException.class, () -> command.execute(tasks, ui, storage)
        );

        assertEquals(1, tasks.size());
    }

    @Test
    void undo_withoutExecute_throwsException() {
        Ui ui = new Ui(true);
        TaskList tasks = new TaskList();
        Storage storage = new Storage("test.txt");

        DeleteCommand command = new DeleteCommand(0);

        assertThrows(
                CherishException.class, () -> command.undo(tasks, ui, storage)
        );
    }

    @Test
    void isExit_returnsFalse() {
        DeleteCommand command = new DeleteCommand(0);
        assertFalse(command.isExit());
    }

    @Test
    void isUndoable_returnsTrue() {
        DeleteCommand command = new DeleteCommand(0);
        assertTrue(command.isUndoable());
    }
}
