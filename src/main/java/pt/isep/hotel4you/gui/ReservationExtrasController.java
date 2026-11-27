package pt.isep.hotel4you.gui;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TitledPane;
import pt.isep.hotel4you.domain.ExtraService;
import pt.isep.hotel4you.domain.ReservationExtra;
import pt.isep.hotel4you.domain.ReservationStatus;
import pt.isep.hotel4you.domain.ReservationSummary;
import pt.isep.hotel4you.service.AccessDeniedException;
import pt.isep.hotel4you.service.HotelFacade;
import pt.isep.hotel4you.service.ReservationNotFoundException;

/** Shows and edits extras for the selected reservation. */
public class ReservationExtrasController {
    private final HotelFacade facade;
    private ReservationSummary reservation;

    @FXML private TitledPane details;
    @FXML private ComboBox<ExtraService> catalogue;
    @FXML private ListView<ReservationExtra> included;
    @FXML private Button addButton;
    @FXML private Button removeButton;
    @FXML private Label priceLabel;
    @FXML private Label extraMessage;

    /**
     * Creates the editor for the reservation screen.
     * @param facade the reservation operations
     */
    public ReservationExtrasController(HotelFacade facade) {
        this.facade = facade;
    }

    @FXML
    private void initialize() {
        catalogue.setCellFactory(list -> catalogueCell());
        catalogue.setButtonCell(catalogueCell());
        included.setCellFactory(list -> new ListCell<>() {
            @Override
            protected void updateItem(ReservationExtra row, boolean empty) {
                super.updateItem(row, empty);
                setText(empty || row == null ? null : row.name() + " — " + money(row.unitPrice()));
            }
        });
        details.expandedProperty().addListener((value, before, expanded) -> {
            if (expanded) {
                refreshContents();
            }
        });
        catalogue.valueProperty().addListener((value, before, selected) -> updateButtons());
        included.getSelectionModel().selectedItemProperty()
                .addListener((value, before, selected) -> updateButtons());
    }

    void selectReservation(ReservationSummary selected) {
        reservation = selected;
        details.setDisable(selected == null);
        if (selected == null) {
            details.setExpanded(false);
        }
        if (details.isExpanded()) {
            refreshContents();
        }
    }

    static boolean canEdit(ReservationSummary selected) {
        return selected != null && selected.status() == ReservationStatus.RESERVED;
    }

    @FXML
    private void onAddClicked() {
        ExtraService selected = catalogue.getValue();
        if (canEdit(reservation) && selected != null) {
            applyChange(() -> facade.addReservationExtra(
                    reservation.id(), selected.getId(), HotelApp.currentUser));
        }
    }

    @FXML
    private void onRemoveClicked() {
        ReservationExtra selected = included.getSelectionModel().getSelectedItem();
        if (canEdit(reservation) && selected != null) {
            applyChange(() -> facade.removeReservationExtra(
                    reservation.id(), selected.extraPriceId(), HotelApp.currentUser));
        }
    }

    private void refreshContents() {
        catalogue.getItems().clear();
        included.getItems().clear();
        extraMessage.setText("");
        priceLabel.setText("");
        if (reservation == null) {
            return;
        }
        try {
            if (canEdit(reservation)) {
                catalogue.getItems().setAll(facade.listReservationExtraOptions( HotelApp.currentUser));
            }
            included.getItems().setAll(facade.listReservationExtras(reservation.id(),
                    HotelApp.currentUser));
            BigDecimal charge = included.getItems().stream().map(ReservationExtra::unitPrice)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            priceLabel.setText("Extras por noite: " + money(charge));
            BigDecimal nightly = facade.quoteReservationPrice(reservation.id(), HotelApp.currentUser).pricePerNight();
            priceLabel.setText(priceLabel.getText() + "\nPreço da reserva por noite: "
                    + (nightly == null ? "falta o preço base" : money(nightly)));
            if (!canEdit(reservation)) {
                extraMessage.setText("Só pode alterar extras antes do check-in.");
            }
        } catch (AccessDeniedException | ReservationNotFoundException | IllegalStateException error) {
            extraMessage.setText(error.getMessage());
        }
        updateButtons();
    }

    private void applyChange(Runnable change) {
        try {
            change.run();
            refreshContents();
        } catch (AccessDeniedException | ReservationNotFoundException | IllegalArgumentException
                | IllegalStateException error) {
            extraMessage.setText(error.getMessage());
        }
    }

    private void updateButtons() {
        catalogue.setDisable(!canEdit(reservation));
        addButton.setDisable(!canEdit(reservation) || catalogue.getValue() == null);
        removeButton.setDisable(!canEdit(reservation) || included.getSelectionModel().getSelectedItem() == null);
    }

    private static ListCell<ExtraService> catalogueCell() {
        return new ListCell<>() {
            @Override
            protected void updateItem(ExtraService entry, boolean empty) {
                super.updateItem(entry, empty);
                setText(empty || entry == null ? null : entry.getName() + " — "
                        + (entry.getPrice() == null ? "sem preço" : money(entry.getPrice())));
            }
        };
    }

    static String money(BigDecimal price) {
        return NumberFormat.getCurrencyInstance(Locale.forLanguageTag("pt-PT")).format(price);
    }
}
