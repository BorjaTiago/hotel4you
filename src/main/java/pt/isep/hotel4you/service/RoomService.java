package pt.isep.hotel4you.service;

import java.math.BigDecimal;
import java.util.List;
import pt.isep.hotel4you.data.RoomTypeRepository;
import pt.isep.hotel4you.domain.Permission;
import pt.isep.hotel4you.domain.RoomType;
import pt.isep.hotel4you.domain.User;

/** Sets the base price of the room types. */
public class RoomService {

    /** The biggest price that fits in DECIMAL(8,2). */
    private static final BigDecimal MAX_PRICE = new BigDecimal("999999.99");
    private static final int PRICE_SCALE = 2;
    /** Plain ASCII digits only. BigDecimal alone also accepts exponents and non-ASCII digits. */
    private static final String PRICE_TEXT = "-?\\d+(\\.\\d+)?";

    private final RoomTypeRepository roomTypeRepository;

    /**
     * Creates the service.
     *
     * @param roomTypeRepository the room type store
     */
    public RoomService(RoomTypeRepository roomTypeRepository) {
        this.roomTypeRepository = roomTypeRepository;
    }

    /**
     * Lists the room types with their base price.
     *
     * @param actor the logged-in user who asks
     * @return the room types ordered by id
     * @throws AccessDeniedException if the actor may not see the prices
     * @throws IllegalStateException if the database is not available
     */
    public List<RoomType> listBasePrices(User actor) {
        if (!Permission.MANAGE_PRICES.allows(actor)) {
            throw new AccessDeniedException("Só o gestor pode consultar os preços.");
        }
        return roomTypeRepository.findAll();
    }

    /**
     * Converts the text of the screen to a price. It accepts a comma or a point as decimal mark.
     *
     * @param text the text typed by the user
     * @return the price
     * @throws IllegalArgumentException if the text is null, blank or not a number
     */
    public BigDecimal parsePrice(String text) {
        if (text == null) {
            throw new IllegalArgumentException("O preço tem de ser um número.");
        }
        String number = text.trim().replace(',', '.');
        if (!number.matches(PRICE_TEXT)) {
            throw new IllegalArgumentException("O preço tem de ser um número.");
        }
        return new BigDecimal(number);
    }

    /**
     * Sets the base price per night of a room type.
     *
     * @param type the room type
     * @param price the new price, from 0.00 to 999999.99 with at most two decimals
     * @param actor the logged-in user who asks
     * @throws AccessDeniedException if the actor may not set the prices
     * @throws IllegalArgumentException if the type or the price is not valid, or the type does not exist
     * @throws IllegalStateException if the database is not available
     */
    public void setBasePrice(RoomType type, BigDecimal price, User actor) {
        if (!Permission.MANAGE_PRICES.allows(actor)) {
            throw new AccessDeniedException("Só o gestor pode definir os preços.");
        }
        if (type == null || type.getId() == null) {
            throw new IllegalArgumentException("Indique o tipo de quarto.");
        }
        if (!isValidPrice(price)) {
            throw new IllegalArgumentException(
                    "O preço tem de estar entre 0,00 e 999999,99, com duas casas decimais.");
        }
        if (!roomTypeRepository.updateBasePrice(type.getId(), price.setScale(PRICE_SCALE))) {
            throw new IllegalArgumentException("O tipo de quarto indicado não existe.");
        }
    }

    private static boolean isValidPrice(BigDecimal price) {
        return price != null
                && price.signum() >= 0
                && price.scale() <= PRICE_SCALE
                && price.compareTo(MAX_PRICE) <= 0;
    }
}
