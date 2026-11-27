package pt.isep.hotel4you.domain;

/**
 * A card that was just issued, with its plain access code. The application shows the code once.
 * Only the hash of the code is stored.
 *
 * @param card the saved card
 * @param accessCode the plain 6 character code
 */
public record IssuedCard(MagneticCard card, String accessCode) {
}
