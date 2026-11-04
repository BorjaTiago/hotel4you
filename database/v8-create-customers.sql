-- v8: create customers. The receptionist registers the clients (H4Y-52).
-- Run after v7 (stock). The document is unique, so the same client is not saved twice.

USE hotel4you;

IF OBJECT_ID('customers', 'U') IS NULL
    CREATE TABLE customers (
        id       INT IDENTITY NOT NULL,
        name     NVARCHAR(100) NOT NULL,
        document VARCHAR(20) NOT NULL,
        CONSTRAINT pk_customers PRIMARY KEY (id),
        CONSTRAINT uq_customers_document UNIQUE (document)
    );
