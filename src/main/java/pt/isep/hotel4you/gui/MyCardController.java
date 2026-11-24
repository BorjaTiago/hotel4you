package pt.isep.hotel4you.gui;

import java.time.format.DateTimeFormatter;
import java.util.List;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.shape.SVGPath;
import pt.isep.hotel4you.domain.CardAccessRecord;
import pt.isep.hotel4you.domain.CardPlace;
import pt.isep.hotel4you.domain.MagneticCard;
import pt.isep.hotel4you.service.EntryRefusedException;
import pt.isep.hotel4you.service.HotelFacade;

/**
 * Controller of the screen "O meu cartão" (H4Y-109 AC-6, AC-7). It shows one big button per place:
 * green if the card may enter, grey with a lock if not. A click passes the card at the place. An
 * allowed place shows "Entrada permitida" and logs the entry. A locked place shows "Entrada
 * recusada" with the reason and logs nothing (H4Y-135).
 */
public class MyCardController {

    private static final DateTimeFormatter TIME = DateTimeFormatter.ofPattern("HH:mm");

    /** A padlock, drawn so that it shows on every system. */
    private static final String LOCK_SHAPE =
            "M6 10V7a6 6 0 0 1 12 0v3h1a1 1 0 0 1 1 1v10a1 1 0 0 1-1 1H5a1 1 0 0 1-1-1V11a1 1 0 0 1 1-1h1z"
            + "M8 10h8V7a4 4 0 0 0-8 0v3z";

    private final HotelFacade facade;

    @FXML
    private Label cardLabel;

    @FXML
    private Label resultLabel;

    @FXML
    private FlowPane placesPane;

    /**
     * Creates the controller.
     *
     * @param facade the facade for the card places and the entries
     */
    public MyCardController(HotelFacade facade) {
        this.facade = facade;
    }

    @FXML
    private void initialize() {
        MagneticCard card = HotelApp.currentCard;
        cardLabel.setText("Cartão n.º " + card.getNumber());
        try {
            List<CardPlace> places = facade.listCardPlaces(card);
            for (CardPlace place : places) {
                placesPane.getChildren().add(placeButton(card, place));
            }
        } catch (IllegalStateException e) {
            showResult(e.getMessage(), false);
        }
    }

    private Button placeButton(MagneticCard card, CardPlace place) {
        Button button = new Button(place.name());
        button.getStyleClass().add("place-button");
        if (place.allowed()) {
            button.getStyleClass().add("place-allowed");
        } else {
            SVGPath lock = new SVGPath();
            lock.setContent(LOCK_SHAPE);
            lock.getStyleClass().add("place-lock-icon");
            button.setGraphic(lock);
            button.getStyleClass().add("place-locked");
        }
        button.setOnAction(event -> onPlaceClicked(card, place));
        return button;
    }

    private void onPlaceClicked(MagneticCard card, CardPlace place) {
        try {
            CardAccessRecord entry = facade.enterPlace(card, place);
            showResult("Entrada permitida: " + place.name() + " às "
                    + entry.getEntryDateTime().format(TIME) + ".", true);
        } catch (EntryRefusedException e) {
            showResult("Entrada recusada: " + place.name() + ". " + e.getReason(), false);
        } catch (IllegalArgumentException | IllegalStateException e) {
            showResult(e.getMessage(), false);
        }
    }

    private void showResult(String text, boolean allowed) {
        resultLabel.setText(text);
        resultLabel.getStyleClass().removeAll("card-result-allowed", "card-result-refused");
        resultLabel.getStyleClass().add(allowed ? "card-result-allowed" : "card-result-refused");
    }

    @FXML
    private void onLogoutClicked() {
        HotelApp.currentCard = null;
        resultLabel.getScene().setRoot(new ScreenLoader(facade).load("login.fxml"));
    }
}
