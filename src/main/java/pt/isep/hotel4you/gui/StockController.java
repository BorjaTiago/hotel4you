package pt.isep.hotel4you.gui;

import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import pt.isep.hotel4you.domain.StockItem;
import pt.isep.hotel4you.service.HotelFacade;

/** Controller of the screen that creates, lists and edits the stock items. */
public class StockController {

    private final HotelFacade facade;

    @FXML
    private TextField nameField;

    @FXML
    private TextField unitField;

    @FXML
    private Label messageLabel;

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
     * @param facade the facade that creates, lists and edits the items
     */
    public StockController(HotelFacade facade) {
        this.facade = facade;
    }

    @FXML
    private void initialize() {
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
    }

    private void loadItems() {
        try {
            itemTable.getItems().setAll(facade.listItems(HotelApp.currentUser));
        } catch (RuntimeException e) {
            messageLabel.setText(e.getMessage());
        }
    }

    @FXML
    private void onSaveItemClicked() {
        messageLabel.setText("");
        try {
            facade.createItem(nameField.getText(), unitField.getText(), HotelApp.currentUser);
            loadItems();
            clearForm();
            messageLabel.setText("Item registado.");
        } catch (RuntimeException e) {
            messageLabel.setText(e.getMessage());
        }
    }

    @FXML
    private void onEditItemClicked() {
        messageLabel.setText("");
        StockItem selected = itemTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            messageLabel.setText("Escolha um item na tabela.");
            return;
        }
        try {
            StockItem edited = new StockItem(selected.getId(), nameField.getText(),
                    unitField.getText(), selected.getStockQuantity());
            facade.editItem(edited, HotelApp.currentUser);
            loadItems();
            clearForm();
            messageLabel.setText("Item guardado.");
        } catch (RuntimeException e) {
            messageLabel.setText(e.getMessage());
        }
    }

    @FXML
    private void onClearClicked() {
        clearForm();
        messageLabel.setText("");
    }

    private void clearForm() {
        itemTable.getSelectionModel().clearSelection();
        nameField.clear();
        unitField.clear();
    }
}
