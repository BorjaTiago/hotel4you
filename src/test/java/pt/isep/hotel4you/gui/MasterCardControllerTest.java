package pt.isep.hotel4you.gui;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import pt.isep.hotel4you.domain.CardType;
import pt.isep.hotel4you.domain.IssuedCard;
import pt.isep.hotel4you.domain.MagneticCard;

/** Tests the texts of the master card screen. */
class MasterCardControllerTest {

    private static final LocalDateTime BLOCKED_AT = LocalDateTime.of(2026, 11, 24, 10, 5);

    private static MagneticCard card(boolean active, LocalDateTime blockedAt) {
        return new MagneticCard(1L, "90001", CardType.MASTER, null, 1L, active, blockedAt);
    }

    @Test
    @Tag("H4Y-153-AC-6")
    void onStateText_showActive_whenCardWorks() {
        assertEquals("Ativo", MasterCardController.stateText(card(true, null)));
    }

    @Test
    @Tag("H4Y-153-AC-1")
    void onStateText_showBlocked_whenCardIsBlocked() {
        assertEquals("Bloqueado", MasterCardController.stateText(card(false, BLOCKED_AT)));
    }

    @Test
    @Tag("H4Y-153-AC-2")
    void onBlockedAtText_showDayAndTime_whenCardIsBlocked() {
        assertEquals("24/11/2026 10:05", MasterCardController.blockedAtText(card(false, BLOCKED_AT)));
    }

    @Test
    @Tag("H4Y-153-AC-2")
    void onBlockedAtText_showEmpty_whenCardIsNotBlocked() {
        assertEquals("", MasterCardController.blockedAtText(card(true, null)));
    }

    @Test
    @Tag("H4Y-153-AC-6")
    void onBlockedMessage_showCardNumber_whenCardIsBlocked() {
        assertEquals("Cartão mestre 90001 bloqueado.",
                MasterCardController.blockedMessage(card(false, BLOCKED_AT)));
    }

    @Test
    @Tag("H4Y-154-AC-6")
    void onIssuedText_showNumberAndCodeOnce_whenNewCardIssued() {
        IssuedCard issued = new IssuedCard(new MagneticCard(2L, "4821937650", CardType.MASTER, null, 1L, true, null),
                "K7MP2Q");

        assertEquals("""
                Número do novo cartão mestre: 4821937650
                Código: K7MP2Q
                Guarde o código agora. Não volta a ser mostrado.""", MasterCardController.issuedText(issued));
    }

    @Test
    @Tag("H4Y-154-AC-6")
    void onIssuedMessage_showOnlyNumber_whenNewCardIssued() {
        MagneticCard card = new MagneticCard(2L, "4821937650", CardType.MASTER, null, 1L, true, null);

        assertEquals("Novo cartão mestre 4821937650 emitido.", MasterCardController.issuedMessage(card));
    }
}
