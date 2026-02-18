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
 * Parses raw user input strings into executable {@link Command} objects.
 * The Parser is responsible for:
 * Trimming and normalizing user input
 * Validating command formats and required parameters
 * Detecting missing, invalid, or duplicated parameters
 * Throwing {@link CherishException} with user-friendly messages
 * All command-format-related errors should be handled here and not deferred
 * to command execution.
 */
public class Parser {

    /**
     * Parses the full user input and returns the corresponding {@link Command}.
     * @param fullCommand Raw command string entered by the user.
     * @return A {@link Command} object representing the user instruction.
     * @throws CherishException If the input is empty, unrecognized, or malformed.
     */
    public static Command parse(String fullCommand) throws CherishException {
        if (fullCommand == null || fullCommand.trim().isEmpty()) {
            throw new CherishException(
                    "You didn't type anything! Try a command like 'todo read book'."
            );
        }

        // Normalize whitespace
        String input = fullCommand.trim().replaceAll("\\s+", " ");

        switch (input) {
        case "bye":
            return new ByeCommand();
        case "list":
            return new ListCommand();
        case "undo":
            return new UndoCommand();
        case "mark":
            throw new CherishException("Please specify a task number. Usage: mark TASK_NUMBER");
        case "unmark":
            throw new CherishException("Please specify a task number. Usage: unmark TASK_NUMBER");
        case "delete":
            throw new CherishException("Please specify a task number. Usage: delete TASK_NUMBER");
        case "todo":
            throw new CherishException("The description of a todo cannot be empty. "
                    + "Format: todo DESCRIPTION");
        case "deadline":
            throw new CherishException(
                    "The description of a deadline cannot be empty. "
                            + "Format: deadline DESCRIPTION /by yyyy-MM-dd HHmm"
            );
        case "event":
            throw new CherishException(
                    "The description of an event cannot be empty. "
                            + "Format: event DESCRIPTION /from yyyy-MM-dd HHmm /to yyyy-MM-dd HHmm"
            );
        case "find":
            throw new CherishException("Please specify a keyword to search for!");
        case "finddate":
            throw new CherishException("Please specify a date! Usage: finddate yyyy-MM-dd");
        default:
            break;
        }

        if (input.startsWith("mark ")) {
            return new MarkCommand(parseIndex(input, "mark"));
        } else if (input.startsWith("unmark ")) {
            return new UnmarkCommand(parseIndex(input, "unmark"));
        } else if (input.startsWith("delete ")) {
            return new DeleteCommand(parseIndex(input, "delete"));
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
        } else {
            throw new CherishException(
                    "I don't recognize that command! "
                            + "Try 'todo', 'deadline', 'event', 'list', 'find', etc."
            );
        }
    }

    /**
     * Parses and validates a 1-based task index.
     * @param input Full command string.
     * @param commandName Name of the command (e.g. "mark").
     * @return Zero-based task index.
     * @throws CherishException If the index is missing or invalid.
     */
    private static int parseIndex(String input, String commandName) throws CherishException {
        String numStr = input.substring(commandName.length()).trim();

        if (numStr.contains(" ")) {
            throw new CherishException("Please provide only one task number.");
        }

        try {
            int index = Integer.parseInt(numStr) - 1;
            if (index < 0) {
                throw new CherishException("Task number must be a positive integer.");
            }
            return index;
        } catch (NumberFormatException e) {
            throw new CherishException(
                    "Invalid task number! Please enter a number after '" + commandName + "'."
            );
        }
    }

    /**
     * Parses a {@code todo} command.
     *
     * @param input Full command string.
     * @return A {@link TodoCommand}.
     * @throws CherishException If the description is missing.
     */
    private static Command parseTodo(String input) throws CherishException {
        String desc = input.substring("todo".length()).trim();
        if (desc.isEmpty()) {
            throw new CherishException("The description of a todo cannot be empty. "
                    + "Format: TODO DESCRIPTION"
            );
        }
        return new TodoCommand(desc);
    }

    /**
     * Parses a {@code deadline} command.
     * Format: {@code deadline DESCRIPTION /by yyyy-MM-dd HHmm}
     *
     * @param input Full command string.
     * @return A {@link DeadlineCommand}.
     * @throws CherishException If the format is invalid or parameters are missing.
     */
    private static Command parseDeadline(String input) throws CherishException {
        if (input.indexOf(" /by ") != input.lastIndexOf(" /by ")) {
            throw new CherishException("Deadline cannot have multiple /by.");
        }

        String[] parts = input.split(" /by ", 2);
        if (parts.length != 2) {
            throw new CherishException(
                    "Invalid deadline format! Use: deadline DESCRIPTION /by yyyy-MM-dd HHmm"
            );
        }

        String desc = parts[0].substring("deadline".length()).trim();
        String by = parts[1].trim();

        if (desc.isEmpty()) {
            throw new CherishException("The description of a deadline cannot be empty.");
        }
        if (by.isEmpty()) {
            throw new CherishException("The deadline time (/by ...) cannot be empty.");
        }

        return new DeadlineCommand(desc, by);
    }

    /**
     * Parses an {@code event} command.
     * Format: {@code event DESCRIPTION /from yyyy-MM-dd HHmm /to yyyy-MM-dd HHmm}
     *
     * @param input Full command string.
     * @return An {@link EventCommand}.
     * @throws CherishException If the format is invalid or parameters are missing.
     */
    private static Command parseEvent(String input) throws CherishException {
        if (input.indexOf(" /from ") != input.lastIndexOf(" /from ")) {
            throw new CherishException("Event cannot have multiple /from.");
        }
        if (input.indexOf(" /to ") != input.lastIndexOf(" /to ")) {
            throw new CherishException("Event cannot have multiple /to.");
        }

        String[] parts = input.split(" /from ", 2);
        if (parts.length != 2) {
            throw new CherishException(
                    "Invalid event format! "
                            + "Use: event DESCRIPTION /from yyyy-MM-dd HHmm /to yyyy-MM-dd HHmm"
            );
        }

        String desc = parts[0].substring("event".length()).trim();
        String[] fromTo = parts[1].split(" /to ", 2);

        if (fromTo.length != 2) {
            throw new CherishException("Invalid event format! Missing '/to'.");
        }

        String from = fromTo[0].trim();
        String to = fromTo[1].trim();

        if (desc.isEmpty() || from.isEmpty() || to.isEmpty()) {
            throw new CherishException(
                    "Event description, start time, and end time cannot be empty."
            );
        }

        return new EventCommand(desc, from, to);
    }

    /**
     * Parses a {@code finddate} command.
     *
     * @param input Full command string.
     * @return A {@link FindDateCommand}.
     * @throws CherishException If the date is missing.
     */
    private static Command parseFindDate(String input) throws CherishException {
        String dateStr = input.substring("finddate".length()).trim();
        if (dateStr.isEmpty()) {
            throw new CherishException("Please specify a date! Example usage: finddate yyyy-MM-dd");
        }
        return new FindDateCommand(dateStr);
    }

    /**
     * Parses a {@code find} command.
     *
     * @param input Full command string.
     * @return A {@link FindCommand}.
     * @throws CherishException If the search keyword is missing.
     */
    private static Command parseFind(String input) throws CherishException {
        String keyword = input.substring("find".length()).trim();
        if (keyword.isEmpty()) {
            throw new CherishException(
                    "Please specify a keyword to search for! Example usage: find KEYWORD"
            );
        }
        return new FindCommand(keyword);
    }
}
