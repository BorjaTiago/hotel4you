package pt.isep.hotel4you.gui;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.StringJoiner;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;
import pt.isep.hotel4you.domain.Floor;
import pt.isep.hotel4you.domain.Permission;
import pt.isep.hotel4you.domain.Room;
import pt.isep.hotel4you.domain.RoomSummary;
import pt.isep.hotel4you.service.HotelFacade;
import pt.isep.hotel4you.service.RoomService.RoomTypeTarget;

/** Controller for the room screen: list with type filter, and registration for managers. */
public class RoomController {

    private static final String ALL_TYPES = "";
    private static final NumberFormat PRICE_FORMAT =
            NumberFormat.getCurrencyInstance(Locale.forLanguageTag("pt-PT"));

    private final HotelFacade facade;
    private Map<Integer, Integer> floorLimits;
    private List<RoomTypeTarget> typeTargets;

    @FXML
    private VBox formBox;

    @FXML
    private TextField numberField;

    @FXML
    private ComboBox<Floor> floorBox;

    @FXML
    private ComboBox<String> roomTypeBox;

    @FXML
    private ComboBox<String> filterBox;

    @FXML
    private Label messageLabel;

    @FXML
    private Label summaryLabel;

    @FXML
    private TableView<RoomSummary> roomTable;

    @FXML
    private TableColumn<RoomSummary, Integer> numberColumn;

    @FXML
    private TableColumn<RoomSummary, String> floorColumn;

    @FXML
    private TableColumn<RoomSummary, String> roomTypeColumn;

    @FXML
    private TableColumn<RoomSummary, String> basePriceColumn;

    /**
     * Creates the controller.
     *
     * @param facade the entry point for room operations
     */
    public RoomController(HotelFacade facade) {
        this.facade = facade;
    }

