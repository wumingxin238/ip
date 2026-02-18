package cherish.command;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import cherish.CherishException;
import cherish.model.TaskList;
import cherish.model.Todo;
import cherish.storage.Storage;
import cherish.ui.Ui;

public class MarkCommandTest {

    @Test
    void execute_validIndex_marksTaskAsDone() throws Exception {
        TaskList tasks = new TaskList();
        Ui ui = new Ui(true);
        Storage storage = new Storage("test.txt");

        tasks.add(new Todo("read book"));

        MarkCommand command = new MarkCommand(0);

        String result = command.execute(tasks, ui, storage);

        assertTrue(tasks.getByIndex(0).isDone());
        assertTrue(result.contains("This task is done"));
    }

    @Test
    void undo_afterExecute_marksTaskAsNotDone() throws Exception {
        TaskList tasks = new TaskList();
        Ui ui = new Ui(true);
        Storage storage = new Storage("test.txt");

        tasks.add(new Todo("read book"));

        MarkCommand command = new MarkCommand(0);

        command.execute(tasks, ui, storage);
        String undoResult = command.undo(tasks, ui, storage);

        assertFalse(tasks.getByIndex(0).isDone());
        assertTrue(undoResult.contains("marked it as not done again"));
    }

    @Test
    void execute_taskAlreadyDone_throwsException() throws Exception {
        TaskList tasks = new TaskList();
        Ui ui = new Ui(true);
        Storage storage = new Storage("test.txt");

        Todo todo = new Todo("read book");
        todo.markAsDone();
        tasks.add(todo);

        MarkCommand command = new MarkCommand(0);

        assertThrows(
                CherishException.class, () -> command.execute(tasks, ui, storage)
        );
    }

    @Test
    void undo_taskAlreadyNotDone_throwsException() throws Exception {
        TaskList tasks = new TaskList();
        Ui ui = new Ui(true);
        Storage storage = new Storage("test.txt");

        tasks.add(new Todo("read book"));

        MarkCommand command = new MarkCommand(0);

        assertThrows(
                CherishException.class, () -> command.undo(tasks, ui, storage)
        );
    }

    @Test
    void execute_invalidIndex_throwsException() {
        TaskList tasks = new TaskList();
        Ui ui = new Ui(true);
        Storage storage = new Storage("test.txt");

        MarkCommand command = new MarkCommand(1);

        assertThrows(
                CherishException.class, () -> command.execute(tasks, ui, storage)
        );
    }

    @Test
    void isUndoable_returnsTrue() {
        MarkCommand command = new MarkCommand(0);

        assertTrue(command.isUndoable());
    }

    @Test
    void isExit_returnsFalse() {
        MarkCommand command = new MarkCommand(0);

        assertFalse(command.isExit());
    }
}
