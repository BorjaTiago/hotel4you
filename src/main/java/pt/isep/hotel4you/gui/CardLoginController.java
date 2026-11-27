package pt.isep.hotel4you.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import pt.isep.hotel4you.service.CardBlockedException;
import pt.isep.hotel4you.service.CardInactiveException;
import pt.isep.hotel4you.service.HotelFacade;
import pt.isep.hotel4you.service.LoginFailedException;

/**
 * Controller of the screen "Entrar com cartão" (H4Y-109 AC-4). The client logs in with the card
 * number and the access code, and the screen "O meu cartão" opens. The manager uses the master card.
 */
public class CardLoginController {

    private final HotelFacade facade;

    @FXML
    private TextField numberField;

    @FXML
    private PasswordField codeField;

    @FXML
    private Label messageLabel;

    /**
     * Creates the controller.
     *
     * @param facade the facade for the card login
     */
    public CardLoginController(HotelFacade facade) {
        this.facade = facade;
    }

    @FXML
    private void onLoginClicked() {
        try {
            HotelApp.currentCard = facade.loginWithCard(numberField.getText(), codeField.getText());
            messageLabel.getScene().setRoot(new ScreenLoader(facade).load("my-card.fxml"));
        } catch (IllegalArgumentException | LoginFailedException | CardBlockedException
                | CardInactiveException | IllegalStateException e) {
            messageLabel.setText(e.getMessage());
            codeField.clear();
        }
    }

    @FXML
    private void onBackClicked() {
        messageLabel.getScene().setRoot(new ScreenLoader(facade).load("login.fxml"));
    }
}
