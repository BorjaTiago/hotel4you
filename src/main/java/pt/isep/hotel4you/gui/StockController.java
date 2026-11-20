package pt.isep.hotel4you.gui;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.util.StringConverter;
import pt.isep.hotel4you.domain.MovementType;
import pt.isep.hotel4you.domain.StockItem;
import pt.isep.hotel4you.domain.StockMovement;
import pt.isep.hotel4you.service.HotelFacade;

/** Controller of the screen that creates, lists and edits the stock items and records stock entries. */
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
    private Label entryItemLabel;

    @FXML
    private Label entryQuantityLabel;

    @FXML
    private Label entryDateLabel;

    @FXML
    private ComboBox<StockItem> entryItemBox;

    @FXML
    private TextField entryQuantityField;

    @FXML
    private TextField entryDateField;

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
     * @param facade the facade that creates, lists and edits the items and records the entries
     */
    public StockController(HotelFacade facade) {
        this.facade = facade;
    }

    @FXML
    private void initialize() {
        nameLabel.setLabelFor(nameField);
        unitLabel.setLabelFor(unitField);
        entryItemLabel.setLabelFor(entryItemBox);
        entryQuantityLabel.setLabelFor(entryQuantityField);
        entryDateLabel.setLabelFor(entryDateField);
        entryItemBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(StockItem item) {
                return item == null ? "" : item.getName() + " (" + item.getUnit() + ")";
            }

            @Override
            public StockItem fromString(String text) {
                return null;
            }
        });
        entryDateField.setText(dateText(LocalDate.now()));
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

    private void fillForm(StockItem item) {
        if (item == null) {
            return;
        }
        nameField.setText(item.getName());
        unitField.setText(item.getUnit());
        entryItemBox.setValue(item);
    }

    private void loadItems() {
        entryItemBox.setValue(null);
        try {
            itemTable.getItems().setAll(facade.listItems(HotelApp.currentUser));
            entryItemBox.setItems(FXCollections.observableArrayList(itemTable.getItems()));
        } catch (RuntimeException e) {
            entryItemBox.setItems(FXCollections.observableArrayList());
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
        entryDateField.setText(dateText(LocalDate.now()));
        try {
            int quantity = facade.parseQuantity(entryQuantityField.getText());
            StockMovement movement = facade.registerMovement(entryItemBox.getValue(),
                    MovementType.ENTRY, quantity, HotelApp.currentUser);
            entryItemBox.setValue(null);
            entryQuantityField.clear();
            entryDateField.setText(dateText(movement.getDateTime().toLocalDate()));
            showMessage(entryMessage(movement), true);
            loadItems();
        } catch (RuntimeException e) {
            showMessage(e.getMessage(), false);
        }
    }

    /**
     * Gives the success text of a stock entry, with the new stock of the item.
     *
     * @param movement the saved entry, with the item and its new stock
     * @return the text for the screen
     */
    static String entryMessage(StockMovement movement) {
        StockItem item = movement.getItem();
        return "Entrada registada: " + movement.getQuantity() + " " + item.getUnit() + " de "
                + item.getName() + ". Stock atual: " + item.getStockQuantity() + ".";
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

    /** Clears the item form and the stock entry form. */
    @FXML
    private void onClearClicked() {
        clearForm();
        entryItemBox.setValue(null);
        entryQuantityField.clear();
        entryDateField.setText(dateText(LocalDate.now()));
        showMessage("", false);
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
