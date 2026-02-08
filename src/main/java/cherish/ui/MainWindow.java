// src/main/java/cherish/ui/MainWindow.java
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
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private VBox dialogContainer;
    @FXML
    private TextField userInput;
    @FXML
    private Button sendButton;

    private Cherish cherish;

    private Image userImage = new Image(this.getClass().getResourceAsStream("/images/rabbit.png"));
    private Image cherishImage = new Image(this.getClass().getResourceAsStream("/images/pig.png"));

    @FXML
    public void initialize() {
        scrollPane.vvalueProperty().bind(dialogContainer.heightProperty());
    }

    /** Injects the Cherish instance */
    public void setCherish(Cherish c) {
        cherish = c;
        // Show initial messages (e.g., loading errors, welcome message) upon initialization
        String initialMessages = cherish.getInitialMessages();
        if (initialMessages != null && !initialMessages.isEmpty()) {
            // For initial messages, treat them as coming from Cherish
            String[] lines = initialMessages.split("\n");
            for (String line : lines) {
                if (!line.trim().isEmpty()) {
                    dialogContainer.getChildren().add(DialogBox.getCherishDialog(line.trim(), cherishImage));
                }
            }
        }
    }

    /**
     * Handles user input, displays it, gets Cherish's response, and displays that too.
     */
    @FXML
    private void handleUserInput() {
        String input = userInput.getText();

        // Add the user's input as a dialog box (image on right, text on left)
        dialogContainer.getChildren().add(DialogBox.getUserDialog(input, userImage));

        // Get Cherish's response
        String response = cherish.getResponse(input);

        // Add Cherish's response as a dialog box (image on left, text on right)
        if (response != null && !response.isEmpty()) {
            dialogContainer.getChildren().add(DialogBox.getCherishDialog(response, cherishImage));
        }

        // Optional: Check for exit condition based on response content (e.g., contains "Bye")
        if (response != null && response.toLowerCase().contains("bye.")) {
            userInput.setDisable(true);
            sendButton.setDisable(true);
        }

        userInput.clear();
    }
}
