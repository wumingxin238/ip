package cherish.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;

import cherish.CherishException;

/**
 * Represents an Event task in the Cherish application.
 * An Event has a description and a specific start and end date/time.
 */
public class Event extends Task {

    private static final DateTimeFormatter INPUT_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm", Locale.ENGLISH);

    private static final DateTimeFormatter DISPLAY_FORMATTER =
            DateTimeFormatter.ofPattern("MMM dd yyyy HHmm", Locale.ENGLISH);

    protected final LocalDateTime from;
    protected final LocalDateTime to;

    /**
     * Constructs an Event task from user input.
     *
     * @param description Description of the event.
     * @param fromStr Start date and time in yyyy-MM-dd HHmm format.
     * @param toStr End date and time in yyyy-MM-dd HHmm format.
     * @throws CherishException If either date/time string is invalid.
     */
    public Event(String description, String fromStr, String toStr) throws CherishException {
        super(description);
        this.from = parseDateTime(fromStr);
        this.to = parseDateTime(toStr);
    }

    /**
     * Constructs an Event task from stored data.
     *
     * @param description Description of the event.
     * @param from Start date and time.
     * @param to End date and time.
     */
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
        return super.toString()
                + " (from: "
                + from.format(DISPLAY_FORMATTER)
                + " to: "
                + to.format(DISPLAY_FORMATTER)
                + ")";
    }

    @Override
    public String toFileString() {
        return "E | "
                + (isDone ? "1" : "0")
                + " | "
                + description
                + " | "
                + from.format(INPUT_FORMATTER)
                + " | "
                + to.format(INPUT_FORMATTER);
    }

    /**
     * Returns the start date and time of the event.
     */
    public LocalDateTime getFrom() {
        return from;
    }

    /**
     * Returns the end date and time of the event.
     */
    public LocalDateTime getTo() {
        return to;
    }

    /* =========================
       Helper methods
       ========================= */

    private static LocalDateTime parseDateTime(String dateTimeStr) throws CherishException {
        try {
            return LocalDateTime.parse(dateTimeStr, INPUT_FORMATTER);
        } catch (DateTimeParseException e) {
            throw new CherishException(
                    "Invalid date/time format! Please use 'yyyy-MM-dd HHmm'."
            );
        }
    }
}
