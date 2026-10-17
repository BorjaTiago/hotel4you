package pt.isep.hotel4you.gui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import pt.isep.hotel4you.domain.User;
import pt.isep.hotel4you.service.HotelFacade;

/** The JavaFX application. It opens the login screen. */
public class HotelApp extends Application {

    /** The facade for all screens. Set by Main before the launch. */
    public static HotelFacade facade;

    /** The start error to show in a popup, or null. Set by Main before the launch. */
    public static String startError;

    /** The user who is logged in, or null if nobody is. Set by the login screen. */
    public static User currentUser;

    /**
     * Shows the login screen, or a popup if the start failed.
     *
     * @param stage the main window
     * @throws IOException if the FXML file cannot be loaded
     */
    @Override
    public void start(Stage stage) {
        if (startError != null) {
            new Alert(Alert.AlertType.ERROR, startError).showAndWait();
            Platform.exit();
            return;
        }
        Parent root = new ScreenLoader(facade).load("login.fxml");
        stage.setTitle("Hotel4You");
        stage.setResizable(false);
        stage.setScene(new Scene(root));
        stage.show();
    }
}
