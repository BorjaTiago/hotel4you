package pt.isep.hotel4you.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import pt.isep.hotel4you.domain.MovementType;
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

    /**
     * {@inheritDoc}
     *
     * <p>Like the CHECK of the database, it refuses a stock below zero.
     */
    @Override
    public boolean addMovement(StockMovement movement) {
        Long id = movement.getItem().getId();
        StockItem item = id == null ? null : items.get(id);
        if (item == null) {
            return false;
        }
        int change = movement.getType() == MovementType.ENTRY
                ? movement.getQuantity() : -movement.getQuantity();
        int newStock = item.getStockQuantity() + change;
        if (newStock < 0) {
            throw new NotEnoughStockException();
        }
        items.put(item.getId(), new StockItem(item.getId(), item.getName(), item.getUnit(),
                newStock));
        movements.add(movement);
        return true;
    }

    /**
     * Returns a copy of the saved movements, in the order of saving.
     *
     * @return the movements
     */
    public List<StockMovement> movements() {
        return List.copyOf(movements);
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
