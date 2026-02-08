// src/main/java/cherish/Main.java
package cherish;

import java.io.IOException;

import cherish.ui.MainWindow;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * A GUI for Cherish using FXML.
 */
public class Main extends Application {

    private Cherish cherish = new Cherish("data/cherish.txt");

    @Override
    public void start(Stage stage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/view/MainWindow.fxml"));
            AnchorPane ap = fxmlLoader.load();
            Scene scene = new Scene(ap);
            stage.setTitle("Cherish");
            stage.setScene(scene);
            MainWindow controller = fxmlLoader.<MainWindow>getController();
            controller.setCherish(cherish);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
