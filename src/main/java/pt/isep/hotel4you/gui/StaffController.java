
package pt.isep.hotel4you.gui;

import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import pt.isep.hotel4you.domain.Role;
import pt.isep.hotel4you.domain.User;
import pt.isep.hotel4you.service.HotelFacade;

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

    public StaffController(HotelFacade facade) {
        this.facade = facade;
    }

    @FXML
    private void initialize() {
        roleBox.getItems().setAll(
                Role.RECEPTIONIST,
                Role.RESTAURANT,
                Role.MANAGER
        );

        roleBox.setConverter(new javafx.util.StringConverter<>() {
            @Override
            public String toString(Role role) {
                return role == null ? "" : translateRole(role);
            }

            @Override
            public Role fromString(String string) {
                return null;
            }
        });

        nameColumn.setCellValueFactory(
                data -> new SimpleStringProperty(data.getValue().getName()));

        usernameColumn.setCellValueFactory(
                data -> new SimpleStringProperty(data.getValue().getUsername()));

        roleColumn.setCellValueFactory(
                data -> new SimpleStringProperty(
                        translateRole(data.getValue().getRole())));

        loadStaff();
    }

    @FXML
    private void onCreateStaffClicked() {
        try {
            facade.createStaff(
                    nameField.getText(),
                    usernameField.getText(),
                    passwordField.getText(),
                    roleBox.getValue(),
                    HotelApp.currentUser
            );

            messageLabel.setText("Funcionário criado.");

            nameField.clear();
            usernameField.clear();
            passwordField.clear();
            roleBox.setValue(null);

            loadStaff();

        } catch (RuntimeException e) {
            messageLabel.setText(e.getMessage());
        }
    }

    private void loadStaff() {
        staffTable.getItems().setAll(facade.findAllUsers().stream()
                .filter(user -> user.getRole() == Role.RECEPTIONIST
                        || user.getRole() == Role.RESTAURANT
                        || user.getRole() == Role.MANAGER)
                .toList());
    }

    private String translateRole(Role role) {
        return switch (role) {
            case RECEPTIONIST -> "Rececionista";
            case RESTAURANT -> "Funcionário do Restaurante";
            case MANAGER -> "Gestor";
            default -> role.name();
        };
    }
}
