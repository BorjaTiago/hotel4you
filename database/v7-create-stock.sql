-- v7: create the items table for the stock catalogue.
-- Run after v1. You can run it again: it skips what already exists.

USE hotel4you;

-- Table: items
IF OBJECT_ID('items', 'U') IS NULL
    CREATE TABLE items (
        id             SMALLINT     IDENTITY NOT NULL,
        name           VARCHAR(100) NOT NULL,
        unit           VARCHAR(20)  NOT NULL,
        stock_quantity INT          NOT NULL CONSTRAINT df_items_stock_quantity DEFAULT 0,
        CONSTRAINT pk_items               PRIMARY KEY (id),
        CONSTRAINT uq_items_name          UNIQUE (name),
        CONSTRAINT ck_items_stock_quantity CHECK (stock_quantity >= 0)
    );
