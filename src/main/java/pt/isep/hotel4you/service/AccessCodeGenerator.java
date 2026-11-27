package pt.isep.hotel4you.service;

import java.security.SecureRandom;
import java.util.function.IntUnaryOperator;

/**
 * Makes the 6 character access code of a card. The code uses upper case letters and digits
 * without 0, O, 1 and I, so the client does not confuse them.
 */
public class AccessCodeGenerator {

    /** The characters a code can use. */
    public static final String ALPHABET = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789";
    /** The length of a code. */
    public static final int LENGTH = 6;

    private final IntUnaryOperator random;

    /** Creates a generator that uses a secure random source. */
    public AccessCodeGenerator() {
        this(new SecureRandom()::nextInt);
    }

    /**
     * Creates a generator with a given random source.
     *
     * @param random gives a number from 0 (included) to the bound (excluded)
     */
    public AccessCodeGenerator(IntUnaryOperator random) {
        this.random = random;
    }

    /**
     * Makes a new code.
     *
     * @return a code of 6 allowed characters
     */
    public String next() {
        StringBuilder code = new StringBuilder(LENGTH);
        for (int i = 0; i < LENGTH; i++) {
            code.append(ALPHABET.charAt(random.applyAsInt(ALPHABET.length())));
        }
        return code.toString();
    }
}
