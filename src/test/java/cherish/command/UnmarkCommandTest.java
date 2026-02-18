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

public class UnmarkCommandTest {

    @Test
    void execute_validIndex_unmarksTask() throws Exception {
        TaskList tasks = new TaskList();
        Ui ui = new Ui(true);
        Storage storage = new Storage("test.txt");

        Todo todo = new Todo("read book");
        todo.markAsDone();
        tasks.add(todo);

        UnmarkCommand command = new UnmarkCommand(0);

        String result = command.execute(tasks, ui, storage);

        assertFalse(tasks.getByIndex(0).isDone());
        assertTrue(result.contains("marked as not done"));
    }

    @Test
    void undo_afterExecute_marksTaskAsDoneAgain() throws Exception {
        TaskList tasks = new TaskList();
        Ui ui = new Ui(true);
        Storage storage = new Storage("test.txt");

        Todo todo = new Todo("read book");
        todo.markAsDone();
        tasks.add(todo);

        UnmarkCommand command = new UnmarkCommand(0);

        command.execute(tasks, ui, storage);
        String undoResult = command.undo(tasks, ui, storage);

        assertTrue(tasks.getByIndex(0).isDone());
        assertTrue(undoResult.contains("completed again"));
    }

    @Test
    void execute_taskAlreadyNotDone_throwsException() throws CherishException {
        TaskList tasks = new TaskList();
        Ui ui = new Ui(true);
        Storage storage = new Storage("test.txt");

        tasks.add(new Todo("read book"));

        UnmarkCommand command = new UnmarkCommand(0);

        assertThrows(
                CherishException.class, () -> command.execute(tasks, ui, storage)
        );
    }

    @Test
    void undo_taskAlreadyDone_throwsException() throws Exception {
        TaskList tasks = new TaskList();
        Ui ui = new Ui(true);
        Storage storage = new Storage("test.txt");

        Todo todo = new Todo("read book");
        todo.markAsDone();
        tasks.add(todo);

        UnmarkCommand command = new UnmarkCommand(0);

        assertThrows(
                CherishException.class, () -> command.undo(tasks, ui, storage)
        );
    }

    @Test
    void execute_invalidIndex_throwsException() {
        TaskList tasks = new TaskList();
        Ui ui = new Ui(true);
        Storage storage = new Storage("test.txt");

        UnmarkCommand command = new UnmarkCommand(1);

        assertThrows(
                CherishException.class, () -> command.execute(tasks, ui, storage)
        );
    }

    @Test
    void isUndoable_returnsTrue() {
        UnmarkCommand command = new UnmarkCommand(0);

        assertTrue(command.isUndoable());
    }

    @Test
    void isExit_returnsFalse() {
        UnmarkCommand command = new UnmarkCommand(0);

        assertFalse(command.isExit());
    }
}
