package pt.isep.hotel4you.gui;

import java.util.List;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
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
    private Label messageLabel;

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
        floorList.setCellFactory(list -> new ListCell<>() {
            @Override
            protected void updateItem(Floor floor, boolean empty) {
                super.updateItem(floor, empty);
                setText(empty || floor == null ? null : "Piso " + floor.number() + " - " + floor.description());
            }
        });
        nameColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getName()));
        guestCardColumn.setCellValueFactory(
                data -> new SimpleStringProperty(yesNo(data.getValue().isGuestCardAccess())));
        reservationColumn.setCellValueFactory(
                data -> new SimpleStringProperty(yesNo(data.getValue().isRequiresReservation())));
        floorList.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldFloor, floor) -> showZones(floor));
        loadFloors();
    }

    private void loadFloors() {
        try {
            floorList.getItems().setAll(facade.listFloors(HotelApp.currentUser));
        } catch (RuntimeException e) {
            messageLabel.setText(e.getMessage());
        }
    }

    private void showZones(Floor floor) {
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
