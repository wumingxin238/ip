// File: src/main/java/cherish/ui/DialogBox.java
package cherish.ui;

import java.io.IOException;
import java.util.Collections;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;

/**
 * Represents a dialog box consisting of an ImageView to represent the speaker's face
 * and a label containing text from the speaker.
 */
public class DialogBox extends HBox {
    // Define CSS class names for styling
    private static final String CHERISH_DIALOG_CSS_CLASS = "cherish-dialog";
    private static final String USER_DIALOG_CSS_CLASS = "user-dialog";
    @FXML
    private Label dialog;
    @FXML
    private ImageView displayPicture;

    private DialogBox(String text, Image img, String cssClass) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(
                    DialogBox.class.getResource("/view/DialogBox.fxml"));
            fxmlLoader.setController(this);
            fxmlLoader.setRoot(this);
            fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        dialog.setText(text);
        displayPicture.setImage(img);

        this.setMaxWidth(Double.MAX_VALUE);

        this.getStyleClass().add(cssClass);

        dialog.maxWidthProperty().bind(
                this.widthProperty().multiply(0.7)
        );
    }

    /**
     * Flips the dialog box such that the ImageView is on the left and text on the right.
     */
    private void flip() {
        ObservableList<Node> tmp = FXCollections.observableArrayList(this.getChildren());
        Collections.reverse(tmp);
        getChildren().setAll(tmp);
        setAlignment(Pos.TOP_LEFT);
    }

    /**
     * Used ChatGPT to help creating the method,
     * for the part creating spacer
     */
    public static DialogBox getUserDialog(String text, Image img) {
        var db = new DialogBox(text, img, USER_DIALOG_CSS_CLASS);
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        db.getChildren().add(0, spacer);

        return db;
    }

    public static DialogBox getCherishDialog(String text, Image img) {
        var db = new DialogBox(text, img, CHERISH_DIALOG_CSS_CLASS);
        db.flip();
        db.setAlignment(Pos.TOP_LEFT);

        return db;
    }
}
