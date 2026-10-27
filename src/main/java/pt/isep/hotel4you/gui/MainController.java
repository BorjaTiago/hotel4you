package pt.isep.hotel4you.gui;

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
 */
public class MainController {

    private final HotelFacade facade;

    @FXML
    private Label welcomeLabel;

    @FXML
    private Button staffButton;

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
        welcomeLabel.setText("Bem-vindo, " + user.getName());
        staffButton.managedProperty().bind(staffButton.visibleProperty());
        staffButton.setVisible(facade.canUse(user, Permission.MANAGE_STAFF));
    }

    @FXML
    private void onStaffClicked() {
        Parent staffScreen = new ScreenLoader(facade).load("staff.fxml");
        contentPane.getChildren().setAll(staffScreen);
    }

    @FXML
    private void onLogoutClicked() {
        HotelApp.currentUser = null;
        Parent root = new ScreenLoader(facade).load("login.fxml");
        Stage stage = (Stage) contentPane.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.sizeToScene();
        stage.centerOnScreen();
    }
}
