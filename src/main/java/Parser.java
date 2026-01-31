public class Parser {

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
            if (desc.isEmpty()) throw new CherishException("The description of a todo cannot be empty.");
            return new TodoCommand(desc);
        } else if (input.startsWith("deadline ")) {
            return parseDeadline(input);
        } else if (input.startsWith("event ")) {
            return parseEvent(input);
        } else if (input.startsWith("finddate ")) {
            String dateStr = input.substring(9).trim();
            if (dateStr.isEmpty()) throw new CherishException("Please specify a date! Usage: finddate yyyy-MM-dd");
            return new FindDateCommand(dateStr);
        } else {
            throw new CherishException("I don't recognize that command! Try 'todo', 'deadline', 'event', 'list', etc.");
        }
    }

    private static int parseIndex(String input, String commandName) throws CherishException {
        try {
            String numStr = input.substring(commandName.length()).trim();
            int index = Integer.parseInt(numStr) - 1; // convert to 0-based
            if (index < 0) throw new CherishException("Task number must be positive.");
            return index;
        } catch (NumberFormatException e) {
            throw new CherishException("Invalid task number! Please enter a number after '" + commandName + "'.");
        }
    }

    private static Command parseDeadline(String input) throws CherishException {
        String[] parts = input.split(" /by ", 2);
        if (parts.length != 2) {
            throw new CherishException("Invalid deadline format! Use: deadline DESCRIPTION /by yyyy-MM-dd HHmm");
        }
        String desc = parts[0].substring(9).trim();
        String by = parts[1].trim();
        if (desc.isEmpty() || by.isEmpty()) {
            throw new CherishException("Deadline description and time cannot be empty.");
        }
        return new DeadlineCommand(desc, by);
    }

    private static Command parseEvent(String input) throws CherishException {
        String[] parts = input.split(" /from ", 2);
        if (parts.length != 2) {
            throw new CherishException("Invalid event format! Use: event DESC /from YYYY-MM-DD HHMM /to YYYY-MM-DD HHMM");
        }
        String desc = parts[0].substring(6).trim();
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