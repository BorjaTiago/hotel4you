-- v10: add the full client data to customers (H4Y-52, Q-54).
-- Run once, after v8. Do not change v8.
-- The script stops with an error when the customers table does not exist,
-- when the script already ran, or when the table has rows.
-- New NOT NULL columns need an empty table, so remove test clients first.

USE hotel4you;

SET XACT_ABORT ON;

IF OBJECT_ID('customers', 'U') IS NULL
    THROW 50100, 'v10: the customers table does not exist. Run v8 first.', 1;

IF COL_LENGTH('customers', 'document_type') IS NOT NULL
    THROW 50101, 'v10: this script already ran.', 1;

IF EXISTS (SELECT 1 FROM customers)
    THROW 50102, 'v10: the customers table has rows. Remove them before you run this script.', 1;

BEGIN TRANSACTION;

-- The same document number can exist for two document types.
ALTER TABLE customers DROP CONSTRAINT uq_customers_document;

ALTER TABLE customers ADD
    document_type VARCHAR(10) NOT NULL
        CONSTRAINT ck_customers_document_type CHECK (document_type IN ('CC', 'PASSPORT', 'OTHER')),
    birth_date    DATE         NOT NULL,
    phone         VARCHAR(20)  NOT NULL,
    nationality   CHAR(2)      NOT NULL
        CONSTRAINT ck_customers_nationality CHECK (nationality LIKE '[A-Z][A-Z]'),
    email         VARCHAR(100) NULL,
    tax_number    CHAR(9)      NULL
        CONSTRAINT ck_customers_tax_number CHECK (tax_number NOT LIKE '%[^0-9]%'),
    CONSTRAINT uq_customers_document_type_document UNIQUE (document_type, document),
    CONSTRAINT ck_customers_birth_date CHECK (birth_date <= CAST(GETDATE() AS DATE));

COMMIT TRANSACTION;
