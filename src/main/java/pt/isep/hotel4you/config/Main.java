package pt.isep.hotel4you.config;

import java.nio.file.Path;
import javafx.application.Application;
import pt.isep.hotel4you.data.Database;
import pt.isep.hotel4you.data.JdbcFloorRepository;
import pt.isep.hotel4you.data.JdbcLocationRepository;
import pt.isep.hotel4you.data.JdbcRoomTypeRepository;
import pt.isep.hotel4you.data.JdbcUserRepository;
import pt.isep.hotel4you.gui.HotelApp;
import pt.isep.hotel4you.service.AuthService;
import pt.isep.hotel4you.service.HotelFacade;
import pt.isep.hotel4you.service.LocationService;
import pt.isep.hotel4you.service.RoomService;
import pt.isep.hotel4you.service.StaffService;

/** Starts the Hotel4You application. */
public class Main {

    private Main() {
    }

    /**
     * Builds the objects of the application and opens the window.
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            AppConfig config = AppConfig.load(Path.of("application.properties"));
            Database database = new Database(config);
            JdbcUserRepository userRepository = new JdbcUserRepository(database);
            AuthService authService = new AuthService(userRepository);
            StaffService staffService = new StaffService(userRepository, authService);
            LocationService locationService = new LocationService(
                    new JdbcFloorRepository(database), new JdbcLocationRepository(database));
            RoomService roomService = new RoomService(new JdbcRoomTypeRepository(database));
            HotelFacade facade = new HotelFacade(authService, staffService, locationService, roomService);

            // Check now that the database works.
            database.check();
            HotelApp.facade = facade;
        } catch (IllegalStateException e) {
            // HotelApp shows this error in a popup.
            HotelApp.startError = e.getMessage();
        }
        Application.launch(HotelApp.class, args);
    }
}
