package cherish.ui;

import cherish.Cherish;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

/**
 * Controller for the main GUI.
 */
public class MainWindow extends AnchorPane {

    // === Constants ===
    private static final String USER_IMAGE_PATH = "/images/rabbit.png";
    private static final String CHERISH_IMAGE_PATH = "/images/pig.png";
    private static final String EXIT_KEYWORD = "bye.";

    // === FXML-injected fields ===
    @FXML
    private ScrollPane scrollPane;

    @FXML
    private VBox dialogContainer;

    @FXML
    private TextField userInput;

    @FXML
    private Button sendButton;

    // === Instance fields ===
    private Cherish cherish;
    private Image userImage = new Image(this.getClass().getResourceAsStream(USER_IMAGE_PATH));
    private Image cherishImage = new Image(this.getClass().getResourceAsStream(CHERISH_IMAGE_PATH));

    @FXML
    public void initialize() {
        scrollPane.vvalueProperty().bind(dialogContainer.heightProperty());
    }

    /**
     * Injects the Cherish instance and displays initial messages.
     *
     * @param c Cherish application instance
     */
    public void setCherish(Cherish c) {
        cherish = c;
        showInitialMessages();
    }

    /**
     * Handles user input, displays it, gets Cherish's response, and displays that too.
     */
    @FXML
    private void handleUserInput() {
        String input = userInput.getText().trim();
        if (input.isEmpty()) {
            return;
        }

        addUserDialog(input);

        String response = cherish.getResponse(input);
        addCherishDialog(response);

        checkExitCondition(response);

        userInput.clear();
    }

    // === Private helper methods ===

    /** Display initial messages from Cherish upon startup. */
    private void showInitialMessages() {
        String initialMessages = cherish.getInitialMessages();
        if (initialMessages != null && !initialMessages.isBlank()) {
            // Split lines, trim, filter out empty, and add to dialogContainer
            java.util.Arrays.stream(initialMessages.split("\n"))
                    .map(String::trim)
                    .filter(line -> !line.isEmpty())
                    .forEach(line -> dialogContainer.getChildren()
                            .add(DialogBox.getCherishDialog(line, cherishImage)));
        }
    }

    /** Adds a dialog box representing the user's input. */
    private void addUserDialog(String input) {
        dialogContainer.getChildren().add(DialogBox.getUserDialog(input, userImage));
    }

    /** Adds a dialog box representing Cherish's response. */
    private void addCherishDialog(String response) {
        if (response != null && !response.isEmpty()) {
            dialogContainer.getChildren().add(DialogBox.getCherishDialog(response, cherishImage));
        }
    }

    /** Checks if the response indicates exit, and disables input if so. */
    private void checkExitCondition(String response) {
        if (response != null && response.toLowerCase().contains(EXIT_KEYWORD)) {
            userInput.setDisable(true);
            sendButton.setDisable(true);
        }
    }
}
