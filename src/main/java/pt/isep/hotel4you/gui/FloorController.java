package pt.isep.hotel4you.gui;

import java.util.List;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.util.StringConverter;
import pt.isep.hotel4you.domain.Floor;
import pt.isep.hotel4you.domain.Location;
import pt.isep.hotel4you.service.HotelFacade;

/** Controller of the screen that shows the floors and the zones of each floor. */
public class FloorController {

    private final HotelFacade facade;

    @FXML
    private ListView<Floor> floorList;

    @FXML
    private TableView<Location> locationTable;

    @FXML
    private TableColumn<Location, String> nameColumn;

    @FXML
    private TableColumn<Location, String> guestCardColumn;

    @FXML
    private TableColumn<Location, String> reservationColumn;

    @FXML
    private TextField numberField;

    @FXML
    private TextField zoneNameField;

    @FXML
    private ComboBox<Floor> zoneFloorBox;

    @FXML
    private CheckBox guestCardBox;

    @FXML
    private CheckBox reservationBox;

    @FXML
    private Label messageLabel;

    /** The id of the zone chosen in the table, or null when none is chosen. */
    private Long selectedZoneId;

    /**
     * Creates the controller.
     *
     * @param facade the facade that finds the floors
     */
    public FloorController(HotelFacade facade) {
        this.facade = facade;
    }

    @FXML
    private void initialize() {
        zoneFloorBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(Floor floor) {
                return floor == null ? "" : floorLabel(floor);
            }

            @Override
            public Floor fromString(String text) {
                return null;
            }
        });
        floorList.setCellFactory(list -> new ListCell<>() {
            @Override
            protected void updateItem(Floor floor, boolean empty) {
                super.updateItem(floor, empty);
                setText(empty || floor == null ? null : floorLabel(floor));
            }
        });
        nameColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getName()));
        guestCardColumn.setCellValueFactory(
                data -> new SimpleStringProperty(yesNo(data.getValue().isGuestCardAccess())));
        reservationColumn.setCellValueFactory(
                data -> new SimpleStringProperty(yesNo(data.getValue().isRequiresReservation())));
        floorList.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldFloor, floor) -> showZones(floor));
        locationTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldZone, zone) -> fillForm(zone));
        loadFloors();
    }

    private void loadFloors() {
        try {
            floorList.getItems().setAll(facade.listFloors(HotelApp.currentUser));
            zoneFloorBox.getItems().setAll(floorList.getItems());
        } catch (RuntimeException e) {
            messageLabel.setText(e.getMessage());
        }
    }

    /** Reloads the floors and selects the floor with the given number again. */
    private void reloadAndSelect(int floorNumber) {
        loadFloors();
        floorList.getItems().stream()
                .filter(item -> item.number() == floorNumber)
                .findFirst()
                .ifPresent(item -> floorList.getSelectionModel().select(item));
    }

    private void fillForm(Location zone) {
        if (zone == null) {
            selectedZoneId = null;
            return;
        }
        selectedZoneId = zone.getId();
        zoneNameField.setText(zone.getName());
        guestCardBox.setSelected(zone.isGuestCardAccess());
        reservationBox.setSelected(zone.isRequiresReservation());
        selectZoneFloor(floorList.getSelectionModel().getSelectedItem());
    }

    private void selectZoneFloor(Floor floor) {
        zoneFloorBox.getItems().stream()
                .filter(item -> floor != null && item.number() == floor.number())
                .findFirst()
                .ifPresent(item -> zoneFloorBox.setValue(item));
    }

    @FXML
    private void onCreateLocationClicked() {
        messageLabel.setText("");
        Floor floor = zoneFloorBox.getValue();
        if (floor == null) {
            messageLabel.setText("Escolha o piso da zona.");
            return;
        }
        try {
            facade.createLocation(floor.number(), zoneNameField.getText(), guestCardBox.isSelected(),
                    reservationBox.isSelected(), HotelApp.currentUser);
            finishAction(floor.number(), "Zona criada.");
        } catch (RuntimeException e) {
            messageLabel.setText(e.getMessage());
        }
    }

    @FXML
    private void onSaveLocationClicked() {
        messageLabel.setText("");
        Floor floor = zoneFloorBox.getValue();
        if (selectedZoneId == null || floor == null) {
            messageLabel.setText("Escolha uma zona na tabela e o piso.");
            return;
        }
        try {
            facade.editLocation(selectedZoneId, floor.number(), zoneNameField.getText(),
                    guestCardBox.isSelected(), reservationBox.isSelected(), HotelApp.currentUser);
            finishAction(floor.number(), "Zona guardada.");
        } catch (RuntimeException e) {
            messageLabel.setText(e.getMessage());
        }
    }

    @FXML
    private void onRemoveLocationClicked() {
        messageLabel.setText("");
        Location zone = locationTable.getSelectionModel().getSelectedItem();
        Floor floor = floorList.getSelectionModel().getSelectedItem();
        if (zone == null || floor == null) {
            messageLabel.setText("Escolha uma zona na tabela.");
            return;
        }
        Alert question = new Alert(Alert.AlertType.CONFIRMATION, "Remover a zona " + zone.getName() + "?",
                ButtonType.YES, ButtonType.NO);
        if (question.showAndWait().orElse(ButtonType.NO) != ButtonType.YES) {
            return;
        }
        try {
            facade.removeLocation(zone.getId(), HotelApp.currentUser);
            finishAction(floor.number(), "Zona removida.");
        } catch (RuntimeException e) {
            messageLabel.setText(e.getMessage());
        }
    }

    private void finishAction(int floorNumber, String message) {
        reloadAndSelect(floorNumber);
        clearForm();
        messageLabel.setText(message);
    }

    private void clearForm() {
        selectedZoneId = null;
        zoneNameField.clear();
        guestCardBox.setSelected(false);
        reservationBox.setSelected(false);
    }

    private static String floorLabel(Floor floor) {
        return "Piso " + floor.number() + " - " + floor.description();
    }

    private void showZones(Floor floor) {
        selectZoneFloor(floor);
        locationTable.getItems().setAll(floor == null ? List.of() : floor.locations());
    }

    @FXML
    private void onSearchClicked() {
        messageLabel.setText("");
        int number;
        try {
            number = Integer.parseInt(numberField.getText().trim());
        } catch (NumberFormatException e) {
            messageLabel.setText("Escreva um número de piso.");
            return;
        }
        try {
            Floor floor = facade.findFloor(number, HotelApp.currentUser);
            floorList.getItems().stream()
                    .filter(item -> item.number() == floor.number())
                    .findFirst()
                    .ifPresent(item -> floorList.getSelectionModel().select(item));
        } catch (RuntimeException e) {
            messageLabel.setText(e.getMessage());
        }
    }

    private static String yesNo(boolean value) {
        return value ? "Sim" : "Não";
    }
}
