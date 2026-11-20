package pt.isep.hotel4you.service;

import java.math.BigDecimal;
import java.util.List;
import pt.isep.hotel4you.data.ExtraPriceRepository;
import pt.isep.hotel4you.domain.ExtraService;
import pt.isep.hotel4you.domain.Permission;
import pt.isep.hotel4you.domain.User;

/** Applies manager permissions and validates fixed extra prices per night. */
public class ExtraPriceService {
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
     * @throws AccessDeniedException if the actor is not a manager
     * @throws IllegalArgumentException if the entry or price is invalid, or the id does not exist
     * @throws IllegalStateException if the database is not available
     */
    public void setPrice(ExtraService extra, BigDecimal price, User actor) {
        if (!Permission.MANAGE_EXTRA_PRICES.allows(actor)) {
            throw new AccessDeniedException("Só o gestor pode definir os preços extra.");
        }
        if (extra == null || extra.getId() == null) {
            throw new IllegalArgumentException("Escolha um extra na tabela.");
        }
        BigDecimal amount = PriceRules.normalize(price);
        if (!repository.updatePrice(extra.getId(), amount)) {
            throw new IllegalArgumentException("O extra indicado não existe.");
        }
    }
}
