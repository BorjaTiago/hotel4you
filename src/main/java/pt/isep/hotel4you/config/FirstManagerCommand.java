package pt.isep.hotel4you.config;

import java.nio.file.Path;
import pt.isep.hotel4you.data.Database;
import pt.isep.hotel4you.data.JdbcUserRepository;
import pt.isep.hotel4you.service.AuthService;
import pt.isep.hotel4you.service.StaffService;

/** Command line tool that creates the first manager from {@code application.properties}. */
public class FirstManagerCommand {

    private FirstManagerCommand() {
    }

    /**
     * Creates the first manager. Prints the result. Exits with code 1 on error.
     *
     * @param args not used
     */
    public static void main(String[] args) {
        try {
            AppConfig config = AppConfig.load(Path.of("application.properties"));
            Database database = new Database(config);
            JdbcUserRepository userRepository = new JdbcUserRepository(database);
            AuthService authService = new AuthService(userRepository);
            StaffService staffService = new StaffService(userRepository, authService);

            boolean created = staffService.createFirstManager(config.managerUsername(), config.managerPassword());
            System.out.println(created ? "Manager created." : "A manager already exists.");
        } catch (RuntimeException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }
}
