package pt.isep.hotel4you.data;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import pt.isep.hotel4you.domain.CardType;
import pt.isep.hotel4you.domain.MagneticCard;

/** Keeps cards in memory. Unit tests use it instead of the database. */
public class InMemoryCardRepository implements CardRepository {

    private final Map<String, MagneticCard> cards = new HashMap<>();
    private long nextId = 1;

    @Override
    public Optional<MagneticCard> findByNumber(String number) {
        return Optional.ofNullable(cards.get(number));
    }

    @Override
    public Optional<MagneticCard> findById(long id) {
        return cards.values().stream()
                .filter(card -> Objects.equals(card.getId(), id))
                .findFirst();
    }

    @Override
    public boolean existsByNumber(String number) {
        return cards.containsKey(number);
    }

    @Override
    public Optional<MagneticCard> findByReservationId(long reservationId) {
        return cards.values().stream()
                .filter(card -> Objects.equals(card.getReservationId(), reservationId))
                .findFirst();
    }

    @Override
    public List<MagneticCard> findByUserId(long userId) {
        return cards.values().stream()
                .filter(card -> Objects.equals(card.getUserId(), userId))
                .sorted(Comparator.comparing(MagneticCard::getId))
                .toList();
    }

    @Override
    public List<MagneticCard> findMasters() {
        return cards.values().stream()
                .filter(card -> card.getType() == CardType.MASTER)
                .sorted(Comparator.comparing(MagneticCard::getId))
                .toList();
    }

    @Override
    public MagneticCard save(MagneticCard card) {
        MagneticCard saved = new MagneticCard(nextId++, card.getNumber(), card.getType(),
                card.getReservationId(), card.getUserId(), card.isActive(), card.getBlockedAt(),
                card.getAccessCodeHash());
        cards.put(saved.getNumber(), saved);
        return saved;
    }

    @Override
    public boolean update(MagneticCard card) {
        if (!cards.containsKey(card.getNumber())) {
            return false;
        }
        cards.put(card.getNumber(), card);
        return true;
    }

    /**
     * Returns how many cards are stored.
     *
     * @return the number of cards
     */
    public int count() {
        return cards.size();
    }
}
