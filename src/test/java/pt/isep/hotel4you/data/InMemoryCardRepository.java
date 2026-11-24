package pt.isep.hotel4you.data;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
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
    public boolean existsByNumber(String number) {
        return cards.containsKey(number);
    }

    @Override
    public MagneticCard save(MagneticCard card) {
        MagneticCard saved = new MagneticCard(nextId++, card.getNumber(), card.getType(),
                card.getReservationId(), card.getUserId(), card.isActive(), card.getBlockedAt());
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
