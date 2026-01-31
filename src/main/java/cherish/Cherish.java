package cherish;

import cherish.command.Command;
import cherish.model.TaskList;
import cherish.parser.Parser;
import cherish.storage.Storage;
import cherish.ui.Ui;

public class Cherish {
    private Storage storage;
    private TaskList tasks;
    private Ui ui;

    public Cherish(String filePath) {
        ui = new Ui();
        storage = new Storage(filePath);
        try {
            tasks = new TaskList(storage.load());
        } catch (CherishException e) {
            ui.showLoadingError();
            tasks = new TaskList();
        }
    }

    public void run() {
        ui.showWelcome();
        boolean isExit = false;
        while (!isExit) {
            try {
                String fullCommand = ui.readCommand();
                Command c = Parser.parse(fullCommand);
                String response = c.execute(tasks, ui, storage);
                if (response != null) {
                    ui.showMessage(response);
                }
                isExit = c.isExit();
            } catch (CherishException e) {
                ui.showError(e.getMessage());
            }
        }
        ui.showBye();
    }
}
