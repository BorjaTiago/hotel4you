package pt.isep.hotel4you.gui;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import pt.isep.hotel4you.service.HotelFacade;
import pt.isep.hotel4you.service.LoginFailedException;

/** Controller of the login screen. */
public class LoginController {

    private final HotelFacade facade;

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label messageLabel;

    /**
     * Creates the controller.
     *
     * @param facade the facade for the login
     */
    public LoginController(HotelFacade facade) {
        this.facade = facade;
    }

    @FXML
    private void onLoginClicked() {
        try {
            HotelApp.currentUser = facade.login(usernameField.getText(), passwordField.getText());
            Parent root = new ScreenLoader(facade).load("main.fxml");
            Stage stage = (Stage) messageLabel.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.sizeToScene();
            stage.centerOnScreen();
        } catch (IllegalArgumentException | LoginFailedException | IllegalStateException e) {
            messageLabel.setText(e.getMessage());
            passwordField.clear();
        }
    }
}
