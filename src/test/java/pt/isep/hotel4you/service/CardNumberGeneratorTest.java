package pt.isep.hotel4you.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import pt.isep.hotel4you.data.InMemoryCardRepository;
import pt.isep.hotel4you.domain.CardType;
import pt.isep.hotel4you.domain.MagneticCard;

/** Tests the generator of unique card numbers. */
class CardNumberGeneratorTest {

    private final InMemoryCardRepository repository = new InMemoryCardRepository();

    @Test
    @Tag("H4Y-109-AC-2")
    void onNext_returnUniqueNumbers_whenGeneratedManyTimes() {
        CardNumberGenerator generator = new CardNumberGenerator(repository);
        Set<String> numbers = new HashSet<>();

        for (int i = 0; i < 10_000; i++) {
            assertTrue(numbers.add(generator.next()));
        }
    }

    @Test
    @Tag("H4Y-109-AC-2")
    void onNext_skipRepeatedValue_whenSourceRepeats() {
        AtomicLong source = new AtomicLong();
        long[] values = {5, 5, 5, 6};
        CardNumberGenerator generator = new CardNumberGenerator(repository, () -> values[(int) source.getAndIncrement()]);

        String first = generator.next();
        String second = generator.next();

        assertNotEquals(first, second);
    }

    @Test
    @Tag("H4Y-109-AC-2")
    void onNext_skipSavedNumber_whenCardAlreadyUsesIt() {
        CardNumberGenerator probe = new CardNumberGenerator(new InMemoryCardRepository(), () -> 5L);
        String taken = probe.next();
        repository.save(new MagneticCard(null, taken, CardType.CLIENT, 1L, null, true, null));
        long[] values = {5, 6};
        int[] index = {0};
        CardNumberGenerator generator = new CardNumberGenerator(repository, () -> values[index[0]++]);

        assertNotEquals(taken, generator.next());
    }

    @Test
    @Tag("H4Y-109-AC-2")
    void onNext_returnDigitsOnly_whenNumberIsGenerated() {
        CardNumberGenerator generator = new CardNumberGenerator(repository, () -> 42L);

        String number = generator.next();

        assertEquals(10, number.length());
        assertTrue(number.chars().allMatch(Character::isDigit));
        assertEquals("0000000042", number);
    }

    @Test
    @Tag("H4Y-109-AC-2")
    void onNext_returnNumberThatFitsDatabase_whenSourceIsLarge() {
        CardNumberGenerator generator = new CardNumberGenerator(repository, () -> Long.MAX_VALUE);

        assertTrue(generator.next().length() <= 20);
    }
}
