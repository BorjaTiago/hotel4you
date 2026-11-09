package pt.isep.hotel4you.gui;

import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import pt.isep.hotel4you.service.HotelFacade;

/**
 * Opens a screen from an FXML file. Every controller gets the HotelFacade in its constructor.
 * All screens use this class. It also adds {@code style.css} to each screen.
 *
 * <p>Example: {@code Parent root = new ScreenLoader(facade).load("login.fxml");}
 * The FXML file must be in {@code src/main/resources/pt/isep/hotel4you/gui/}.
 * The controller needs a public constructor with one HotelFacade parameter.
 */
public class ScreenLoader {

    private final HotelFacade facade;

    /**
     * Creates a loader.
     *
     * @param facade the facade to give to each controller
     */
    public ScreenLoader(HotelFacade facade) {
        this.facade = facade;
    }

    /**
     * Opens a screen from an FXML file.
     *
     * @param fxmlName the file name, for example {@code "login.fxml"}
     * @return the screen, ready to show
     * @throws IllegalStateException if the screen cannot be opened
     */
    public Parent load(String fxmlName) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlName));
        loader.setControllerFactory(type -> createController(type));
        try {
            Parent root = loader.load();
            root.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
            return root;
        } catch (IOException e) {
            throw new IllegalStateException("Não foi possível abrir o ecrã.");
        }
    }

    private Object createController(Class<?> type) {
        try {
            return type.getConstructor(HotelFacade.class).newInstance(facade);
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException("Não foi possível criar o controlador do ecrã.");
        }
    }
}
