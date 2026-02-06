package cherish.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import cherish.CherishException;

/**
 * Represents an Event task in the Cherish application.
 * An Event has a description and a specific start and end date/time.
 */
public class Event extends Task {
    protected LocalDateTime from;
    protected LocalDateTime to;

    /**
     * Constructs an Event task from a description string and start/end date/time strings.
     * This constructor is typically used when parsing user input.
     *
     * @param description The description of the event task.
     * @param fromStr The start date and time string in the format "yyyy-MM-dd HHmm".
     * @param toStr The end date and time string in the format "yyyy-MM-dd HHmm".
     * @throws CherishException If the date/time strings are in an invalid format.
     */
    // Constructor for user input
    public Event(String description, String fromStr, String toStr) throws CherishException {
        super(description);
        try {
            this.from = LocalDateTime.parse(fromStr, DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm"));
            this.to = LocalDateTime.parse(toStr, DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm"));
        } catch (DateTimeParseException e) {
            throw new CherishException("Invalid date/time format!"
                    + " Please use 'yyyy-MM-dd HHmm' for both start and end times.");
        }
    }

    /**
     * Constructs an Event task from a description string and LocalDateTime objects for start and end.
     * This constructor is typically used when loading tasks from a file.
     *
     * @param description The description of the event task.
     * @param from The LocalDateTime object representing the start time.
     * @param to The LocalDateTime object representing the end time.
     * @throws CherishException If any required parameters are missing or invalid (not applicable here).
     */
    // Constructor for loading from file
    public Event(String description, LocalDateTime from, LocalDateTime to) throws CherishException {
        super(description);
        this.from = from;
        this.to = to;
    }

    @Override
    public TaskType getType() {
        return TaskType.EVENT;
    }

    @Override
    public String toString() {
        String formattedFrom = from.format(DateTimeFormatter.ofPattern("MMM dd yyyy HHmm"));
        String formattedTo = to.format(DateTimeFormatter.ofPattern("MMM dd yyyy HHmm"));
        return super.toString()
                + " (from: " + formattedFrom + " to: " + formattedTo + ")";
    }

    @Override
    public String toFileString() {
        String savedFrom = from.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm"));
        String savedTo = to.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm"));
        return "E | " + (isDone ? "1" : "0") + " | " + description + " | " + savedFrom + " | " + savedTo;
    }

    /**
     * Gets the start date and time of the event.
     *
     * @return The LocalDateTime object representing the start time.
     */
    public LocalDateTime getFrom() {
        return from;
    }

    /**
     * Gets the end date and time of the event.
     *
     * @return The LocalDateTime object representing the end time.
     */
    public LocalDateTime getTo() {
        return to;
    }
}
