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

class ReservationServicesServiceTest {
    private static final long BOOKING = 1L;
    private static final long FOOD = 5L;
    private static final long EVENTS = 6L;
    private static final long POOL = 7L;
    private static final long SPA = 8L;
    private static final long CONFERENCE = 9L;

    private final InMemoryReservationRepository reservations = new InMemoryReservationRepository();
    private final InMemoryExtraPriceRepository catalogue = new InMemoryExtraPriceRepository();
    private final ReservationExtrasService extras = new ReservationExtrasService(reservations, catalogue,
            new InMemoryReservationExtrasRepository(reservations));
    private final ReservationServicesService service = new ReservationServicesService(extras);
    private final Receptionist actor = new Receptionist(4L, "Ana", "ana", "hash");

    @BeforeEach
    void setUp() {
        LocalDate arrival = LocalDate.of(2026, 12, 1);
        reservations.add(new Reservation(BOOKING, 2L, arrival, arrival.plusDays(3), ReservationStatus.RESERVED));
        catalogue.add(new ExtraService(1L, "Vista mar", ExtraType.ROOM_EXTRA, new BigDecimal("15.00")));
        catalogue.add(new ExtraService(FOOD, "Comida", ExtraType.PAID_SERVICE, new BigDecimal("20.00")));
        catalogue.add(new ExtraService(EVENTS, "Eventos culturais", ExtraType.PAID_SERVICE, new BigDecimal("10.00")));
        catalogue.add(new ExtraService(POOL, "Piscinas privadas", ExtraType.PAID_SERVICE, new BigDecimal("0.00")));
        catalogue.add(new ExtraService(SPA, "Spa", ExtraType.PAID_SERVICE, new BigDecimal("40.00")));
        catalogue.add(new ExtraService(CONFERENCE, "Salas de conferências", ExtraType.PAID_SERVICE, null));
    }

    @Test
    @Tag("H4Y-108-AC-1")
    void onAddService_storeSpaAndItsPrice_whenReservationIsReserved() {
        service.addService(BOOKING, SPA, actor);
        catalogue.updatePrice(SPA, new BigDecimal("50.00"));

        assertEquals(List.of(new ReservationExtra(SPA, "Spa", new BigDecimal("40.00"))),
                service.listServices(BOOKING, actor));
        assertTrue(extras.listExtras(BOOKING, actor).isEmpty());
    }

    @Test
    @Tag("H4Y-108-AC-2")
    void onEditServices_keepBothChargesAndSupportRemoval_whenSeveralServicesAreChosen() {
        service.addService(BOOKING, FOOD, actor);
        service.addService(BOOKING, EVENTS, actor);

        assertEquals(List.of(new BigDecimal("20.00"), new BigDecimal("10.00")),
                service.listServices(BOOKING, actor).stream().map(ReservationExtra::unitPrice).toList());
        service.removeService(BOOKING, EVENTS, actor);
        assertEquals(List.of(FOOD), service.listServices(BOOKING, actor).stream()
                .map(ReservationExtra::extraPriceId).toList());
    }

    @Test
    @Tag("H4Y-108-AC-3")
    void onAddService_refuseUnsetPriceAndAcceptZero_whenCataloguePricesDiffer() {
        assertThrows(IllegalArgumentException.class, () -> service.addService(BOOKING, CONFERENCE, actor));
        service.addService(BOOKING, POOL, actor);

        assertEquals(List.of(POOL), service.listServices(BOOKING, actor).stream()
                .map(ReservationExtra::extraPriceId).toList());
    }

    @Test
    @Tag("H4Y-108-AC-4")
    void onEditServices_refuseAndKeepServices_whenReservationIsCheckedIn() {
        service.addService(BOOKING, SPA, actor);
        reservations.checkIn(BOOKING);

        assertThrows(IllegalStateException.class, () -> service.addService(BOOKING, FOOD, actor));
        assertThrows(IllegalStateException.class, () -> service.removeService(BOOKING, SPA, actor));
        assertEquals(List.of(SPA), service.listServices(BOOKING, actor).stream()
                .map(ReservationExtra::extraPriceId).toList());
    }

    @Test
    @Tag("H4Y-108-AC-1")
    void onListCatalogue_showOnlyPaidServices_whenCatalogueContainsRoomExtras() {
        assertEquals(List.of(FOOD, EVENTS, POOL, SPA, CONFERENCE), service.listCatalogue(actor).stream()
                .map(ExtraService::getId).toList());
        assertThrows(IllegalArgumentException.class, () -> service.addService(BOOKING, 1L, actor));
    }
}