    @FXML
    private void initialize() {
        numberColumn.setCellValueFactory(
                data -> new ReadOnlyObjectWrapper<>(data.getValue().number()));
        floorColumn.setCellValueFactory(
                data -> new SimpleStringProperty(floorText(data.getValue().floorNumber(),
                        data.getValue().floorDescription())));
        roomTypeColumn.setCellValueFactory(
                data -> new SimpleStringProperty(typeText(data.getValue().roomTypeName())));
        basePriceColumn.setCellValueFactory(
                data -> new SimpleStringProperty(priceText(data.getValue().basePrice())));
        filterBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(String name) {
                return name == null || name.isEmpty() ? "Todos" : typeText(name);
            }

            @Override
            public String fromString(String text) {
                return text;
            }
        });
        boolean manager = facade.canUse(HotelApp.currentUser, Permission.MANAGE_ROOMS);
        formBox.managedProperty().bind(formBox.visibleProperty());
        formBox.setVisible(manager);
        summaryLabel.managedProperty().bind(summaryLabel.visibleProperty());
        summaryLabel.setVisible(manager);
        loadFilterChoices();
        if (manager) {
            initManagerForm();
        }
        loadRooms();
        filterBox.setOnAction(event -> {
            messageLabel.setText("");
            loadRooms();
        });
    }

    private void initManagerForm() {
        floorLimits = facade.roomFloorLimits();
        typeTargets = facade.roomTypeTargets();
        loadFloors();
        floorBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(Floor floor) {
                return floor == null ? "" : floorText(floor.number(), floor.description());
            }

            @Override
            public Floor fromString(String text) {
                return null;
            }
        });
        roomTypeBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(String name) {
                return name == null ? "" : typeText(name);
            }

            @Override
            public String fromString(String text) {
                return null;
            }
        });
        loadRoomTypes();
        roomTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldRoom, selected) -> fillForm(selected));
    }

    private void fillForm(RoomSummary selected) {
        if (selected == null) {
            return;
        }
        numberField.setText(String.valueOf(selected.number()));
        floorBox.getItems().stream()
                .filter(floor -> floor.number() == selected.floorNumber())
                .findFirst().ifPresent(floorBox::setValue);
        roomTypeBox.getItems().stream()
                .filter(name -> name.equalsIgnoreCase(selected.roomTypeName()))
                .findFirst().ifPresent(roomTypeBox::setValue);
    }

    @FXML
    private void onSaveRoomClicked() {
        try {
            facade.saveRoom(readRoom(null), HotelApp.currentUser);
            showMessage("Quarto registado.", true);
            numberField.clear();
            loadRooms();
        } catch (RuntimeException e) {
            showMessage(e.getMessage(), false);
        }
    }

    @FXML
    private void onSaveRoomChangesClicked() {
        RoomSummary selected = roomTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showMessage("Escolha um quarto na tabela.", false);
            return;
        }
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION,
                "Guardar as alterações do quarto " + selected.number() + "?",
                ButtonType.YES, ButtonType.NO);
        if (confirmation.showAndWait().orElse(ButtonType.NO) != ButtonType.YES) {
            return;
        }
        try {
            facade.editRoom(readRoom(selected.id()), HotelApp.currentUser);
            clearForm();
            showMessage("Quarto guardado.", true);
            loadRooms();
        } catch (RuntimeException e) {
            showMessage(e.getMessage(), false);
        }
    }

    @FXML
    private void onRemoveRoomClicked() {
        RoomSummary selected = roomTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showMessage("Escolha um quarto na tabela.", false);
            return;
        }
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION,
                "Remover o quarto " + selected.number() + "?",
                ButtonType.YES, ButtonType.NO);
        if (confirmation.showAndWait().orElse(ButtonType.NO) != ButtonType.YES) {
            return;
        }
        try {
            facade.removeRoom(selected.id(), HotelApp.currentUser);
            clearForm();
            showMessage("Quarto removido.", true);
            loadRooms();
        } catch (RuntimeException e) {
            showMessage(e.getMessage(), false);
        }
    }

    @FXML
    private void onClearClicked() {
        clearForm();
        messageLabel.setText("");
    }

    private void clearForm() {
        numberField.clear();
        floorBox.setValue(null);
        roomTypeBox.setValue(null);
        roomTable.getSelectionModel().clearSelection();
    }

    private Room readRoom(Long id) {
        if (floorBox.getValue() == null || roomTypeBox.getValue() == null) {
            throw new IllegalArgumentException("Escolha o piso e o tipo de quarto.");
        }
        int number = readRoomNumber();
        return new Room(id, number, floorBox.getValue().number(), roomTypeBox.getValue());
    }

    private int readRoomNumber() {
        String numberText = numberField.getText();
        if (numberText == null || numberText.isBlank()) {
            throw new IllegalArgumentException("Indique o número do quarto.");
        }
        try {
            return Integer.parseInt(numberText.trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Indique um número de quarto válido.");
        }
    }

    private void loadFilterChoices() {
        List<String> choices = new ArrayList<>();
        choices.add(ALL_TYPES);
        try {
            facade.roomTypeTargets().stream().map(RoomTypeTarget::name).forEach(choices::add);
        } catch (RuntimeException e) {
            showMessage(e.getMessage(), false);
        }
        filterBox.getItems().setAll(choices);
        filterBox.getSelectionModel().selectFirst();
    }

    private void loadFloors() {
        try {
            floorBox.getItems().setAll(facade.listFloors(HotelApp.currentUser).stream()
                    .filter(floor -> floorLimits.containsKey(floor.number()))
                    .toList());
        } catch (RuntimeException e) {
            showMessage(e.getMessage(), false);
        }
    }

    private void loadRoomTypes() {
        try {
            roomTypeBox.getItems().setAll(facade.listRoomTypes(HotelApp.currentUser));
        } catch (RuntimeException e) {
            showMessage(e.getMessage(), false);
        }
    }

    private void loadRooms() {
        try {
            roomTable.getItems().setAll(
                    facade.listRoomSummaries(filterBox.getValue(), HotelApp.currentUser));
            if (formBox.isVisible()) {
                updateSummary(facade.listRooms(HotelApp.currentUser));
            }
        } catch (RuntimeException e) {
            roomTable.getItems().clear();
            summaryLabel.setText("");
            showMessage(e.getMessage(), false);
        }
    }

    private void updateSummary(List<Room> rooms) {
        StringJoiner summary = new StringJoiner(" · ");
        for (Floor floor : floorBox.getItems()) {
            summary.add(floorText(floor.number(), floor.description()) + ": "
                    + countOnFloor(rooms, floor.number()) + "/" + floorLimits.get(floor.number()));
        }
        for (RoomTypeTarget type : typeTargets) {
            summary.add(type.label() + " " + countOfType(rooms, type.name()) + "/" + type.total());
        }
        summaryLabel.setText(summary.toString());
    }

    private static String floorText(int number, String description) {
        return number + " - " + description;
    }

    private static String typeText(String name) {
        return name.substring(0, 1).toUpperCase() + name.substring(1);
    }

    private static String priceText(BigDecimal basePrice) {
        return basePrice == null ? "—" : PRICE_FORMAT.format(basePrice);
    }

    private static long countOnFloor(List<Room> rooms, int floorNumber) {
        return rooms.stream().filter(room -> room.getFloorNumber() == floorNumber).count();
    }

    private static long countOfType(List<Room> rooms, String roomTypeName) {
        return rooms.stream()
                .filter(room -> room.getRoomTypeName().equalsIgnoreCase(roomTypeName))
                .count();
    }

    private void showMessage(String message, boolean success) {
        messageLabel.setText(message == null ? "O pedido não foi concluído." : message);
        messageLabel.getStyleClass().remove("message-success");
        if (success) {
            messageLabel.getStyleClass().add("message-success");
        }
    }
}
