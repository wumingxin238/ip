package cherish.command;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import cherish.CherishException;
import cherish.model.Event;
import cherish.model.TaskList;
import cherish.storage.Storage;
import cherish.ui.Ui;

public class EventCommandTest {

    @Test
    void execute_validEvent_addsTask() throws Exception {
        Ui ui = new Ui(true);
        TaskList tasks = new TaskList();
        Storage storage = new Storage("test.txt");

        EventCommand command = new EventCommand(
                "team meeting",
                "2026-02-01 1400",
                "2026-02-01 1600"
        );

        String result = command.execute(tasks, ui, storage);

        assertEquals(1, tasks.size());
        assertTrue(tasks.getByIndex(0) instanceof Event);
        assertTrue(result.contains("I've added this event"));
    }

    @Test
    void undo_afterExecute_removesEvent() throws Exception {
        Ui ui = new Ui(true);
        TaskList tasks = new TaskList();
        Storage storage = new Storage("test.txt");

        EventCommand command = new EventCommand(
                "conference",
                "2026-02-02 0900",
                "2026-02-02 1700"
        );

        command.execute(tasks, ui, storage);
        String undoResult = command.undo(tasks, ui, storage);

        assertEquals(0, tasks.size());
        assertTrue(undoResult.contains("I've undone adding this event"));
    }

    @Test
    void execute_invalidDateFormat_throwsException() {
        Ui ui = new Ui(true);
        TaskList tasks = new TaskList();
        Storage storage = new Storage("test.txt");

        EventCommand command = new EventCommand(
                "bad date",
                "invalid-date",
                "2026-02-01 1600"
        );

        assertThrows(
                CherishException.class, () -> command.execute(tasks, ui, storage)
        );

        assertEquals(0, tasks.size());
    }

    @Test
    void execute_endBeforeStart_throwsException() {
        Ui ui = new Ui(true);
        TaskList tasks = new TaskList();
        Storage storage = new Storage("test.txt");

        EventCommand command = new EventCommand(
                "time travel meeting",
                "2026-02-01 1600",
                "2026-02-01 1400"
        );

        assertThrows(
                CherishException.class, () -> command.execute(tasks, ui, storage)
        );

        assertEquals(0, tasks.size());
    }

    @Test
    void isExit_returnsFalse() {
        EventCommand command = new EventCommand(
                "event",
                "2026-02-01 1400",
                "2026-02-01 1500"
        );

        assertFalse(command.isExit());
    }

    @Test
    void isUndoable_returnsTrue() {
        EventCommand command = new EventCommand(
                "event",
                "2026-02-01 1400",
                "2026-02-01 1500"
        );

        assertTrue(command.isUndoable());
    }
}
