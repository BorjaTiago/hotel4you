-- v19: create the feedback table (H4Y-111, RF-27, Q-18).
-- The reception records a suggestion or a complaint of a client. The manager receives it.
-- customer_id NULL = no client is known. Run after v8. It needs v8 (customers).
-- You can run it again: it skips what already exists.

USE hotel4you;

IF OBJECT_ID('dbo.customers', 'U') IS NULL
    THROW 50005, 'Execute primeiro o script v8 (clientes).', 1;

IF OBJECT_ID('dbo.feedback', 'U') IS NULL
    CREATE TABLE dbo.feedback (
        id          INT           IDENTITY NOT NULL,
        customer_id INT           NULL,
        type        VARCHAR(10)   NOT NULL,
        description NVARCHAR(500) NOT NULL,
        created_at  DATETIME2(0)  NOT NULL,
        CONSTRAINT pk_feedback PRIMARY KEY (id),
        CONSTRAINT fk_feedback_customer FOREIGN KEY (customer_id) REFERENCES dbo.customers(id),
        CONSTRAINT ck_feedback_type CHECK (type IN ('suggestion', 'complaint')),
        CONSTRAINT ck_feedback_description CHECK (LEN(description) > 0)
    );
