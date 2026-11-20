package pt.isep.hotel4you.data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import pt.isep.hotel4you.domain.ExtraService;

/** Keeps the extra price catalogue in memory for unit tests. */
public class InMemoryExtraPriceRepository implements ExtraPriceRepository {
    private final List<ExtraService> extras = new ArrayList<>();

    @Override
    public List<ExtraService> findAll() {
        return extras.stream().sorted(Comparator.comparing(ExtraService::getId)).toList();
    }

    @Override
    public boolean updatePrice(long id, BigDecimal price) {
        for (int i = 0; i < extras.size(); i++) {
            ExtraService extra = extras.get(i);
            if (extra.getId() == id) {
                extras.set(i, new ExtraService(id, extra.getName(), extra.getType(), price));
                return true;
            }
        }
        return false;
    }

    /**
     * Adds test catalogue data.
     * @param extra the extra to add
     * @throws IllegalArgumentException if the entry has no saved identifier
     */
    public void add(ExtraService extra) {
        if (extra == null || extra.getId() == null) {
            throw new IllegalArgumentException("A catalogue entry must have an id.");
        }
        extras.add(extra);
    }
}
