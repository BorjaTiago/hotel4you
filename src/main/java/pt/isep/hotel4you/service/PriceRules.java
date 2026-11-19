package pt.isep.hotel4you.service;

import java.math.BigDecimal;

/** Shared parsing and DECIMAL(8,2) rules for room base prices and extra prices. */
final class PriceRules {
    private static final BigDecimal MAX_PRICE = new BigDecimal("999999.99");
    private static final int SCALE = 2;
    private static final int MAX_TEXT_LENGTH = 12;

    private PriceRules() {
    }

    /** Parses a short plain decimal number, with a comma or point as decimal mark. */
    static BigDecimal parse(String text) {
        String number = text == null ? "" : text.trim();
        if (number.length() > MAX_TEXT_LENGTH || !number.matches("-?[0-9]+([.,][0-9]+)?")) {
            throw new IllegalArgumentException("O preço tem de ser um número.");
        }
        return new BigDecimal(number.replace(',', '.'));
    }

    /** Validates a price and returns it with the database scale. */
    static BigDecimal normalize(BigDecimal price) {
        if (price == null || price.signum() < 0 || price.scale() > SCALE
                || price.compareTo(MAX_PRICE) > 0) {
            throw new IllegalArgumentException(
                    "O preço tem de estar entre 0,00 e 999999,99, com duas casas decimais.");
        }
        return price.setScale(SCALE);
    }
}
