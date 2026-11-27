package pt.isep.hotel4you.service;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import pt.isep.hotel4you.data.InMemoryExtraPriceRepository;
import pt.isep.hotel4you.data.InMemoryReservationExtrasRepository;
import pt.isep.hotel4you.data.InMemoryReservationRepository;
import pt.isep.hotel4you.domain.ExtraService;
import pt.isep.hotel4you.domain.ExtraType;
import pt.isep.hotel4you.domain.Receptionist;
import pt.isep.hotel4you.domain.Reservation;
import pt.isep.hotel4you.domain.ReservationExtra;
import pt.isep.hotel4you.domain.ReservationStatus;

class ReservationExtrasServiceTest {
    private static final long BOOKING = 1L;
    private static final long SEA = 1L;
    private static final long MOUNTAIN = 2L;
    private static final long DRINKS = 3L;
    private static final long LAUNDRY = 4L;
    private static final long SPA = 5L;

    private final InMemoryReservationRepository reservations = new InMemoryReservationRepository();
    private final InMemoryExtraPriceRepository catalogue = new InMemoryExtraPriceRepository();
    private final InMemoryReservationExtrasRepository extras = new InMemoryReservationExtrasRepository(reservations);
    private final ReservationExtrasService service = new ReservationExtrasService(reservations, catalogue, extras);
    private final Receptionist actor = new Receptionist(4L, "Ana", "ana", "hash");

    @BeforeEach
    void setUp() {
        LocalDate arrival = LocalDate.of(2026, 12, 1);
        reservations.add(new Reservation(BOOKING, 2L, arrival, arrival.plusDays(3), ReservationStatus.RESERVED));
        catalogue.add(new ExtraService(SEA, "Vista mar", ExtraType.ROOM_EXTRA, new BigDecimal("15.00")));
        catalogue.add(new ExtraService(MOUNTAIN, "Vista montanha", ExtraType.ROOM_EXTRA, new BigDecimal("12.00")));
        catalogue.add(new ExtraService(DRINKS, "Bebidas", ExtraType.ROOM_EXTRA, new BigDecimal("7.00")));
        catalogue.add(new ExtraService(LAUNDRY, "Lavandaria", ExtraType.ROOM_EXTRA, new BigDecimal("5.00")));
        catalogue.add(new ExtraService(SPA, "Spa", ExtraType.PAID_SERVICE, new BigDecimal("40.00")));
    }

    @Test
    @Tag("H4Y-107-AC-1")
    void onAddExtra_saveSeaViewAndKeepPrice_whenCataloguePriceChanges() {
        service.addExtra(BOOKING, SEA, actor);
        catalogue.updatePrice(SEA, new BigDecimal("20.00"));
        service.addExtra(BOOKING, SEA, actor);

        assertEquals(List.of(new ReservationExtra(SEA, "Vista mar", new BigDecimal("15.00"))),
                service.listExtras(BOOKING, actor));
    }

    @Test
    @Tag("H4Y-107-AC-2")
    void onAddExtra_keepBothCharges_whenDrinksAndLaundryAreAdded() {
        service.addExtra(BOOKING, DRINKS, actor);
        service.addExtra(BOOKING, LAUNDRY, actor);

        assertEquals(List.of(new BigDecimal("7.00"), new BigDecimal("5.00")),
                service.listExtras(BOOKING, actor).stream().map(ReservationExtra::unitPrice).toList());
    }

    @Test
    @Tag("H4Y-107-AC-3")
    void onRemoveExtra_dropCharge_whenExtraIsRemoved() {
        service.addExtra(BOOKING, SEA, actor);
        service.removeExtra(BOOKING, SEA, actor);

        assertTrue(service.listExtras(BOOKING, actor).isEmpty());
    }

    @Test
    @Tag("H4Y-107-AC-4")
    void onAddExtra_refuseWithoutChangingView_whenNewViewHasNoPrice() {
        service.addExtra(BOOKING, SEA, actor);
        catalogue.updatePrice(MOUNTAIN, null);

        var error = assertThrows(IllegalArgumentException.class, () -> service.addExtra(BOOKING, MOUNTAIN, actor));
        assertEquals("Defina o preço de Vista montanha antes de o adicionar à reserva.", error.getMessage());
        assertEquals(List.of(SEA), service.listExtras(BOOKING, actor).stream().map(ReservationExtra::extraPriceId).toList());
    }

    @Test
    @Tag("H4Y-107-AC-5")
    void onAddExtra_refuse_whenReservationIsCancelled() {
        reservations.cancel(BOOKING);

        assertThrows(IllegalStateException.class, () -> service.addExtra(BOOKING, DRINKS, actor));
        assertTrue(service.listExtras(BOOKING, actor).isEmpty());
    }

    @Test
    @Tag("H4Y-107-AC-6")
    void onAddExtra_replacePreviousView_whenMountainViewIsChosen() {
        service.addExtra(BOOKING, SEA, actor);
        service.addExtra(BOOKING, DRINKS, actor);
        service.addExtra(BOOKING, MOUNTAIN, actor);

        assertEquals(List.of(MOUNTAIN, DRINKS), service.listExtras(BOOKING, actor).stream()
                .map(ReservationExtra::extraPriceId).toList());
    }

    @Test
    @Tag("H4Y-107-AC-7")
    void onEditExtras_refuseWithoutLosingSelections_whenReservationIsCheckedIn() {
        service.addExtra(BOOKING, SEA, actor);
        reservations.checkIn(BOOKING);

        assertThrows(IllegalStateException.class, () -> service.addExtra(BOOKING, DRINKS, actor));
        assertThrows(IllegalStateException.class, () -> service.removeExtra(BOOKING, SEA, actor));
        assertEquals(List.of(SEA), service.listExtras(BOOKING, actor).stream().map(ReservationExtra::extraPriceId).toList());
    }

    @Test
    @Tag("H4Y-107-AC-1")
    void onAddExtra_refuse_whenActorDoesNotManageReservationsOrItemIsPaidService() {
        assertThrows(AccessDeniedException.class, () -> service.addExtra(BOOKING, SEA, null));
        assertThrows(IllegalArgumentException.class, () -> service.addExtra(BOOKING, SPA, actor));
        assertTrue(service.listExtras(BOOKING, actor).isEmpty());
    }

    @Test
    @Tag("H4Y-107-AC-7")
    void onAddExtra_refuse_whenCheckInWinsDuringSave() {
        extras.checkInBeforeNextEdit = true;

        assertThrows(IllegalStateException.class, () -> service.addExtra(BOOKING, SEA, actor));
        assertTrue(service.listExtras(BOOKING, actor).isEmpty());
    }
}
