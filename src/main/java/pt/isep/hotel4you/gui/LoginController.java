package pt.isep.hotel4you.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import pt.isep.hotel4you.domain.User;
import pt.isep.hotel4you.service.HotelFacade;

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
            User user = facade.login(usernameField.getText(), passwordField.getText());
            // The real main window comes with H4Y-24.
            messageLabel.setStyle("-fx-text-fill: #1e8449;");
            messageLabel.setText("Welcome, " + user.getName() + " (" + user.getRole() + ")");
        } catch (RuntimeException e) {
            messageLabel.setStyle("");
            messageLabel.setText(e.getMessage());
        }
    }
}
