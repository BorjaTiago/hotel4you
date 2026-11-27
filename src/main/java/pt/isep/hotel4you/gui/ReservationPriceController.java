package pt.isep.hotel4you.gui;

import static pt.isep.hotel4you.gui.ReservationExtrasController.money;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.util.StringConverter;
import pt.isep.hotel4you.domain.ReservationPrice;
import pt.isep.hotel4you.domain.ReservationSummary;
import pt.isep.hotel4you.service.AccessDeniedException;
import pt.isep.hotel4you.service.HotelFacade;
import pt.isep.hotel4you.service.ReservationNotFoundException;

/** Shows the price breakdown for a saved reservation. */
public class ReservationPriceController {
    private final HotelFacade facade;

    @FXML private ComboBox<ReservationSummary> reservations;
    @FXML private Label breakdown;
    @FXML private Label total;
    @FXML private Label message;

    /**
     * Creates the reservation price screen.
     * @param facade the reservation operations
     */
    public ReservationPriceController(HotelFacade facade) {
        this.facade = facade;
    }

    @FXML
    private void initialize() {
        reservations.setConverter(new StringConverter<>() {
            @Override
            public String toString(ReservationSummary reservation) {
                if (reservation == null) {
                    return "";
                }
                return "Quarto " + reservation.roomNumber() + " — " + reservation.clientName()
                        + " (" + reservation.startDate() + " a " + reservation.endDate() + ")";
            }

            @Override
            public ReservationSummary fromString(String text) {
                throw new UnsupportedOperationException();
            }
        });
        reservations.valueProperty().addListener((value, before, chosen) -> showPrice());
        try {
            reservations.getItems().setAll(facade.listActiveReservations(HotelApp.currentUser));
            if (reservations.getItems().isEmpty()) {
                message.setText("Não há reservas ativas.");
            }
        } catch (AccessDeniedException | IllegalStateException error) {
            message.setText(error.getMessage());
        }
    }

    @FXML
    private void showPrice() {
        breakdown.setText("");
        total.setText("");
        message.setText("");
        ReservationSummary selected = reservations.getValue();
        if (selected == null) {
            message.setText("Escolha uma reserva.");
            return;
        }
        try {
            ReservationPrice price = facade.quoteReservationPrice(selected.id(), HotelApp.currentUser);
            breakdown.setText("Noites: " + price.nights()
                    + "\nPreço base por noite: " + (price.basePrice() == null ? "por definir" : money(price.basePrice()))
                    + "\nExtras do quarto por noite: " + money(price.extrasPerNight())
                    + "\nServiços pagos por noite: " + money(price.servicesPerNight()));
            if (price.pricePerNight() == null) {
                message.setText(totalText(price));
            } else {
                breakdown.setText(breakdown.getText() + "\nPreço por noite: " + money(price.pricePerNight()));
                total.setText(totalText(price));
            }
        } catch (AccessDeniedException | ReservationNotFoundException | IllegalStateException error) {
            message.setText(error.getMessage());
        }
    }

    static String totalText(ReservationPrice price) {
        return price.total() == null
                ? "Não é possível calcular o total. Falta o preço base do tipo de quarto."
                : "Preço total: " + money(price.total());
    }
}
