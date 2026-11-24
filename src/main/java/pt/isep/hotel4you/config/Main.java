package pt.isep.hotel4you.config;

import java.nio.file.Path;
import javafx.application.Application;
import java.time.Clock;
import pt.isep.hotel4you.data.Database;
import pt.isep.hotel4you.data.JdbcCardAccessRecordRepository;
import pt.isep.hotel4you.data.JdbcCardRepository;
import pt.isep.hotel4you.data.JdbcClientRepository;
import pt.isep.hotel4you.data.JdbcExtraPriceRepository;
import pt.isep.hotel4you.data.JdbcFloorRepository;
import pt.isep.hotel4you.data.JdbcItemRepository;
import pt.isep.hotel4you.data.JdbcLocationRepository;
import pt.isep.hotel4you.data.JdbcRoomRepository;
import pt.isep.hotel4you.data.JdbcReservationRepository;
import pt.isep.hotel4you.data.JdbcRoomTypeRepository;
import pt.isep.hotel4you.data.JdbcUserRepository;
import pt.isep.hotel4you.gui.HotelApp;
import pt.isep.hotel4you.service.AccessCodeGenerator;
import pt.isep.hotel4you.service.AuthService;
import pt.isep.hotel4you.service.CardAccessService;
import pt.isep.hotel4you.service.CardNumberGenerator;
import pt.isep.hotel4you.service.ClientService;
import pt.isep.hotel4you.service.ExtraPriceService;
import pt.isep.hotel4you.service.HotelFacade;
import pt.isep.hotel4you.service.LocationService;
import pt.isep.hotel4you.service.ReservationService;
import pt.isep.hotel4you.service.RoomService;
import pt.isep.hotel4you.service.StaffService;
import pt.isep.hotel4you.service.StockService;

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
            JdbcRoomTypeRepository roomTypeRepository = new JdbcRoomTypeRepository(database);
            JdbcReservationRepository reservationRepository = new JdbcReservationRepository(database);
            JdbcItemRepository itemRepository = new JdbcItemRepository(database);
            RoomService roomService = new RoomService(
                    new JdbcRoomRepository(database), roomTypeRepository, reservationRepository, itemRepository);
            StockService stockService = new StockService(itemRepository);
            ClientService clientService = new ClientService(new JdbcClientRepository(database));
            ReservationService reservationService = new ReservationService(reservationRepository,
                    roomService, roomTypeRepository);
            ExtraPriceService extraPriceService = new ExtraPriceService(new JdbcExtraPriceRepository(database));
            JdbcCardRepository cardRepository = new JdbcCardRepository(database);
            CardAccessService cardAccessService = new CardAccessService(cardRepository,
                    new JdbcCardAccessRecordRepository(database), reservationRepository, userRepository,
                    locationService, new CardNumberGenerator(cardRepository),
                    new AccessCodeGenerator(), authService, Clock.systemDefaultZone());
            HotelFacade facade = new HotelFacade(authService, staffService, locationService, roomService,
                    stockService, clientService, reservationService,
                    extraPriceService, cardAccessService);

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
