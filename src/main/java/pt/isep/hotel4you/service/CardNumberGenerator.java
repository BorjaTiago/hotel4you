package pt.isep.hotel4you.service;

import java.security.SecureRandom;
import java.util.HashSet;
import java.util.Set;
import java.util.function.LongSupplier;
import pt.isep.hotel4you.data.CardRepository;

/** Makes card numbers. A number never repeats: not one made before, and not one a saved card has. */
public class CardNumberGenerator {

    private static final int DIGITS = 10;
    private static final long RANGE = 10_000_000_000L;

    private final CardRepository cardRepository;
    private final LongSupplier source;
    private final Set<String> issued = new HashSet<>();

    /**
     * Creates a generator that uses a secure random source.
     *
     * @param cardRepository the repository that tells which numbers saved cards have
     */
    public CardNumberGenerator(CardRepository cardRepository) {
        this(cardRepository, new SecureRandom()::nextLong);
    }

    /**
     * Creates a generator with its own source of values. Tests use it to control the values.
     *
     * @param cardRepository the repository that tells which numbers saved cards have
     * @param source the source of raw values, each value becomes a number of 10 digits
     */
    public CardNumberGenerator(CardRepository cardRepository, LongSupplier source) {
        this.cardRepository = cardRepository;
        this.source = source;
    }

    /**
     * Makes a new card number.
     *
     * @return a number of 10 digits that no earlier call returned and no saved card has
     * @throws IllegalStateException if the database is not available
     */
    public synchronized String next() {
        String number;
        do {
            number = String.format("%0" + DIGITS + "d", Math.floorMod(source.getAsLong(), RANGE));
        } while (issued.contains(number) || cardRepository.existsByNumber(number));
        issued.add(number);
        return number;
    }
}
