package pt.isep.hotel4you.data;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import pt.isep.hotel4you.domain.CardAccessRecord;

/** Keeps card entries in memory. Unit tests use it instead of the database. */
public class InMemoryCardAccessRecordRepository implements CardAccessRecordRepository {

    private final List<CardAccessRecord> records = new ArrayList<>();
    private long nextId = 1;

    @Override
    public CardAccessRecord save(CardAccessRecord record) {
        CardAccessRecord saved = new CardAccessRecord(nextId++, record.getCardId(), record.getRoomId(),
                record.getLocationId(), record.getEntryDateTime());
        records.add(saved);
        return saved;
    }

    @Override
    public List<CardAccessRecord> findByCardId(long cardId) {
        return records.stream()
                .filter(record -> record.getCardId() == cardId)
                .sorted(Comparator.comparing(CardAccessRecord::getEntryDateTime))
                .toList();
    }

    @Override
    public List<CardAccessRecord> findByRoomId(long roomId) {
        return records.stream()
                .filter(record -> Objects.equals(record.getRoomId(), roomId))
                .sorted(Comparator.comparing(CardAccessRecord::getEntryDateTime))
                .toList();
    }

    /**
     * Returns how many entries are stored.
     *
     * @return the number of entries
     */
    public int count() {
        return records.size();
    }
}
