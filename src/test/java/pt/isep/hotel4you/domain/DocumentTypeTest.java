package pt.isep.hotel4you.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/** Tests the ID document types of a client. */
class DocumentTypeTest {

    @Test
    @Tag("H4Y-52-AC-1")
    void onFromDatabase_returnType_whenValueIsStored() {
        assertEquals(DocumentType.CC, DocumentType.fromDatabase("CC"));
        assertEquals(DocumentType.PASSPORT, DocumentType.fromDatabase("passport"));
        assertEquals(DocumentType.OTHER, DocumentType.fromDatabase("OTHER"));
    }

    @Test
    @Tag("H4Y-52-AC-1")
    void onFromDatabase_throw_whenValueIsUnknown() {
        assertThrows(IllegalArgumentException.class, () -> DocumentType.fromDatabase("BI"));
    }

    @Test
    @Tag("H4Y-52-AC-1")
    void onDisplayName_returnPortugueseName_whenTypeIsShown() {
        assertEquals("Cartão de Cidadão", DocumentType.CC.displayName());
        assertEquals("Passaporte", DocumentType.PASSPORT.displayName());
        assertEquals("Outro", DocumentType.OTHER.displayName());
    }
}
