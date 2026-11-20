package pt.isep.hotel4you.gui;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.util.StringConverter;
import pt.isep.hotel4you.domain.Role;
import pt.isep.hotel4you.domain.User;
import pt.isep.hotel4you.service.AccessDeniedException;
import pt.isep.hotel4you.service.HotelFacade;
import pt.isep.hotel4you.service.StaffNotFoundException;

/** Controller of the screen that creates, lists, finds, edits and removes staff accounts. */
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
    private Label titleLabel;

    @FXML
    private Label editHintLabel;

    @FXML
    private Button createButton;

    @FXML
    private Button saveButton;

    @FXML
    private Button removeButton;

    @FXML
    private TextField searchField;

    @FXML
    private Label searchResultLabel;

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
                return role == null ? "" : role.displayName();
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
                data -> new SimpleStringProperty(data.getValue().getRole().displayName()));
        staffTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldUser, user) -> fillForm(user));
        // Create mode shows the create button and the password; edit mode shows the save button.
        BooleanBinding editing = staffTable.getSelectionModel().selectedItemProperty().isNotNull();
        showWhen(saveButton, editing);
        showWhen(removeButton, editing);
        showWhen(createButton, editing.not());
        showWhen(passwordField, editing.not());
        showWhen(editHintLabel, editing.not());
        titleLabel.textProperty().bind(Bindings.when(editing)
                .then("Editar conta de funcionário").otherwise("Criar conta de funcionário"));
        loadStaff();
    }

    private static void showWhen(Node node, BooleanBinding condition) {
        node.visibleProperty().bind(condition);
        node.managedProperty().bind(condition);
    }

    private void fillForm(User user) {
        // Clear the password. Edit does not change it.
        passwordField.clear();
        if (user != null) {
            nameField.setText(user.getName());
            usernameField.setText(user.getUsername());
            roleBox.setValue(user.getRole());
        }
    }

    private void clearForm() {
        staffTable.getSelectionModel().clearSelection();
        nameField.clear();
        usernameField.clear();
        passwordField.clear();
        roleBox.setValue(null);
        searchResultLabel.getStyleClass().remove("message");
        searchResultLabel.setText("");
    }

    private void loadStaff() {
        try {
            staffTable.getItems().setAll(facade.listUsers(HotelApp.currentUser));
        } catch (AccessDeniedException | IllegalStateException e) {
            messageLabel.setText(e.getMessage());
        }
    }

    @FXML
    private void onCreateStaffClicked() {
        try {
            facade.createStaff(nameField.getText(), usernameField.getText(),
                    passwordField.getText(), roleBox.getValue(), HotelApp.currentUser);
            messageLabel.setText("Conta criada.");
            clearForm();
            loadStaff();
        } catch (RuntimeException e) {
            messageLabel.setText(e.getMessage());
        } finally {
            passwordField.clear();
        }
    }

    @FXML
    private void onSaveStaffChangesClicked() {
        User selected = staffTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            messageLabel.setText("Selecione uma conta na tabela.");
            return;
        }
        try {
            facade.editStaff(selected.getId(), nameField.getText(), usernameField.getText(),
                    roleBox.getValue(), HotelApp.currentUser);
            messageLabel.setText("Conta alterada. O novo tipo aplica-se no próximo login.");
            clearForm();
            loadStaff();
        } catch (RuntimeException e) {
            messageLabel.setText(e.getMessage());
        }
    }

    @FXML
    private void onRemoveStaffClicked() {
        messageLabel.setText("");
        User selected = staffTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            messageLabel.setText("Selecione uma conta na tabela.");
            return;
        }
        Alert question = new Alert(Alert.AlertType.CONFIRMATION,
                "Remover a conta de " + selected.getUsername() + "? Esta ação não pode ser desfeita.",
                ButtonType.YES, ButtonType.NO);
        Button noButton = (Button) question.getDialogPane().lookupButton(ButtonType.NO);
        Button yesButton = (Button) question.getDialogPane().lookupButton(ButtonType.YES);
        yesButton.setDefaultButton(false);
        noButton.setDefaultButton(true);
        noButton.requestFocus();
        if (question.showAndWait().orElse(ButtonType.NO) != ButtonType.YES) {
            return;
        }
        try {
            facade.removeStaff(selected.getId(), HotelApp.currentUser);
            clearForm();
            loadStaff();
            messageLabel.setText("Conta removida.");
        } catch (RuntimeException e) {
            messageLabel.setText(e.getMessage());
        }
    }

    @FXML
    private void onFindUserClicked() {
        searchResultLabel.getStyleClass().remove("message");
        try {
            User user = facade.findUser(searchField.getText(), HotelApp.currentUser);
            searchResultLabel.setText("Nome: " + user.getName() + "\nNome de utilizador: "
                    + user.getUsername() + "\nTipo de conta: " + user.getRole().displayName());
        } catch (AccessDeniedException | StaffNotFoundException | IllegalStateException e) {
            searchResultLabel.getStyleClass().add("message");
            searchResultLabel.setText(e.getMessage());
        }
    }

    @FXML
    private void onClearClicked() {
        messageLabel.setText("");
        clearForm();
    }
}
