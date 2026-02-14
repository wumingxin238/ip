package cherish.parser;

import cherish.CherishException;
import cherish.command.ByeCommand;
import cherish.command.Command;
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

/**
 * Parses user input commands and returns the corresponding Command object.
 * Handles various command formats and validates input where necessary.
 */
public class Parser {

    /**
     * Parses the full user command string and returns the appropriate Command instance.
     *
     * @param fullCommand The raw command string entered by the user.
     * @return A Command object representing the parsed instruction.
     * @throws CherishException If the command is invalid, unrecognized, or missing required arguments.
     */
    public static Command parse(String fullCommand) throws CherishException {
        if (fullCommand == null || fullCommand.trim().isEmpty()) {
            throw new CherishException(
                    "You didn't type anything! Try a command like 'todo read book'."
            );
        }

        String input = fullCommand.trim();

        if (input.equals("bye")) {
            return new ByeCommand();
        } else if (input.equals("list")) {
            return new ListCommand();
        } else if (input.startsWith("mark ")) {
            int index = parseIndex(input, "mark");
            return new MarkCommand(index);
        } else if (input.startsWith("unmark ")) {
            int index = parseIndex(input, "unmark");
            return new UnmarkCommand(index);
        } else if (input.startsWith("delete ")) {
            int index = parseIndex(input, "delete");
            return new DeleteCommand(index);
        } else if (input.startsWith("todo ")) {
            return parseTodo(input);
        } else if (input.startsWith("deadline ")) {
            return parseDeadline(input);
        } else if (input.startsWith("event ")) {
            return parseEvent(input);
        } else if (input.startsWith("finddate ")) {
            return parseFindDate(input);
        } else if (input.startsWith("find ")) {
            return parseFind(input);
        } else if (input.equals("undo")) {
            return new UndoCommand();
        } else {
            throw new CherishException(
                    "I don't recognize that command! Try 'todo', 'deadline', 'event', 'list', 'find', etc."
            );
        }
    }

    /**
     * Parses and validates the index for commands like 'mark', 'unmark', or 'delete'.
     *
     * @param input The full command string.
     * @param commandName The command type (e.g., "mark").
     * @return The 0-based index of the task.
     * @throws CherishException If the index is invalid.
     */
    private static int parseIndex(String input, String commandName) throws CherishException {
        try {
            String numStr = input.substring(commandName.length()).trim();
            int index = Integer.parseInt(numStr) - 1;
            if (index < 0) {
                throw new CherishException("Task number must be positive.");
            }
            return index;
        } catch (NumberFormatException e) {
            throw new CherishException(
                    "Invalid task number! Please enter a number after '" + commandName + "'."
            );
        }
    }

    /**
     * Parses a 'todo' command.
     *
     * @param input The full command string.
     * @return A TodoCommand object.
     * @throws CherishException If description is missing.
     */
    private static Command parseTodo(String input) throws CherishException {
        String desc = input.substring(5).trim(); // remove "todo "
        if (desc.isEmpty()) {
            throw new CherishException("The description of a todo cannot be empty.");
        }
        return new TodoCommand(desc);
    }

    /**
     * Parses a 'deadline' command.
     *
     * Format: "deadline DESCRIPTION /by yyyy-MM-dd HHmm"
     *
     * @param input The full command string.
     * @return A DeadlineCommand object.
     * @throws CherishException If format or description/time is invalid.
     */
    private static Command parseDeadline(String input) throws CherishException {
        String[] parts = input.split(" /by ", 2);
        if (parts.length != 2) {
            throw new CherishException(
                    "Invalid deadline format! Use: deadline DESCRIPTION /by yyyy-MM-dd HHmm"
            );
        }

        String descPart = parts[0].trim();
        if (descPart.length() <= 9 || descPart.equalsIgnoreCase("deadline")) {
            throw new CherishException(
                    "The description of a deadline cannot be empty. "
                            + "Format: deadline DESCRIPTION /by yyyy-MM-dd HHmm"
            );
        }

        String desc = descPart.substring(9).trim(); // remove "deadline "
        String by = parts[1].trim();

        if (desc.isEmpty()) {
            throw new CherishException(
                    "The description of a deadline cannot be empty. "
                            + "Format: deadline DESCRIPTION /by yyyy-MM-dd HHmm"
            );
        }

        if (by.isEmpty()) {
            throw new CherishException(
                    "The deadline time (/by ...) cannot be empty. "
                            + "Format: deadline DESCRIPTION /by yyyy-MM-dd HHmm"
            );
        }

        return new DeadlineCommand(desc, by);
    }

    /**
     * Parses an 'event' command.
     *
     * Format: "event DESCRIPTION /from yyyy-MM-dd HHmm /to yyyy-MM-dd HHmm"
     *
     * @param input The full command string.
     * @return An EventCommand object.
     * @throws CherishException If format or times are invalid.
     */
    private static Command parseEvent(String input) throws CherishException {
        String[] parts = input.split(" /from ", 2);
        if (parts.length != 2) {
            throw new CherishException(
                    "Invalid event format! Use: event DESCRIPTION /from YYYY-MM-DD HHMM /to YYYY-MM-DD HHMM"
            );
        }

        String descPart = parts[0].trim();
        if (descPart.length() <= 6 || descPart.equalsIgnoreCase("event")) {
            throw new CherishException(
                    "The description of an event cannot be empty. "
                            + "Format: event DESCRIPTION /from yyyy-MM-dd HHmm /to yyyy-MM-dd HHmm"
            );
        }

        String desc = descPart.substring(6).trim(); // remove "event "
        String[] fromTo = parts[1].split(" /to ", 2);
        if (fromTo.length != 2) {
            throw new CherishException("Invalid event format! Missing '/to'.");
        }

        String from = fromTo[0].trim();
        String to = fromTo[1].trim();

        if (desc.isEmpty() || from.isEmpty() || to.isEmpty()) {
            throw new CherishException(
                    "Event description, start, and end times cannot be empty."
            );
        }

        return new EventCommand(desc, from, to);
    }

    /**
     * Parses a 'finddate' command.
     *
     * @param input The full command string.
     * @return A FindDateCommand object.
     * @throws CherishException If date string is empty.
     */
    private static Command parseFindDate(String input) throws CherishException {
        String dateStr = input.substring(9).trim(); // remove "finddate "
        if (dateStr.isEmpty()) {
            throw new CherishException("Please specify a date! Usage: finddate yyyy-MM-dd");
        }
        return new FindDateCommand(dateStr);
    }

    /**
     * Parses a 'find' command.
     *
     * @param input The full command string.
     * @return A FindCommand object.
     * @throws CherishException If keyword is empty.
     */
    private static Command parseFind(String input) throws CherishException {
        String keyword = input.substring(5).trim(); // remove "find "
        if (keyword.isEmpty()) {
            throw new CherishException(
                    "Please specify a keyword to search for! Usage: find KEYWORD"
            );
        }
        return new FindCommand(keyword);
    }
}
