package pt.isep.hotel4you.gui;

import java.math.BigDecimal;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import pt.isep.hotel4you.domain.ExtraService;
import pt.isep.hotel4you.service.HotelFacade;

/** Shows the extra catalogue and sends price changes to the service through the facade. */
public class ExtraPriceController {
    private final HotelFacade facade;
    @FXML private Label extraLabel;
    @FXML private Label priceLabel;
    @FXML private TableView<ExtraService> extraTable;
    @FXML private TableColumn<ExtraService, String> nameColumn;
    @FXML private TableColumn<ExtraService, String> priceColumn;
    @FXML private TextField priceField;
    @FXML private Button saveButton;
    @FXML private Label messageLabel;

    /**
     * Creates the controller.
     * @param facade the facade that reads and sets extra prices
     */
    public ExtraPriceController(HotelFacade facade) {
        this.facade = facade;
    }

    @FXML
    private void initialize() {
        extraLabel.setLabelFor(extraTable);
        priceLabel.setLabelFor(priceField);
        nameColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getName()));
        priceColumn.setCellValueFactory(data -> new SimpleStringProperty(priceText(data.getValue().getPrice())));
        saveButton.disableProperty().bind(extraTable.getSelectionModel().selectedItemProperty().isNull());
        loadPrices();
    }

    private boolean loadPrices() {
        try {
            extraTable.getItems().setAll(facade.listExtraPrices(HotelApp.currentUser));
            return true;
        } catch (RuntimeException error) {
            showMessage(error.getMessage(), false);
            return false;
        }
    }

    @FXML
    private void onSavePriceClicked() {
        showMessage("", false);
        ExtraService extra = extraTable.getSelectionModel().getSelectedItem();
        try {
            facade.setExtraPrice(extra, facade.parsePrice(priceField.getText()),
                    HotelApp.currentUser);
        } catch (RuntimeException error) {
            showMessage(error.getMessage(), false);
            return;
        }
        priceField.clear();
        if (loadPrices()) {
            showMessage("Preço extra guardado.", true);
        } else {
            showMessage("Preço extra guardado, mas não foi possível atualizar a lista.", false);
        }
    }

    private void showMessage(String text, boolean success) {
        messageLabel.setText(text);
        messageLabel.getStyleClass().remove("message-success");
        if (success) {
            messageLabel.getStyleClass().add("message-success");
        }
    }

    /** Formats a price for the table; the column header states the currency. */
    static String priceText(BigDecimal price) {
        return price == null ? "Por definir" : price.toPlainString().replace('.', ',');
    }
}
