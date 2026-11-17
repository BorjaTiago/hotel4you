package pt.isep.hotel4you.service;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import pt.isep.hotel4you.data.InMemoryExtraPriceRepository;
import pt.isep.hotel4you.domain.ExtraService;
import pt.isep.hotel4you.domain.ExtraType;
import pt.isep.hotel4you.domain.Manager;
import pt.isep.hotel4you.domain.Receptionist;

/** Tests the extra price calls at the GUI facade boundary. */
class ExtraPriceFacadeTest {
    @Test
    @Tag("H4Y-93-AC-1")
    @Tag("H4Y-93-AC-4")
    void onSetExtraPrice_returnServiceMessageAndKeepPrice_whenScreenSubmitsText() {
        var repository = new InMemoryExtraPriceRepository();
        var extra = new ExtraService(1L, "Vista mar", ExtraType.ROOM_EXTRA, null);
        repository.add(extra);
        var facade = new HotelFacade(null, null, null, null, null, null, null,
                new ExtraPriceService(repository));
        var manager = new Manager(1L, "Gestor", "gestor", "hash");

        assertEquals("Preço extra guardado.",
                facade.setExtraPrice(extra, facade.parseExtraPrice("15,00"), manager));
        assertThrows(IllegalArgumentException.class,
                () -> facade.setExtraPrice(extra, facade.parseExtraPrice("abc"), manager));
        assertThrows(IllegalArgumentException.class,
                () -> facade.setExtraPrice(extra, facade.parseExtraPrice("-1"), manager));
        assertEquals(new BigDecimal("15.00"), facade.listExtraPrices(manager).getFirst().getPrice());
    }

    @Test
    @Tag("H4Y-93-AC-1")
    @Tag("H4Y-93-AC-5")
    void onManageExtraPrices_delegateAndCheckActor_whenFacadeIsCalled() {
        var repository = new InMemoryExtraPriceRepository();
        var extra = new ExtraService(1L, "Vista mar", ExtraType.ROOM_EXTRA, null);
        repository.add(extra);
        var facade = new HotelFacade(null, null, null, null, null, null, null,
                new ExtraPriceService(repository));
        var manager = new Manager(1L, "Gestor", "gestor", "hash");
        var receptionist = new Receptionist(2L, "Receção", "rececao", "hash");
        facade.setExtraPrice(extra, new BigDecimal("15.00"), manager);
        assertEquals(new BigDecimal("15.00"), facade.listExtraPrices(manager).getFirst().getPrice());
        assertThrows(AccessDeniedException.class, () -> facade.listExtraPrices(receptionist));
        assertThrows(AccessDeniedException.class,
                () -> facade.setExtraPrice(extra, BigDecimal.ONE, receptionist));
        assertEquals(new BigDecimal("15.00"), repository.findAll().getFirst().getPrice());
    }
}
