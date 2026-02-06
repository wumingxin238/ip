package cherish.command;

import cherish.CherishException;
import cherish.model.TaskList;
import cherish.storage.Storage;
import cherish.ui.Ui;

/**
 * Command to find tasks in the list based on a keyword in their description.
 */
public class FindCommand extends Command {
    private final String keyword;

    /**
     * Constructs a FindCommand with the keyword to search for.
     *
     * @param keyword The keyword to search for within task descriptions.
     */
    public FindCommand(String keyword) {
        this.keyword = keyword;
    }

    @Override
    public String execute(TaskList tasks, Ui ui, Storage storage) throws CherishException {
        // Delegate the search logic to the TaskList model
        String results = tasks.findTasksByKeyword(this.keyword);
        return results; // Return the formatted string from TaskList
    }

    @Override
    public boolean isExit() {
        return false; // Find command does not terminate the program
    }
}
