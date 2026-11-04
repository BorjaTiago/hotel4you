package pt.isep.hotel4you.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import pt.isep.hotel4you.service.HotelFacade;

/** Controller of the screen where the receptionist registers a client. */
public class ClientController {

    private static final String FIELD_ERROR = "field-error";

    private final HotelFacade facade;

    @FXML
    private Label nameLabel;

    @FXML
    private Label documentLabel;

    @FXML
    private TextField nameField;

    @FXML
    private TextField documentField;

    @FXML
    private Label messageLabel;

    /**
     * Creates the controller.
     *
     * @param facade the facade that registers the clients
     */
    public ClientController(HotelFacade facade) {
        this.facade = facade;
    }

    @FXML
    private void initialize() {
        nameLabel.setLabelFor(nameField);
        documentLabel.setLabelFor(documentField);
    }

    @FXML
    private void onRegisterClientClicked() {
        clearErrors();
        try {
            facade.registerClient(nameField.getText(), documentField.getText(), HotelApp.currentUser);
            nameField.clear();
            documentField.clear();
            showMessage("Cliente registado.", true);
        } catch (IllegalArgumentException e) {
            // The service names the field in its message.
            markIf(nameField, e.getMessage().contains("nome"));
            markIf(documentField, e.getMessage().contains("documento"));
            showMessage(e.getMessage(), false);
        } catch (RuntimeException e) {
            showMessage(e.getMessage(), false);
        }
    }

    private void markIf(TextField field, boolean hasError) {
        if (hasError) {
            field.getStyleClass().add(FIELD_ERROR);
        }
    }

    private void clearErrors() {
        nameField.getStyleClass().remove(FIELD_ERROR);
        documentField.getStyleClass().remove(FIELD_ERROR);
    }

    private void showMessage(String message, boolean success) {
        messageLabel.setText(message);
        messageLabel.getStyleClass().remove("message-success");
        if (success) {
            messageLabel.getStyleClass().add("message-success");
        }
    }
}
