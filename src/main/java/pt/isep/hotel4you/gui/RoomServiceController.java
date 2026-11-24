package pt.isep.hotel4you.gui;

import java.time.format.DateTimeFormatter;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.util.StringConverter;
import pt.isep.hotel4you.domain.RoomServiceRequest;
import pt.isep.hotel4you.domain.RoomServiceType;
import pt.isep.hotel4you.service.AccessDeniedException;
import pt.isep.hotel4you.service.HotelFacade;
import pt.isep.hotel4you.service.RoomNotOccupiedException;

/** Screen where the receptionist registers a room service request (H4Y-110). */
public class RoomServiceController {
    private static final DateTimeFormatter DATE = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter TIME = DateTimeFormatter.ofPattern("HH:mm");

    private final HotelFacade facade;
    @FXML private Label roomLabel;
    @FXML private TextField roomField;
    @FXML private Label typeLabel;
    @FXML private ComboBox<RoomServiceType> typeBox;
    @FXML private Label messageLabel;

    /**
     * Creates the controller.
     *
     * @param facade the facade that registers the request
     */
    public RoomServiceController(HotelFacade facade) {
        this.facade = facade;
    }

    @FXML
    private void initialize() {
        roomLabel.setLabelFor(roomField);
        typeLabel.setLabelFor(typeBox);
        typeBox.getItems().setAll(RoomServiceType.values());
        typeBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(RoomServiceType type) {
                return type == null ? "" : type.displayName();
            }

            @Override
            public RoomServiceType fromString(String text) {
                return null;
            }
        });
    }

    @FXML
    private void onRegisterClicked() {
        showMessage("", false);
        RoomServiceRequest request;
        try {
            request = facade.registerRoomService(roomField.getText(), typeBox.getValue(),
                    HotelApp.currentUser);
        } catch (AccessDeniedException | IllegalArgumentException | RoomNotOccupiedException
                | IllegalStateException error) {
            showMessage(error.getMessage(), false);
            return;
        }
        roomField.clear();
        typeBox.getSelectionModel().clearSelection();
        showMessage(confirmationText(request), true);
    }

    private void showMessage(String text, boolean success) {
        messageLabel.setText(text);
        messageLabel.getStyleClass().remove("message-success");
        if (success) {
            messageLabel.getStyleClass().add("message-success");
        }
    }

    /**
     * Builds the confirmation with the type, the room and the date and time of the request.
     *
     * @param request the saved request
     * @return the text to show
     */
    static String confirmationText(RoomServiceRequest request) {
        return "Pedido registado: " + request.getType().displayName() + " para o quarto "
                + request.getRoomNumber() + ", em " + DATE.format(request.getRequestedAt())
                + " às " + TIME.format(request.getRequestedAt()) + ".";
    }
}
