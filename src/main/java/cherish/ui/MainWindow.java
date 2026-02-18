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
 * Controller for the main GUI.
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

    @FXML
    public void initialize() {
        scrollPane.setFitToWidth(true);
        scrollPane.vvalueProperty().bind(dialogContainer.heightProperty());

        // Load the custom CSS stylesheet onto the dialogContainer instead of the main window
        String stylesheet = getClass().getResource(STYLESHEET_PATH).toExternalForm();
        System.out.println("Loading stylesheet onto dialogContainer: " + stylesheet);
        dialogContainer.getStylesheets().add(stylesheet);
        // Optional: Add a CSS class to the container itself if defined in styles.css
        // dialogContainer.getStyleClass().add("dialog-container");
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

        if (cherish.shouldExit()) {
            handleExit();
        }

        userInput.clear();
    }

    // === Private helper methods ===

    /** Display initial messages from Cherish upon startup. */
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

    /** Helper Method */
    private void handleExit() {
        userInput.setDisable(true);
        sendButton.setDisable(true);

        PauseTransition delay = new PauseTransition(Duration.seconds(2));
        delay.setOnFinished(event -> {
            Stage stage = (Stage) userInput.getScene().getWindow();
            stage.close();
        });
        delay.play();
    }

}
