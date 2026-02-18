package cherish.parser;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import cherish.CherishException;
import cherish.command.ByeCommand;
import cherish.command.DeadlineCommand;
import cherish.command.DeleteCommand;
import cherish.command.EventCommand;
import cherish.command.FindCommand;
import cherish.command.FindDateCommand;
import cherish.command.ListCommand;
import cherish.command.MarkCommand;
import cherish.command.TodoCommand;
import cherish.command.UndoCommand;
import cherish.command.UnmarkCommand;

public class ParserTest {

    /* =====================
       Basic commands
       ===================== */

    @Test
    void parse_byeCommand_success() throws Exception {
        assertInstanceOf(ByeCommand.class, Parser.parse("bye"));
    }

    @Test
    void parse_listCommand_success() throws Exception {
        assertInstanceOf(ListCommand.class, Parser.parse("list"));
    }

    @Test
    void parse_undoCommand_success() throws Exception {
        assertInstanceOf(UndoCommand.class, Parser.parse("undo"));
    }

    /* =====================
       Todo
       ===================== */

    @Test
    void parse_todoWithDescription_success() throws Exception {
        assertInstanceOf(TodoCommand.class, Parser.parse("todo read book"));
    }

    @Test
    void parse_todoWithoutDescription_throwsException() {
        assertThrows(
                CherishException.class, () -> Parser.parse("todo")
        );
    }

    /* =====================
       Deadline
       ===================== */

    @Test
    void parse_deadlineValidFormat_success() throws Exception {
        assertInstanceOf(
                DeadlineCommand.class,
                Parser.parse("deadline submit report /by 2026-02-01 1800")
        );
    }

    @Test
    void parse_deadlineMissingBy_throwsException() {
        assertThrows(
                CherishException.class, () -> Parser.parse("deadline submit report")
        );
    }

    @Test
    void parse_deadlineMultipleBy_throwsException() {
        assertThrows(
                CherishException.class, () -> Parser.parse("deadline a /by b /by c")
        );
    }

    /* =====================
       Event
       ===================== */

    @Test
    void parse_eventValidFormat_success() throws Exception {
        assertInstanceOf(
                EventCommand.class,
                Parser.parse("event meeting /from 2026-02-01 1400 /to 2026-02-01 1600")
        );
    }

    @Test
    void parse_eventMissingFrom_throwsException() {
        assertThrows(
                CherishException.class, () -> Parser.parse("event meeting /to 2026-02-01 1600")
        );
    }

    @Test
    void parse_eventMissingTo_throwsException() {
        assertThrows(
                CherishException.class, () -> Parser.parse("event meeting /from 2026-02-01 1400")
        );
    }

    /* =====================
       Mark / Unmark / Delete
       ===================== */

    @Test
    void parse_markValidIndex_success() throws Exception {
        assertInstanceOf(MarkCommand.class, Parser.parse("mark 1"));
    }

    @Test
    void parse_unmarkValidIndex_success() throws Exception {
        assertInstanceOf(UnmarkCommand.class, Parser.parse("unmark 2"));
    }

    @Test
    void parse_deleteValidIndex_success() throws Exception {
        assertInstanceOf(DeleteCommand.class, Parser.parse("delete 3"));
    }

    @Test
    void parse_markWithoutIndex_throwsException() {
        assertThrows(
                CherishException.class, () -> Parser.parse("mark")
        );
    }

    @Test
    void parse_markNonNumericIndex_throwsException() {
        assertThrows(
                CherishException.class, () -> Parser.parse("mark abc")
        );
    }

    /* =====================
       Find / FindDate
       ===================== */

    @Test
    void parse_findWithKeyword_success() throws Exception {
        assertInstanceOf(FindCommand.class, Parser.parse("find book"));
    }

    @Test
    void parse_findWithoutKeyword_throwsException() {
        assertThrows(
                CherishException.class, () -> Parser.parse("find")
        );
    }

    @Test
    void parse_findDateValid_success() throws Exception {
        assertInstanceOf(
                FindDateCommand.class,
                Parser.parse("finddate 2026-02-01")
        );
    }

    @Test
    void parse_findDateWithoutDate_throwsException() {
        assertThrows(
                CherishException.class, () -> Parser.parse("finddate")
        );
    }

    /* =====================
       Unknown / empty input
       ===================== */

    @Test
    void parse_emptyInput_throwsException() {
        assertThrows(
                CherishException.class, () -> Parser.parse("")
        );
    }

    @Test
    void parse_unknownCommand_throwsException() {
        assertThrows(
                CherishException.class, () -> Parser.parse("dance now")
        );
    }
}
