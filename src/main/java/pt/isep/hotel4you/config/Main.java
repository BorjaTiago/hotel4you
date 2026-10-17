package pt.isep.hotel4you.config;

import java.nio.file.Path;
import java.sql.SQLException;
import javafx.application.Application;
import pt.isep.hotel4you.data.Database;
import pt.isep.hotel4you.data.JdbcUserRepository;
import pt.isep.hotel4you.gui.HotelApp;
import pt.isep.hotel4you.service.AuthService;
import pt.isep.hotel4you.service.HotelFacade;
import pt.isep.hotel4you.service.StaffService;

/** Starts the Hotel4You application. */
public class Main {

    private Main() {
    }

    /**
     * Builds the objects of the application and opens the window.
     *
     * @param args the command line arguments
     * @throws SQLException if the test connection cannot be closed
     */
    public static void main(String[] args) throws SQLException {
        try {
            AppConfig config = AppConfig.load(Path.of("application.properties"));
            Database database = new Database(config);
            JdbcUserRepository userRepository = new JdbcUserRepository(database);
            AuthService authService = new AuthService(userRepository);
            StaffService staffService = new StaffService(userRepository, authService);
            HotelFacade facade = new HotelFacade(authService, staffService);

            // Check now that the database works. Then close the test connection.
            database.connect().close();
            HotelApp.facade = facade;
        } catch (IllegalStateException e) {
            // HotelApp shows this error in a popup.
            HotelApp.startError = e.getMessage();
        }
        Application.launch(HotelApp.class, args);
    }
}
