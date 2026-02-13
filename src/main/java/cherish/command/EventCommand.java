package cherish.command;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import cherish.CherishException;
import cherish.model.Event;
import cherish.model.TaskList;
import cherish.storage.Storage;
import cherish.ui.Ui;

/**
 * Command to add a new Event task to the task list.
 */
public class EventCommand extends Command {

    private static final DateTimeFormatter INPUT_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm");

    private final String description;
    private final String fromString;
    private final String toString;

    /**
     * Creates an EventCommand.
     *
     * @param description Description of the event.
     * @param fromString Start date/time string.
     * @param toString End date/time string.
     */
    public EventCommand(String description, String fromString, String toString) {
        this.description = description;
        this.fromString = fromString;
        this.toString = toString;
    }

    @Override
    public String execute(TaskList tasks, Ui ui, Storage storage) throws CherishException {
        LocalDateTime from = parseDateTime(fromString, "event");
        LocalDateTime to = parseDateTime(toString, "event");

        validateTimeRange(from, to);

        Event event = new Event(description, from, to);
        tasks.add(event);

        saveTasks(storage, tasks);

        return buildSuccessMessage(event, tasks.size());
    }

    /* =========================
       Helper methods
       ========================= */

    private LocalDateTime parseDateTime(String input, String taskType) throws CherishException {
        try {
            return LocalDateTime.parse(input, INPUT_FORMATTER);
        } catch (DateTimeParseException e) {
            throw new CherishException(
                    "Invalid date/time format for " + taskType + "! "
                            + "Use: yyyy-MM-dd HHmm (e.g., 2026-02-01 1400)"
            );
        }
    }

    private void validateTimeRange(LocalDateTime from, LocalDateTime to) throws CherishException {
        if (from.isAfter(to)) {
            throw new CherishException("Event start time cannot be after end time!");
        }
    }

    private void saveTasks(Storage storage, TaskList tasks) throws CherishException {
        storage.save(tasks.toArray());
    }

    private String buildSuccessMessage(Event event, int taskCount) {
        return "Got it! I've added this task:\n  "
                + event
                + "\nNow you have "
                + taskCount
                + (taskCount == 1 ? " task" : " tasks")
                + " in your list.";
    }
}
