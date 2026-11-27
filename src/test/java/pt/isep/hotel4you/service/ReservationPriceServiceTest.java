package pt.isep.hotel4you.service;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import pt.isep.hotel4you.data.InMemoryExtraPriceRepository;
import pt.isep.hotel4you.data.InMemoryReservationExtrasRepository;
import pt.isep.hotel4you.data.InMemoryReservationRepository;
import pt.isep.hotel4you.data.InMemoryRoomRepository;
import pt.isep.hotel4you.data.InMemoryRoomTypeRepository;
import pt.isep.hotel4you.domain.ExtraService;
import pt.isep.hotel4you.domain.ExtraType;
import pt.isep.hotel4you.domain.Receptionist;
import pt.isep.hotel4you.domain.Reservation;
import pt.isep.hotel4you.domain.ReservationStatus;
import pt.isep.hotel4you.domain.Room;
import pt.isep.hotel4you.domain.RoomType;

class ReservationPriceServiceTest {
    private static final long THREE_NIGHTS = 1L;
    private static final long TWO_NIGHTS = 2L;
    private static final long SEA = 1L;
    private static final long LAUNDRY = 4L;
    private static final long SPA = 8L;
    private final InMemoryReservationRepository reservations = new InMemoryReservationRepository();
    private final InMemoryRoomRepository rooms = new InMemoryRoomRepository();
    private final InMemoryRoomTypeRepository types = new InMemoryRoomTypeRepository();
    private final InMemoryExtraPriceRepository catalogue = new InMemoryExtraPriceRepository();
    private final InMemoryReservationExtrasRepository store = new InMemoryReservationExtrasRepository(reservations);
    private final ReservationExtrasService extras = new ReservationExtrasService(reservations, catalogue, store);
    private final ReservationServicesService services = new ReservationServicesService(extras);
    private final ReservationPriceService pricing = new ReservationPriceService(reservations, rooms, types, store);
    private final Receptionist actor = new Receptionist(4L, "Ana", "ana", "hash");

    @BeforeEach
    void setUp() {
        rooms.addFloor(2);
        rooms.addRoomType("duplo");
        Room room = rooms.save(new Room(null, 201, 2, "duplo"), 10);
        types.add(new RoomType(1L, "duplo", 2, new BigDecimal("80.00")));
        LocalDate arrival = LocalDate.of(2026, 12, 1);
        reservations.add(new Reservation(THREE_NIGHTS, room.getId(), arrival, arrival.plusDays(3),
                ReservationStatus.RESERVED));
        reservations.add(new Reservation(TWO_NIGHTS, room.getId(), arrival, arrival.plusDays(2),
                ReservationStatus.RESERVED));
        catalogue.add(new ExtraService(SEA, "Vista mar", ExtraType.ROOM_EXTRA, new BigDecimal("15.00")));
        catalogue.add(new ExtraService(LAUNDRY, "Lavandaria", ExtraType.ROOM_EXTRA, new BigDecimal("5.00")));
        catalogue.add(new ExtraService(SPA, "Spa", ExtraType.PAID_SERVICE, new BigDecimal("40.00")));
    }

    @Test
    @Tag("H4Y-130-AC-1")
    void onQuote_returnBaseTimesNights_whenNoExtrasAreIncluded() {
        var price = pricing.quote(THREE_NIGHTS, actor);

        assertEquals(3, price.nights());
        assertEquals(new BigDecimal("240.00"), price.total());
    }

    @Test
    @Tag("H4Y-130-AC-2")
    void onQuote_includeSavedPrices_whenRoomExtrasAreIncluded() {
        extras.addExtra(THREE_NIGHTS, SEA, actor);
        extras.addExtra(THREE_NIGHTS, LAUNDRY, actor);
        catalogue.updatePrice(SEA, new BigDecimal("99.00"));

        var price = pricing.quote(THREE_NIGHTS, actor);
        assertEquals(new BigDecimal("100.00"), price.pricePerNight());
        assertEquals(new BigDecimal("300.00"), price.total());
    }

    @Test
    @Tag("H4Y-130-AC-3")
    void onQuote_includeServiceTimesNights_whenSpaIsIncluded() {
        services.addService(TWO_NIGHTS, SPA, actor);

        assertEquals(new BigDecimal("240.00"), pricing.quote(TWO_NIGHTS, actor).total());
    }

    @Test
    @Tag("H4Y-130-AC-4")
    void onQuote_recalculate_whenAnExtraIsRemoved() {
        extras.addExtra(THREE_NIGHTS, SEA, actor);
        assertEquals(new BigDecimal("285.00"), pricing.quote(THREE_NIGHTS, actor).total());
        extras.removeExtra(THREE_NIGHTS, SEA, actor);

        assertEquals(new BigDecimal("240.00"), pricing.quote(THREE_NIGHTS, actor).total());
    }

    @Test
    @Tag("H4Y-130-AC-5")
    void onQuote_leaveTotalUnavailable_whenBasePriceIsMissing() {
        types.updateBasePrice(1L, null);
        extras.addExtra(THREE_NIGHTS, SEA, actor);

        var price = pricing.quote(THREE_NIGHTS, actor);
        assertNull(price.pricePerNight());
        assertNull(price.total());
        assertEquals(new BigDecimal("15.00"), price.extrasPerNight());
    }

    @Test
    @Tag("H4Y-130-AC-6")
    void onQuote_readAllPrices_whenBothGroupsAreIncluded() {
        extras.addExtra(THREE_NIGHTS, SEA, actor);
        services.addService(THREE_NIGHTS, SPA, actor);
        reservations.checkIn(THREE_NIGHTS);

        var price = pricing.quote(THREE_NIGHTS, actor);
        assertEquals(new BigDecimal("80.00"), price.basePrice());
        assertEquals(new BigDecimal("15.00"), price.extrasPerNight());
        assertEquals(new BigDecimal("40.00"), price.servicesPerNight());
        assertEquals(new BigDecimal("405.00"), price.total());
        assertThrows(AccessDeniedException.class, () -> pricing.quote(THREE_NIGHTS, null));
        assertThrows(ReservationNotFoundException.class, () -> pricing.quote(99L, actor));
    }
}
