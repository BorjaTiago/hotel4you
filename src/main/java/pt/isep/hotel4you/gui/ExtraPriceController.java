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
        extraTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, extra) -> {
            priceField.setText(extra == null || extra.getPrice() == null ? ""
                    : extra.getPrice().toPlainString().replace('.', ','));
            showMessage("", false);
        });
        try {
            loadPrices(null);
        } catch (RuntimeException error) {
            showMessage(error.getMessage(), false);
        }
    }

    private void loadPrices(Long selectedId) {
        extraTable.getItems().setAll(facade.listExtraPrices(HotelApp.currentUser));
        if (selectedId != null) {
            extraTable.getItems().stream().filter(extra -> selectedId.equals(extra.getId())).findFirst()
                    .ifPresent(extra -> extraTable.getSelectionModel().select(extra));
        }
    }

    @FXML
    private void onSavePriceClicked() {
        showMessage("", false);
        ExtraService extra = extraTable.getSelectionModel().getSelectedItem();
        try {
            String message = facade.setExtraPrice(extra, facade.parseExtraPrice(priceField.getText()),
                    HotelApp.currentUser);
            loadPrices(extra.getId());
            showMessage(message, true);
        } catch (RuntimeException error) {
            showMessage(error.getMessage(), false);
        }
    }

    private void showMessage(String text, boolean success) {
        messageLabel.setText(text);
        messageLabel.getStyleClass().remove("message-success");
        if (success) {
            messageLabel.getStyleClass().add("message-success");
        }
    }

    static String priceText(BigDecimal price) {
        return price == null ? "Por definir" : price.toPlainString().replace('.', ',') + " €";
    }
}
