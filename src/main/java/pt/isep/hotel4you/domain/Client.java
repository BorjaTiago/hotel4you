package pt.isep.hotel4you.domain;

import java.time.LocalDate;

/** A hotel client. The receptionist or the manager registers the client before the reservation. */
public class Client {

    private final Long id;
    private final String name;
    private final DocumentType documentType;
    private final String document;
    private final LocalDate birthDate;
    private final String phone;
    private final String nationality;
    private final String email;
    private final String taxNumber;

    /**
     * Creates a client.
     *
     * @param id the identifier, or null if the client is not saved yet
     * @param name the full name
     * @param documentType the type of the ID document
     * @param document the ID document number
     * @param birthDate the date of birth
     * @param phone the phone number
     * @param nationality the ISO 3166 two-letter country code, for example "PT"
     * @param email the email address, or null if the client did not give one
     * @param taxNumber the tax number (NIF), or null if the client did not give one
     */
    public Client(Long id, String name, DocumentType documentType, String document, LocalDate birthDate,
            String phone, String nationality, String email, String taxNumber) {
        this.id = id;
        this.name = name;
        this.documentType = documentType;
        this.document = document;
        this.birthDate = birthDate;
        this.phone = phone;
        this.nationality = nationality;
        this.email = email;
        this.taxNumber = taxNumber;
    }

    /**
     * Returns the identifier.
     *
     * @return the identifier, or null if the client is not saved yet
     */
    public Long getId() {
        return id;
    }

    /**
     * Returns the full name.
     *
     * @return the full name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the type of the ID document.
     *
     * @return the document type
     */
    public DocumentType getDocumentType() {
        return documentType;
    }

    /**
     * Returns the ID document number.
     *
     * @return the document number
     */
    public String getDocument() {
        return document;
    }

    /**
     * Returns the date of birth.
     *
     * @return the date of birth
     */
    public LocalDate getBirthDate() {
        return birthDate;
    }

    /**
     * Returns the phone number.
     *
     * @return the phone number
     */
    public String getPhone() {
        return phone;
    }

    /**
     * Returns the nationality.
     *
     * @return the ISO 3166 two-letter country code
     */
    public String getNationality() {
        return nationality;
    }

    /**
     * Returns the email address.
     *
     * @return the email address, or null if there is none
     */
    public String getEmail() {
        return email;
    }

    /**
     * Returns the tax number (NIF).
     *
     * @return the tax number, or null if there is none
     */
    public String getTaxNumber() {
        return taxNumber;
    }
}
