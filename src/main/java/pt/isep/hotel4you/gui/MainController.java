package pt.isep.hotel4you.gui;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import pt.isep.hotel4you.domain.Permission;
import pt.isep.hotel4you.domain.User;
import pt.isep.hotel4you.service.HotelFacade;

/**
 * Controller of the main window. It shows the menu of the logged-in user and opens each
 * function in the centre. The menu asks the facade which functions the user may see.
 * Without a logged-in user, it shows no function and opens the login screen.
 */
public class MainController {

    private final HotelFacade facade;

    @FXML
    private Label welcomeLabel;

    @FXML
    private Button staffButton;

    @FXML
    private Button floorsButton;

    @FXML
    private Button roomsButton;

    @FXML
    private Button reservationsListButton;

    @FXML
    private Button pricesButton;

    @FXML
    private Button stockButton;

    @FXML
    private StackPane contentPane;

    /**
     * Creates the controller.
     *
     * @param facade the facade that decides which functions the user may see
     */
    public MainController(HotelFacade facade) {
        this.facade = facade;
    }

    @FXML
    private void initialize() {
        User user = HotelApp.currentUser;
        if (user == null) {
            welcomeLabel.setText("");
            Platform.runLater(this::openLogin);
            return;
        }
        staffButton.managedProperty().bind(staffButton.visibleProperty());
        staffButton.setVisible(facade.canUse(user, Permission.MANAGE_STAFF));
        floorsButton.managedProperty().bind(floorsButton.visibleProperty());
        floorsButton.setVisible(facade.canUse(user, Permission.MANAGE_FLOORS));
        roomsButton.managedProperty().bind(roomsButton.visibleProperty());
        roomsButton.setVisible(facade.canUse(user, Permission.LIST_ROOMS));
        reservationsListButton.managedProperty().bind(reservationsListButton.visibleProperty());
        reservationsListButton.setVisible(facade.canUse(user, Permission.MANAGE_RESERVATIONS));
        pricesButton.managedProperty().bind(pricesButton.visibleProperty());
        pricesButton.setVisible(facade.canUse(user, Permission.MANAGE_PRICES));
        stockButton.managedProperty().bind(stockButton.visibleProperty());
        stockButton.setVisible(facade.canUse(user, Permission.MANAGE_STOCK));
        welcomeLabel.setText(
                "Olá, " + user.getName() + " (" + user.getRole().displayName() + ")");
        if (facade.canUse(user, Permission.MANAGE_RESERVATIONS)) {
            onReservationsListClicked();
        }
    }

    @FXML
    private void onStaffClicked() {
        Parent staffScreen = new ScreenLoader(facade).load("staff.fxml");
        contentPane.getChildren().setAll(staffScreen);
    }

    @FXML
    private void onFloorsClicked() {
        Parent floorsScreen = new ScreenLoader(facade).load("floors.fxml");
        contentPane.getChildren().setAll(floorsScreen);
    }

    @FXML
    private void onRoomsClicked() {
        Parent roomScreen = new ScreenLoader(facade).load("rooms.fxml");
        contentPane.getChildren().setAll(roomScreen);
    }

    @FXML
    private void onReservationsListClicked() {
        Parent reservationsListScreen = new ScreenLoader(facade).load("reservations-list.fxml");
        contentPane.getChildren().setAll(reservationsListScreen);
    }

    @FXML
    private void onPricesClicked() {
        Parent pricesScreen = new ScreenLoader(facade).load("prices.fxml");
        contentPane.getChildren().setAll(pricesScreen);
    }

    @FXML
    private void onStockClicked() {
        Parent stockScreen = new ScreenLoader(facade).load("stock.fxml");
        contentPane.getChildren().setAll(stockScreen);
    }

    @FXML
    private void onLogoutClicked() {
        HotelApp.currentUser = null;
        openLogin();
    }

    private void openLogin() {
        Scene scene = contentPane.getScene();
        if (scene == null || !(scene.getWindow() instanceof Stage stage)) {
            return;
        }
        Parent root = new ScreenLoader(facade).load("login.fxml");
        stage.setScene(new Scene(root));
        stage.sizeToScene();
        stage.centerOnScreen();
    }
}
