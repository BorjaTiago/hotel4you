package pt.isep.hotel4you.gui;

import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.util.StringConverter;
import pt.isep.hotel4you.domain.Role;
import pt.isep.hotel4you.domain.User;
import pt.isep.hotel4you.service.HotelFacade;

/** Controller of the screen that creates and lists staff accounts. */
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

    @FXML
    private TableView<User> staffTable;

    @FXML
    private TableColumn<User, String> nameColumn;

    @FXML
    private TableColumn<User, String> usernameColumn;

    @FXML
    private TableColumn<User, String> roleColumn;

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
        nameColumn.setCellValueFactory(
                data -> new SimpleStringProperty(data.getValue().getName()));
        usernameColumn.setCellValueFactory(
                data -> new SimpleStringProperty(data.getValue().getUsername()));
        roleColumn.setCellValueFactory(
                data -> new SimpleStringProperty(roleLabel(data.getValue().getRole())));
        loadStaff();
    }

    private void loadStaff() {
        try {
            staffTable.getItems().setAll(facade.listUsers());
        } catch (IllegalStateException e) {
            messageLabel.setText(e.getMessage());
        }
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
            loadStaff();
        } catch (RuntimeException e) {
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
