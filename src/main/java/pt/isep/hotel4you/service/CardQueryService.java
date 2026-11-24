package pt.isep.hotel4you.service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import pt.isep.hotel4you.data.CardRepository;
import pt.isep.hotel4you.data.LocationRepository;
import pt.isep.hotel4you.data.RoomRepository;
import pt.isep.hotel4you.domain.CardAccessRecord;
import pt.isep.hotel4you.domain.CardEntry;
import pt.isep.hotel4you.domain.Location;
import pt.isep.hotel4you.domain.MagneticCard;
import pt.isep.hotel4you.domain.Room;
import pt.isep.hotel4you.domain.User;

/**
 * Reads the entries of the cards for the manager screens (RF-20, RF-21). It uses the queries of
 * {@link CardAccessService} and adds the card number and the name of each place. Blocked cards
 * can still be queried.
 */
public class CardQueryService {

    private final CardAccessService cardAccessService;
    private final CardRepository cardRepository;
    private final RoomRepository roomRepository;
    private final LocationRepository locationRepository;

    /**
     * Creates the service.
     *
     * @param cardAccessService reads the entries and checks the permission
     * @param cardRepository reads the cards
     * @param roomRepository reads the room numbers
     * @param locationRepository reads the zone names
     */
    public CardQueryService(CardAccessService cardAccessService, CardRepository cardRepository,
            RoomRepository roomRepository, LocationRepository locationRepository) {
        this.cardAccessService = cardAccessService;
        this.cardRepository = cardRepository;
        this.roomRepository = roomRepository;
        this.locationRepository = locationRepository;
    }

    /**
     * Lists the places a card entered, the oldest first (RF-20).
     *
     * @param number the card number
     * @param actor the logged-in user
     * @return one line per entry, or an empty list if the card was never used
     * @throws AccessDeniedException if the user is not a manager
     * @throws CardNotFoundException if no card has this number
     * @throws IllegalStateException if the database is not available
     */
    public List<CardEntry> getPlacesByCard(String number, User actor) {
        List<CardAccessRecord> records = cardAccessService.getPlacesByCard(number, actor);
        MagneticCard card = cardRepository.findByNumber(number.trim()).orElseThrow(CardNotFoundException::new);
        Map<Long, Room> rooms = roomRepository.findAll().stream()
                .collect(Collectors.toMap(Room::getId, Function.identity()));
        return records.stream()
                .map(record -> new CardEntry(card.getNumber(), card.getType(), placeName(record, rooms),
                        record.getEntryDateTime()))
                .toList();
    }

    private String placeName(CardAccessRecord record, Map<Long, Room> rooms) {
        if (record.getRoomId() != null) {
            Room room = rooms.get(record.getRoomId());
            return room == null ? "Quarto removido" : "Quarto " + room.getNumber();
        }
        return locationRepository.findById(record.getLocationId())
                .map(Location::getName)
                .orElse("Local removido");
    }
}
