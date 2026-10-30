package pt.isep.hotel4you.gui;

import java.util.List;
import java.util.Map;
import java.util.StringJoiner;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.util.StringConverter;
import pt.isep.hotel4you.domain.Room;
import pt.isep.hotel4you.service.HotelFacade;
import pt.isep.hotel4you.service.RoomService.RoomTypeTarget;

/** Controller for room registration and the room list. */
public class RoomController {

    private final HotelFacade facade;
    private Map<Integer, Integer> floorLimits;
    private List<RoomTypeTarget> typeTargets;

    @FXML
    private TextField numberField;

    @FXML
    private ComboBox<Integer> floorBox;

    @FXML
    private ComboBox<String> roomTypeBox;

    @FXML
    private Label messageLabel;

    @FXML
    private Label summaryLabel;

    @FXML
    private TableView<Room> roomTable;

    @FXML
    private TableColumn<Room, Integer> numberColumn;

    @FXML
    private TableColumn<Room, Integer> floorColumn;

    @FXML
    private TableColumn<Room, String> roomTypeColumn;

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
        floorLimits = facade.roomFloorLimits();
        typeTargets = facade.roomTypeTargets();
        floorBox.getItems().setAll(floorLimits.keySet().stream().sorted().toList());
        numberColumn.setCellValueFactory(
                data -> new ReadOnlyObjectWrapper<>(data.getValue().getNumber()));
        floorColumn.setCellValueFactory(
                data -> new ReadOnlyObjectWrapper<>(data.getValue().getFloorNumber()));
        roomTypeColumn.setCellValueFactory(
                data -> new SimpleStringProperty(typeText(data.getValue().getRoomTypeName())));
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
        loadRooms();
    }

    @FXML
    private void onSaveRoomClicked() {
        try {
            if (floorBox.getValue() == null || roomTypeBox.getValue() == null) {
                throw new IllegalArgumentException("Escolha o piso e o tipo de quarto.");
            }
            int number = readRoomNumber();
            facade.saveRoom(new Room(null, number, floorBox.getValue(), roomTypeBox.getValue()),
                    HotelApp.currentUser);
            showMessage("Quarto registado.", true);
            numberField.clear();
            loadRooms();
        } catch (RuntimeException e) {
            showMessage(e.getMessage(), false);
        }
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

    private void loadRoomTypes() {
        try {
            roomTypeBox.getItems().setAll(facade.listRoomTypes(HotelApp.currentUser));
        } catch (RuntimeException e) {
            showMessage(e.getMessage(), false);
        }
    }

    private void loadRooms() {
        try {
            List<Room> rooms = facade.listRooms(HotelApp.currentUser);
            roomTable.getItems().setAll(rooms);
            updateSummary(rooms);
        } catch (RuntimeException e) {
            roomTable.getItems().clear();
            summaryLabel.setText("");
            showMessage(e.getMessage(), false);
        }
    }

    private void updateSummary(List<Room> rooms) {
        StringJoiner summary = new StringJoiner(" · ");
        for (int floor : floorBox.getItems()) {
            summary.add("Piso " + floor + ": " + countOnFloor(rooms, floor) + "/" + floorLimits.get(floor));
        }
        for (RoomTypeTarget type : typeTargets) {
            summary.add(type.label() + " " + countOfType(rooms, type.name()) + "/" + type.total());
        }
        summaryLabel.setText(summary.toString());
    }

    private static String typeText(String name) {
        return name.substring(0, 1).toUpperCase() + name.substring(1);
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
        messageLabel.setStyle(success ? "-fx-text-fill: #2e7d32;" : "-fx-text-fill: #b00020;");
    }
}
