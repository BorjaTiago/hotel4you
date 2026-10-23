package pt.isep.hotel4you.gui;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.util.StringConverter;
import pt.isep.hotel4you.domain.Role;
import pt.isep.hotel4you.service.AccessDeniedException;
import pt.isep.hotel4you.service.HotelFacade;
import pt.isep.hotel4you.service.UsernameTakenException;

/** Controller of the screen that creates staff accounts. */
public class StaffController {

    private final HotelFacade facade;

    @FXML
    private TextField nameField;

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private ComboBox<Role> roleBox;

    @FXML
    private Label messageLabel;

    /**
     * Creates the controller.
     *
     * @param facade the facade that creates the accounts
     */
    public StaffController(HotelFacade facade) {
        this.facade = facade;
    }

    @FXML
    private void initialize() {
        roleBox.getItems().setAll(Role.RECEPTIONIST, Role.RESTAURANT, Role.MANAGER);
        roleBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(Role role) {
                return role == null ? "" : roleLabel(role);
            }

            @Override
            public Role fromString(String text) {
                return null;
            }
        });
    }

    @FXML
    private void onCreateStaffClicked() {
        try {
            facade.createStaff(nameField.getText(), usernameField.getText(),
                    passwordField.getText(), roleBox.getValue(), HotelApp.currentUser);
            messageLabel.setText("Conta criada.");
            nameField.clear();
            usernameField.clear();
            roleBox.setValue(null);
        } catch (IllegalArgumentException | IllegalStateException | AccessDeniedException
                | UsernameTakenException e) {
            messageLabel.setText(e.getMessage());
        } finally {
            passwordField.clear();
        }
    }

    private static String roleLabel(Role role) {
        return switch (role) {
            case RECEPTIONIST -> "Rececionista";
            case RESTAURANT -> "Funcionário do restaurante";
            case MANAGER -> "Gestor";
            default -> role.name();
        };
    }
}
