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
            throw new CherishException("You didn't type anything! Try a command like 'todo read book'.");
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
            String desc = input.substring(5).trim();
            if (desc.isEmpty()) {
                throw new CherishException("The description of a todo cannot be empty.");
            }
            return new TodoCommand(desc);
        } else if (input.startsWith("deadline ")) {
            return parseDeadline(input);
        } else if (input.startsWith("event ")) {
            return parseEvent(input);
        } else if (input.startsWith("finddate ")) { // Existing command
            String dateStr = input.substring(9).trim();
            if (dateStr.isEmpty()) {
                throw new CherishException("Please specify a date! Usage: finddate yyyy-MM-dd");
            }
            return new FindDateCommand(dateStr);
        } else if (input.startsWith("find ")) { // New command
            String keyword = input.substring(5).trim(); // Extract the keyword after "find "
            if (keyword.isEmpty()) {
                throw new CherishException("Please specify a keyword to search for! Usage: find KEYWORD");
            }
            return new FindCommand(keyword);
        } else {
            throw new CherishException("I don't recognize that command! Try 'todo', "
                    + "'deadline', 'event', 'list', 'find', etc.");
        }
    }

    /**
     * Helper method to extract and validate the task index from commands like 'mark', 'unmark', or 'delete'.
     *
     * @param input The full command string (e.g., "mark 1").
     * @param commandName The name of the command being parsed (e.g., "mark").
     * @return The 0-based integer index of the task.
     * @throws CherishException If the index is missing, not a number, or not positive.
     */
    private static int parseIndex(String input, String commandName) throws CherishException {
        try {
            String numStr = input.substring(commandName.length()).trim();
            int index = Integer.parseInt(numStr) - 1; // convert to 0-based
            if (index < 0) {
                throw new CherishException("Task number must be positive.");
            }
            return index;
        } catch (NumberFormatException e) {
            throw new CherishException("Invalid task number! Please enter a number after '" + commandName + "'.");
        }
    }

    /**
     * Parses a 'deadline' command string.
     * Expected format: "deadline DESCRIPTION /by yyyy-MM-dd HHmm"
     *
     * @param input The full 'deadline' command string.
     * @return A DeadlineCommand object.
     * @throws CherishException If the format is invalid or required parts are missing.
     */
    private static Command parseDeadline(String input) throws CherishException {
        String[] parts = input.split(" /by ", 2);
        if (parts.length != 2) {
            throw new CherishException("Invalid deadline format! Use: deadline DESCRIPTION /by yyyy-MM-dd HHmm");
        }

        // Check the length before substring to avoid StringIndexOutOfBoundsException
        String descPart = parts[0].trim();
        if (descPart.length() <= 9) { // "deadline " is 9 characters long
            if (descPart.equalsIgnoreCase("deadline")) {
                throw new CherishException("The description of a deadline cannot be empty. "
                        + "Format: deadline DESCRIPTION /by yyyy-MM-dd HHmm");
            } else {
                // Handle other malformed cases if needed
                throw new CherishException("Invalid deadline format! Use: deadline DESCRIPTION /by yyyy-MM-dd HHmm");
            }
        }

        String desc = descPart.substring(9).trim(); // Remove "deadline " part
        String by = parts[1].trim();

        // Optional: Double-check after trim if needed, though length check above makes this less likely
        if (desc.isEmpty()) {
            throw new CherishException("The description of a deadline cannot be empty. "
                    + "Format: deadline DESCRIPTION /by yyyy-MM-dd HHmm");
        }
        if (by.isEmpty()) {
            throw new CherishException("The deadline time (/by ...) cannot be empty."
                    + " Format: deadline DESCRIPTION /by yyyy-MM-dd HHmm");
        }
        return new DeadlineCommand(desc, by);
    }

    /**
     * Parses an 'event' command string.
     * Expected format: "event DESCRIPTION /from yyyy-MM-dd HHmm /to yyyy-MM-dd HHmm"
     *
     * @param input The full 'event' command string.
     * @return An EventCommand object.
     * @throws CherishException If the format is invalid or required parts are missing.
     */
    private static Command parseEvent(String input) throws CherishException {
        String[] parts = input.split(" /from ", 2);
        if (parts.length != 2) {
            throw new CherishException("Invalid event format! Use: event DESC"
                    + " /from YYYY-MM-DD HHMM /to YYYY-MM-DD HHMM");
        }

        // Check the length before substring to avoid StringIndexOutOfBoundsException
        String descPart = parts[0].trim();
        if (descPart.length() <= 6) { // "event " is 6 characters long
            if (descPart.equalsIgnoreCase("event")) {
                throw new CherishException("The description of an event cannot be empty."
                        + " Format: event DESCRIPTION /from yyyy-MM-dd HHmm /to yyyy-MM-dd HHmm");
            } else {
                // Handle other malformed cases if needed, but mostly the above check is sufficient
                throw new CherishException("Invalid event format!"
                        + " Use: event DESC /from YYYY-MM-DD HHMM /to YYYY-MM-DD HHMM");
            }
        }

        String desc = descPart.substring(6).trim(); // Remove "event " part
        String[] fromTo = parts[1].split(" /to ", 2);
        if (fromTo.length != 2) {
            throw new CherishException("Invalid event format! Missing '/to'.");
        }
        String from = fromTo[0].trim();
        String to = fromTo[1].trim();
        if (desc.isEmpty() || from.isEmpty() || to.isEmpty()) {
            throw new CherishException("Event description, start, and end times cannot be empty.");
        }
        return new EventCommand(desc, from, to);
    }
}