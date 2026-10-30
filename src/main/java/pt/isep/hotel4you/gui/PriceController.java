package pt.isep.hotel4you.gui;

import java.math.BigDecimal;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import pt.isep.hotel4you.domain.RoomType;
import pt.isep.hotel4you.service.HotelFacade;

/** Controller of the screen that shows and sets the base price of the room types. */
public class PriceController {

    private final HotelFacade facade;

    @FXML
    private TableView<RoomType> typeTable;

    @FXML
    private TableColumn<RoomType, String> typeColumn;

    @FXML
    private TableColumn<RoomType, String> capacityColumn;

    @FXML
    private TableColumn<RoomType, String> priceColumn;

    @FXML
    private TextField priceField;

    @FXML
    private Label messageLabel;

    /**
     * Creates the controller.
     *
     * @param facade the facade that lists and sets the prices
     */
    public PriceController(HotelFacade facade) {
        this.facade = facade;
    }

    @FXML
    private void initialize() {
        typeColumn.setCellValueFactory(data -> new SimpleStringProperty(typeText(data.getValue())));
        capacityColumn.setCellValueFactory(
                data -> new SimpleStringProperty(String.valueOf(data.getValue().getCapacity())));
        priceColumn.setCellValueFactory(
                data -> new SimpleStringProperty(priceText(data.getValue().getBasePrice())));
        loadPrices();
    }

    private void loadPrices() {
        try {
            typeTable.getItems().setAll(facade.listBasePrices(HotelApp.currentUser));
        } catch (RuntimeException e) {
            messageLabel.setText(e.getMessage());
        }
    }

    @FXML
    private void onSavePriceClicked() {
        messageLabel.setText("");
        RoomType type = typeTable.getSelectionModel().getSelectedItem();
        if (type == null) {
            messageLabel.setText("Escolha um tipo de quarto na tabela.");
            return;
        }
        try {
            facade.setBasePrice(type, facade.parsePrice(priceField.getText()), HotelApp.currentUser);
            loadPrices();
            priceField.clear();
            messageLabel.setText("Preço guardado.");
        } catch (RuntimeException e) {
            messageLabel.setText(e.getMessage());
        }
    }

    private static String typeText(RoomType type) {
        String name = type.getName();
        return name.substring(0, 1).toUpperCase() + name.substring(1);
    }

    private static String priceText(BigDecimal price) {
        return price == null ? "—" : price.toPlainString().replace('.', ',');
    }
}
