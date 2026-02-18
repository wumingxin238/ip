package cherish.command;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import cherish.model.TaskList;
import cherish.storage.Storage;
import cherish.ui.Ui;

public class ByeCommandTest {

    @Test
    void execute_showsByeMessageAndReturnsNull() throws Exception {
        Ui ui = new Ui(true);
        TaskList tasks = new TaskList();
        Storage storage = new Storage("test.txt");
        ByeCommand command = new ByeCommand();

        String result = command.execute(tasks, ui, storage);

        assertNull(result);
        assertEquals(
                "Bye. Hope to see you again soon!",
                ui.peekMessagesForGui()
        );
    }

    @Test
    void isExit_returnsTrue() {
        ByeCommand command = new ByeCommand();

        assertTrue(command.isExit());
    }

    @Test
    void isUndoable_returnsFalse() {
        ByeCommand command = new ByeCommand();

        assertFalse(command.isUndoable());
    }
}
