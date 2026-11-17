package pt.isep.hotel4you.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import pt.isep.hotel4you.domain.StockItem;
import pt.isep.hotel4you.domain.StockMovement;

/** Keeps stock items in memory. Unit tests use it instead of the database. */
public class InMemoryItemRepository implements ItemRepository {

    private final Map<Long, StockItem> items = new HashMap<>();
    private final List<StockMovement> movements = new ArrayList<>();
    private long nextId = 1;

    @Override
    public Optional<StockItem> findById(long id) {
        return Optional.ofNullable(items.get(id));
    }

    @Override
    public Optional<StockItem> findByName(String name) {
        return items.values().stream()
                .filter(item -> item.getName().equalsIgnoreCase(name))
                .findFirst();
    }

    @Override
    public List<StockItem> findAll() {
        return new ArrayList<>(items.values());
    }

    @Override
    public StockItem save(StockItem item) {
        StockItem saved = new StockItem(nextId++, item.getName(), item.getUnit(),
                item.getStockQuantity());
        items.put(saved.getId(), saved);
        return saved;
    }

    @Override
    public boolean update(StockItem item) {
        if (!items.containsKey(item.getId())) {
            return false;
        }
        items.put(item.getId(), item);
        return true;
    }

    @Override
    public boolean addMovement(StockMovement movement) {
        StockItem item = items.get(movement.getItem().getId());
        if (item == null) {
            return false;
        }
        items.put(item.getId(), new StockItem(item.getId(), item.getName(), item.getUnit(),
                item.getStockQuantity() + movement.getQuantity()));
        movements.add(movement);
        return true;
    }

    /**
     * Returns the saved movements, in the order of saving.
     *
     * @return the movements
     */
    public List<StockMovement> movements() {
        return movements;
    }

    /**
     * Returns how many items are stored.
     *
     * @return the number of items
     */
    public int count() {
        return items.size();
    }
}
