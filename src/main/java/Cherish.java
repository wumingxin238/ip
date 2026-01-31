import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

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

    public static void main(String[] args) {
        new Cherish("data/cherish.txt").run();
    }
}