package pt.isep.hotel4you.gui;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;
import pt.isep.hotel4you.domain.ExitReason;
import pt.isep.hotel4you.domain.MovementType;
import pt.isep.hotel4you.domain.StockItem;
import pt.isep.hotel4you.domain.StockMovement;
import pt.isep.hotel4you.service.HotelFacade;

/**
 * Controller of the screen that creates, lists and edits the stock items and records stock
 * entries, uses and exits.
 */
public class StockController {

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private final HotelFacade facade;

    @FXML
    private Label nameLabel;

    @FXML
    private Label unitLabel;

    @FXML
    private TextField nameField;

    @FXML
    private TextField unitField;

    @FXML
    private Label messageLabel;

    @FXML
    private Label movementItemLabel;

    @FXML
    private Label movementQuantityLabel;

    @FXML
    private Label movementDateLabel;

    @FXML
    private Label movementTypeLabel;

    @FXML
    private Label exitReasonLabel;

    @FXML
    private ComboBox<MovementType> movementTypeBox;

    @FXML
    private ComboBox<ExitReason> exitReasonBox;

    @FXML
    private VBox exitReasonPane;

    @FXML
    private ComboBox<StockItem> movementItemBox;

    @FXML
    private TextField movementQuantityField;

    @FXML
    private TextField movementDateField;

    @FXML
    private TableView<StockItem> itemTable;

    @FXML
    private TableColumn<StockItem, String> nameColumn;

    @FXML
    private TableColumn<StockItem, String> unitColumn;

    @FXML
    private TableColumn<StockItem, String> quantityColumn;

    /**
     * Creates the controller.
     *
     * @param facade the facade that creates, lists and edits the items and records the movements
     */
    public StockController(HotelFacade facade) {
        this.facade = facade;
    }

