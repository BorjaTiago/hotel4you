package pt.isep.hotel4you.service;

import java.math.BigDecimal;
import java.util.List;
import pt.isep.hotel4you.data.ExtraPriceRepository;
import pt.isep.hotel4you.domain.ExtraService;
import pt.isep.hotel4you.domain.Permission;
import pt.isep.hotel4you.domain.User;

/** Applies manager permissions and validates fixed extra prices per night. */
public class ExtraPriceService {
    private static final BigDecimal MAX_PRICE = new BigDecimal("999999.99");
    private static final int PRICE_SCALE = 2;
    private final ExtraPriceRepository repository;

    /**
     * Creates the extra price service.
     * @param repository the extra price catalogue
     */
    public ExtraPriceService(ExtraPriceRepository repository) {
        this.repository = repository;
    }

    /**
     * Lists the extra prices, including entries without a price.
     * @param actor the logged-in user
     * @return catalogue entries ordered by identifier
     * @throws AccessDeniedException if the actor is not a manager
     * @throws IllegalStateException if the database is not available
     */
    public List<ExtraService> listPrices(User actor) {
        if (!Permission.MANAGE_EXTRA_PRICES.allows(actor)) {
            throw new AccessDeniedException("Só o gestor pode consultar os preços extra.");
        }
        return repository.findAll();
    }

    /**
     * Sets the extra price per night. Invalid data does not update the catalogue.
     * @param extra the selected saved catalogue entry
     * @param price the price from 0.00 to 999999.99, with at most two decimal places
     * @param actor the logged-in user
     * @return the success message in Portuguese
     * @throws AccessDeniedException if the actor is not a manager
     * @throws IllegalArgumentException if the entry or price is invalid, or the id does not exist
     * @throws IllegalStateException if the database is not available
     */
    public String setPrice(ExtraService extra, BigDecimal price, User actor) {
        if (!Permission.MANAGE_EXTRA_PRICES.allows(actor)) {
            throw new AccessDeniedException("Só o gestor pode definir os preços extra.");
        }
        if (extra == null || extra.getId() == null) {
            throw new IllegalArgumentException("Escolha um extra na tabela.");
        }
        if (price == null || price.signum() < 0 || price.scale() > PRICE_SCALE
                || price.compareTo(MAX_PRICE) > 0) {
            throw new IllegalArgumentException(
                    "O preço tem de estar entre 0,00 e 999999,99, com duas casas decimais.");
        }
        if (!repository.updatePrice(extra.getId(), price.setScale(PRICE_SCALE))) {
            throw new IllegalArgumentException("O extra indicado não existe.");
        }
        return "Preço extra guardado.";
    }

    /**
     * Parses the price text, with a comma or a point as decimal mark.
     * @param text the entered price
     * @return the numeric price, for validation by {@link #setPrice}
     * @throws IllegalArgumentException if the text is null, blank or not a plain decimal number
     */
    public BigDecimal parsePrice(String text) {
        if (text == null) {
            throw new IllegalArgumentException("O preço tem de ser um número.");
        }
        String number = text.trim().replace(',', '.');
        if (!number.matches("-?[0-9]+(\\.[0-9]+)?")) {
            throw new IllegalArgumentException("O preço tem de ser um número.");
        }
        return new BigDecimal(number);
    }
}
