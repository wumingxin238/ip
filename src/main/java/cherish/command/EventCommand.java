package cherish.command;

import cherish.CherishException;
import cherish.model.Event;
import cherish.storage.Storage;
import cherish.model.TaskList;
import cherish.ui.Ui;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class EventCommand extends Command {
    private static final DateTimeFormatter INPUT_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm");
    private String description;
    private String fromString;
    private String toString;

    public EventCommand(String description, String fromString, String toString) {
        this.description = description;
        this.fromString = fromString;
        this.toString = toString;
    }

    @Override
    public String execute(TaskList tasks, Ui ui, Storage storage) throws CherishException {
        LocalDateTime from, to;
        try {
            from = LocalDateTime.parse(fromString, INPUT_FORMATTER);
            to = LocalDateTime.parse(toString, INPUT_FORMATTER);
        } catch (DateTimeParseException e) {
            throw new CherishException("Invalid date/time format for event! Use: yyyy-MM-dd HHmm (e.g., 2026-02-01 1400)");
        }

        if (from.isAfter(to)) {
            throw new CherishException("Event start time cannot be after end time!");
        }

        Event event = new Event(description, from, to);
        tasks.add(event);

        try {
            storage.save(tasks.toArray());
        } catch (CherishException e) {
            // Handle save error if needed
        }

        return "Got it! I've added this task:\n  " + event +
                "\nNow you have " + tasks.size() + (tasks.size() == 1 ? " task" : " tasks") + " in your list.";
    }
}
