package cherish;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class CherishTest {

    @Test
    void getResponse_validAddCommand_addsTask() {
        Cherish cherish = new Cherish("test.txt");

        String response = cherish.getResponse("todo read book");

        assertTrue(response.contains("read book"));
        assertFalse(cherish.shouldExit());
    }

    @Test
    void getResponse_byeCommand_setsExitFlag() {
        Cherish cherish = new Cherish("test.txt");

        cherish.getResponse("bye");

        assertTrue(cherish.shouldExit());
    }

    @Test
    void getResponse_invalidCommand_returnsErrorMessage() {
        Cherish cherish = new Cherish("test.txt");

        String response = cherish.getResponse("this is invalid");

        assertTrue(response.toLowerCase().contains("oops")
                || response.toLowerCase().contains("unknown"));
        assertFalse(cherish.shouldExit());
    }

    @Test
    void undo_withoutHistory_showsNothingToUndoMessage() {
        Cherish cherish = new Cherish("test.txt");

        String response = cherish.getResponse("undo");

        assertTrue(response.contains("Nothing to undo"));
    }

    @Test
    void undo_afterUndoableCommand_revertsState() {
        Cherish cherish = new Cherish("test.txt");

        cherish.getResponse("todo read book");
        cherish.getResponse("mark 1");
        cherish.getResponse("undo");

        String listResponse = cherish.getResponse("list");

        assertTrue(listResponse.contains("[ ]"));
        assertTrue(listResponse.contains("read book"));
    }

}
