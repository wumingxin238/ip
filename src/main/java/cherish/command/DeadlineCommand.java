package cherish.command;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import cherish.CherishException;
import cherish.model.Deadline;
import cherish.model.TaskList;
import cherish.storage.Storage;
import cherish.ui.Ui;

/**
 * Command to add a new Deadline task.
 */
public class DeadlineCommand extends Command {

    private static final DateTimeFormatter INPUT_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm");

    private final String description;
    private final String byString;

    /**
     * Creates a DeadlineCommand.
     *
     * @param description Description of the deadline.
     * @param byString Deadline date and time in yyyy-MM-dd HHmm format.
     */
    public DeadlineCommand(String description, String byString) {
        this.description = description;
        this.byString = byString;
    }

    @Override
    public String execute(TaskList tasks, Ui ui, Storage storage) throws CherishException {
        LocalDateTime by = parseDateTime(byString);

        Deadline deadline = new Deadline(description, by);
        tasks.add(deadline);

        saveTasks(storage, tasks);

        return buildSuccessMessage(deadline, tasks.size());
    }

    @Override
    public String undo(TaskList tasks, Ui ui, Storage storage) throws CherishException {
        // Since DeadlineCommand always adds a task to the end of the list,
        // undoing means removing the last task added.
        Deadline removedDeadline = (Deadline) tasks.pop(); // Cast because pop returns Task

        saveTasks(storage, tasks);

        return buildUndoMessage(removedDeadline, tasks.size());
    }

    /* =========================
       Helper methods
       ========================= */

    private LocalDateTime parseDateTime(String dateTimeStr) throws CherishException {
        try {
            return LocalDateTime.parse(dateTimeStr, INPUT_FORMATTER);
        } catch (DateTimeParseException e) {
            throw new CherishException(
                    "Invalid date/time format! Use yyyy-MM-dd HHmm (e.g., 2026-01-31 1800)."
            );
        }
    }

    private void saveTasks(Storage storage, TaskList tasks) throws CherishException {
        storage.save(tasks.toArray());
    }

    private String buildSuccessMessage(Deadline deadline, int taskCount) {
        return "Got it! I've added this task:\n  "
                + deadline
                + "\nNow you have "
                + taskCount
                + (taskCount == 1 ? " task" : " tasks")
                + " in your list.";
    }

    private String buildUndoMessage(Deadline deadline, int taskCount) {
        return "Great! You have undone adding this task:\n  " + deadline
                + "\nNow you have " + taskCount
                + (taskCount == 1 ? " task" : " tasks") + " in your list.";
    }
}
