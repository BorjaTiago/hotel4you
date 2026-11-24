package pt.isep.hotel4you.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import pt.isep.hotel4you.data.InMemoryExtraPriceRepository;
import pt.isep.hotel4you.domain.ExtraService;
import pt.isep.hotel4you.domain.ExtraType;
import pt.isep.hotel4you.domain.Manager;
import pt.isep.hotel4you.domain.Receptionist;

/** Tests delegation and actor checks at the extra price facade boundary. */
class ExtraPriceFacadeTest {
    private final Manager manager = new Manager(1L, "Gestor", "gestor", "hash");
    private final Receptionist receptionist = new Receptionist(2L, "Receção", "rececao", "hash");
    private final ExtraService extra = new ExtraService(1L, "Vista mar", ExtraType.ROOM_EXTRA, null);
    private InMemoryExtraPriceRepository repository;
    private HotelFacade facade;

    @BeforeEach
    void setUp() {
        repository = new InMemoryExtraPriceRepository();
        repository.add(extra);
        facade = new HotelFacade(null, null, null, null, null, null, null,
                new ExtraPriceService(repository), null);
    }

    @Test
    @Tag("H4Y-93-AC-1")
    void onSetExtraPrice_saveAmount_whenManagerSetsPrice() {
        facade.setExtraPrice(extra, new BigDecimal("15.00"), manager);
        assertEquals(new BigDecimal("15.00"), repository.findAll().getFirst().getPrice());
    }

    @Test
    @Tag("H4Y-93-AC-3")
    void onListExtraPrices_returnCatalogue_whenManagerRequestsPrices() {
        assertEquals(repository.findAll(), facade.listExtraPrices(manager));
    }

    @Test
    @Tag("H4Y-93-AC-5")
    void onSetExtraPrice_denyAndKeepAmount_whenActorIsReceptionist() {
        assertThrows(AccessDeniedException.class,
                () -> facade.setExtraPrice(extra, BigDecimal.ONE, receptionist));
        assertEquals(extra, repository.findAll().getFirst());
    }

    @Test
    @Tag("H4Y-93-AC-5")
    void onListExtraPrices_deny_whenActorIsReceptionist() {
        assertThrows(AccessDeniedException.class, () -> facade.listExtraPrices(receptionist));
    }
}
