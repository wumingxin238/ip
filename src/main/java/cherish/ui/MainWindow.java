// File: src/main/java/cherish/ui/MainWindow.java
package cherish.ui;

import cherish.Cherish;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * Controller for the main GUI window of the Cherish ChatBot application.
 */
public class MainWindow extends AnchorPane {

    // === Constants ===
    private static final String USER_IMAGE_PATH = "/images/rabbit.png";
    private static final String CHERISH_IMAGE_PATH = "/images/pig.png";
    private static final String EXIT_KEYWORD = "bye.";
    private static final String STYLESHEET_PATH = "/view/styles.css";

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

    /**
     * Initializes the controller after its FXML elements have been loaded.
     * Sets up auto-scrolling and loads the custom CSS stylesheet.
     */
    @FXML
    public void initialize() {
        scrollPane.setFitToWidth(true);
        // Binds scroll position to the bottom of the container for auto-scrolling
        scrollPane.vvalueProperty().bind(dialogContainer.heightProperty());

        // Load the custom CSS stylesheet onto the dialogContainer to apply styles to chat bubbles
        String stylesheet = getClass().getResource(STYLESHEET_PATH).toExternalForm();
        dialogContainer.getStylesheets().add(stylesheet);
    }

    /**
     * Injects the Cherish application instance and displays the initial welcome messages.
     *
     * @param c The Cherish application instance.
     */
    public void setCherish(Cherish c) {
        cherish = c;
        showInitialMessages();
    }

    /**
     * Handles the action triggered when the user sends input.
     * Processes the command, displays user and bot responses, checks for exit, and clears input.
     */
    @FXML
    private void handleUserInput() {
        String input = userInput.getText().trim();
        if (input.isEmpty()) {
            return; // Ignore empty inputs
        }

        addUserDialog(input);

        String response = cherish.getResponse(input);
        addCherishDialog(response);

        // Check if the response signals an exit command
        if (cherish.shouldExit()) {
            handleExit();
        }

        userInput.clear();
    }

    // === Private helper methods ===

    /**
     * Displays the initial welcome messages from Cherish.
     */
    private void showInitialMessages() {
        String initialMessages = cherish.getInitialMessages();
        if (initialMessages != null && !initialMessages.isBlank()) {
            java.util.Arrays.stream(initialMessages.split("\n"))
                    .map(String::trim)
                    .filter(line -> !line.isEmpty())
                    .forEach(line -> dialogContainer.getChildren()
                            .add(DialogBox.getCherishDialog(line, cherishImage)));
        }
    }

    /**
     * Adds a dialog box representing the user's input to the display.
     */
    private void addUserDialog(String input) {
        dialogContainer.getChildren().add(DialogBox.getUserDialog(input, userImage));
    }

    /**
     * Adds a dialog box representing Cherish's response to the display.
     */
    private void addCherishDialog(String response) {
        if (response != null && !response.isEmpty()) {
            dialogContainer.getChildren().add(DialogBox.getCherishDialog(response, cherishImage));
        }
    }

    /**
     * Handles the application shutdown process.
     * Disables UI elements and schedules the window to close after a delay.
     */
    private void handleExit() {
        userInput.setDisable(true);
        sendButton.setDisable(true);

        // Schedule the window to close after a 2-second delay
        PauseTransition delay = new PauseTransition(Duration.seconds(2));
        delay.setOnFinished(event -> {
            Stage stage = (Stage) userInput.getScene().getWindow();
            stage.close();
        });
        delay.play();
    }

}
