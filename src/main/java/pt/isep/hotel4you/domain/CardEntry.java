package pt.isep.hotel4you.domain;

import java.time.LocalDateTime;

/**
 * One line of the card queries: the card, the place it entered and the time of the entry
 * (RF-20, RF-21).
 *
 * @param cardNumber the number of the card
 * @param cardType the type of the card
 * @param place the place, for example "Quarto 202" or "Restaurante"
 * @param entryDateTime the date and time of the entry
 */
public record CardEntry(String cardNumber, CardType cardType, String place, LocalDateTime entryDateTime) {
}