    @FXML
    private void initialize() {
        nameLabel.setLabelFor(nameField);
        unitLabel.setLabelFor(unitField);
        movementItemLabel.setLabelFor(movementItemBox);
        movementQuantityLabel.setLabelFor(movementQuantityField);
        movementDateLabel.setLabelFor(movementDateField);
        movementTypeLabel.setLabelFor(movementTypeBox);
        exitReasonLabel.setLabelFor(exitReasonBox);
        movementTypeBox.setItems(FXCollections.observableArrayList(MovementType.values()));
        movementTypeBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(MovementType type) {
                return type == null ? "" : typeText(type);
            }

            @Override
            public MovementType fromString(String text) {
                return null;
            }
        });
        exitReasonBox.setItems(FXCollections.observableArrayList(ExitReason.values()));
        exitReasonBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(ExitReason reason) {
                return reason == null ? "" : reasonText(reason);
            }

            @Override
            public ExitReason fromString(String text) {
                return null;
            }
        });
        movementTypeBox.valueProperty().addListener(
                (observable, oldType, newType) -> updateReasonBox(newType));
        movementTypeBox.setValue(MovementType.ENTRY);
        movementItemBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(StockItem item) {
                return item == null ? "" : item.getName() + " (" + item.getUnit() + ")";
            }

            @Override
            public StockItem fromString(String text) {
                return null;
            }
        });
        movementDateField.setText(dateText(LocalDate.now()));
        nameColumn.setCellValueFactory(
                data -> new SimpleStringProperty(data.getValue().getName()));
        unitColumn.setCellValueFactory(
                data -> new SimpleStringProperty(data.getValue().getUnit()));
        quantityColumn.setCellValueFactory(
                data -> new SimpleStringProperty(String.valueOf(data.getValue().getStockQuantity())));
        itemTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldItem, newItem) -> fillForm(newItem));
        loadItems();
    }

    /** Only an exit has a reason, so the reason box shows only for an exit. */
    private void updateReasonBox(MovementType type) {
        boolean exit = type == MovementType.EXIT;
        exitReasonPane.setVisible(exit);
        exitReasonPane.setManaged(exit);
        if (!exit) {
            exitReasonBox.setValue(null);
        }
    }

    private void fillForm(StockItem item) {
        if (item == null) {
            return;
        }
        nameField.setText(item.getName());
        unitField.setText(item.getUnit());
        movementItemBox.setValue(item);
    }

    private void loadItems() {
        movementItemBox.setValue(null);
        try {
            itemTable.getItems().setAll(facade.listItems(HotelApp.currentUser));
            movementItemBox.setItems(FXCollections.observableArrayList(itemTable.getItems()));
        } catch (RuntimeException e) {
            movementItemBox.setItems(FXCollections.observableArrayList());
            showMessage(e.getMessage(), false);
        }
    }

    @FXML
    private void onSaveItemClicked() {
        showMessage("", false);
        try {
            facade.createItem(nameField.getText(), unitField.getText(), HotelApp.currentUser);
            loadItems();
            clearForm();
            showMessage("Item registado.", true);
        } catch (RuntimeException e) {
            showMessage(e.getMessage(), false);
        }
    }

    @FXML
    private void onEditItemClicked() {
        showMessage("", false);
        StockItem selected = itemTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showMessage("Escolha um item na tabela.", false);
            return;
        }
        try {
            StockItem edited = new StockItem(selected.getId(), nameField.getText(),
                    unitField.getText(), selected.getStockQuantity());
            facade.editItem(edited, HotelApp.currentUser);
            loadItems();
            clearForm();
            showMessage("Item guardado.", true);
        } catch (RuntimeException e) {
            showMessage(e.getMessage(), false);
        }
    }

    @FXML
    private void onRegisterMovementClicked() {
        showMessage("", false);
        movementDateField.setText(dateText(LocalDate.now()));
        try {
            int quantity = facade.parseQuantity(movementQuantityField.getText());
            StockMovement movement = facade.registerMovement(movementItemBox.getValue(),
                    movementTypeBox.getValue(), quantity, exitReasonBox.getValue(),
                    HotelApp.currentUser);
            clearMovementForm();
            movementDateField.setText(dateText(movement.getDateTime().toLocalDate()));
            showMessage(movementMessage(movement), true);
            loadItems();
        } catch (RuntimeException e) {
            showMessage(e.getMessage(), false);
        }
    }

    /**
     * Gives the success text of a stock movement, with the new stock of the item.
     * An exit also shows its reason.
     *
     * @param movement the saved movement, with the item and its new stock
     * @return the text for the screen
     */
    static String movementMessage(StockMovement movement) {
        StockItem item = movement.getItem();
        String start = switch (movement.getType()) {
            case ENTRY -> "Entrada registada";
            case USE -> "Uso registado";
            case EXIT -> "Saída registada ("
                    + reasonText(movement.getExitReason()).toLowerCase(Locale.ROOT) + ")";
        };
        return start + ": " + movement.getQuantity() + " " + item.getUnit() + " de "
                + item.getName() + ". Stock atual: " + item.getStockQuantity() + ".";
    }

    /**
     * Gives the name of a movement type for the screen.
     *
     * @param type the movement type
     * @return the name, for example "Saída"
     */
    static String typeText(MovementType type) {
        return switch (type) {
            case ENTRY -> "Entrada";
            case USE -> "Uso";
            case EXIT -> "Saída";
        };
    }

    /**
     * Gives the name of an exit reason for the screen.
     *
     * @param reason the exit reason
     * @return the name, for example "Devolução"
     */
    static String reasonText(ExitReason reason) {
        return switch (reason) {
            case DISCARD -> "Descarte";
            case RETURN -> "Devolução";
            case LOSS -> "Perda";
        };
    }

    /**
     * Gives a date as day, month and year, for example 16/11/2026.
     *
     * @param date the date
     * @return the text for the screen
     */
    static String dateText(LocalDate date) {
        return date.format(DATE_FORMAT);
    }

    /** Clears the item form and the stock movement form. */
    @FXML
    private void onClearClicked() {
        clearForm();
        clearMovementForm();
        movementDateField.setText(dateText(LocalDate.now()));
        showMessage("", false);
    }

    /** Clears the movement form. The type goes back to entry, so the reason box hides. */
    private void clearMovementForm() {
        movementItemBox.setValue(null);
        movementTypeBox.setValue(MovementType.ENTRY);
        exitReasonBox.setValue(null);
        movementQuantityField.clear();
    }

    private void showMessage(String message, boolean success) {
        messageLabel.setText(message);
        messageLabel.getStyleClass().remove("message-success");
        if (success) {
            messageLabel.getStyleClass().add("message-success");
        }
    }

    private void clearForm() {
        itemTable.getSelectionModel().clearSelection();
        nameField.clear();
        unitField.clear();
    }
}
