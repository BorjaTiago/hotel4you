package pt.isep.hotel4you.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/** Tests the generator of the 6 character card codes. */
class AccessCodeGeneratorTest {

    @Test
    @Tag("H4Y-109-AC-3")
    void onNext_returnSixAllowedCharacters_whenGeneratedManyTimes() {
        AccessCodeGenerator generator = new AccessCodeGenerator();

        for (int i = 0; i < 1_000; i++) {
            String code = generator.next();
            assertEquals(6, code.length());
            assertTrue(code.matches("[A-HJ-NP-Z2-9]{6}"), code);
        }
    }

    @Test
    @Tag("H4Y-109-AC-3")
    void onNext_neverUseConfusingCharacters_whenEveryIndexIsUsed() {
        int[] index = {0};
        AccessCodeGenerator generator = new AccessCodeGenerator(bound -> index[0]++ % bound);
        StringBuilder all = new StringBuilder();

        for (int i = 0; i < 6; i++) {
            all.append(generator.next());
        }

        String text = all.toString();
        assertTrue(text.chars().noneMatch(c -> c == '0' || c == 'O' || c == '1' || c == 'I'), text);
        assertEquals(32, text.chars().distinct().count());
    }
}
