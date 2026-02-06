// src/test/java/cherish/parser/ParserTest.java
package cherish.parser;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import cherish.CherishException;
import cherish.command.ByeCommand;
import cherish.command.Command;
import cherish.command.DeadlineCommand;
import cherish.command.EventCommand;
import cherish.command.ListCommand;
import cherish.command.MarkCommand;
import cherish.command.TodoCommand;

class ParserTest {

    @Test
    void testParseValidTodo() throws CherishException {
        Command command = cherish.parser.Parser.parse("todo Read a book");
        assertTrue(command instanceof TodoCommand, "Should return a TodoCommand");
        // Can optionally check if description is stored correctly if accessible
    }

    @Test
    void testParseValidDeadline() throws CherishException {
        Command command = cherish.parser.Parser.parse("deadline Finish homework /by 2026-02-01 2359");
        assertTrue(command instanceof DeadlineCommand, "Should return a DeadlineCommand");
    }

    @Test
    void testParseValidEvent() throws CherishException {
        Command command = cherish.parser.Parser.parse("event Team meeting /from 2026-02-01 1000 /to 2026-02-01 1100");
        assertTrue(command instanceof EventCommand, "Should return an EventCommand");
    }

    @Test
    void testParseValidMark() throws CherishException {
        Command command = cherish.parser.Parser.parse("mark 2"); // Assuming 2nd task exists
        assertTrue(command instanceof MarkCommand, "Should return a MarkCommand");
        // Note: This test relies on the parser returning the right *type* of command.
        // The *validity* of the index '2' is checked during execution, not parsing.
    }

    @Test
    void testParseValidList() throws CherishException {
        Command command = cherish.parser.Parser.parse("list");
        assertTrue(command instanceof ListCommand, "Should return a ListCommand");
    }

    @Test
    void testParseValidBye() throws CherishException {
        Command command = cherish.parser.Parser.parse("bye");
        assertTrue(command instanceof ByeCommand, "Should return a ByeCommand");
        assertTrue(command.isExit(), "ByeCommand should indicate exit");
    }

    @Test
    void testParseEmptyInput_throwsException() {
        CherishException thrown = assertThrows(CherishException.class, () -> {
            cherish.parser.Parser.parse("");
        }, "Parsing an empty string should throw an exception");
        assertTrue(thrown.getMessage().contains("didn't type anything"), "Exception message should be appropriate");
    }

    @Test
    void testParseInvalidCommand_throwsException() {
        CherishException thrown = assertThrows(CherishException.class, () -> {
            cherish.parser.Parser.parse("invalidcommand sometext");
        }, "Parsing an unknown command should throw an exception");
        assertTrue(thrown.getMessage().contains("don't recognize"), "Exception message should be appropriate");
    }

    @Test
    void testParseMarkInvalidNumber_throwsException() {
        CherishException thrown = assertThrows(CherishException.class, () -> {
            cherish.parser.Parser.parse("mark not_a_number");
        }, "Parsing 'mark' with non-number should throw an exception");
        assertTrue(thrown.getMessage().contains("Invalid task number"), "Exception message should be appropriate");
    }
}
