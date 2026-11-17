-- v12: create the stock_movements table for the stock entries (H4Y-90, RF-29).
-- Run after v11. It needs the items table (v7) and the users table (v2).
-- You can run it again: it skips what already exists.
-- The import_id column and its link to stock_imports come with the supplier file import (RF-28).

USE hotel4you;

-- Table: stock_movements
IF OBJECT_ID('stock_movements', 'U') IS NULL
    CREATE TABLE stock_movements (
        id          INT          IDENTITY NOT NULL,
        item_id     SMALLINT     NOT NULL,
        type        VARCHAR(5)   NOT NULL,
        exit_reason VARCHAR(10)  NULL,
        quantity    SMALLINT     NOT NULL,
        moved_at    DATETIME2(0) NOT NULL,
        user_id     SMALLINT     NOT NULL,
        CONSTRAINT pk_stock_movements             PRIMARY KEY (id),
        CONSTRAINT fk_stock_movements_item        FOREIGN KEY (item_id) REFERENCES items (id),
        CONSTRAINT fk_stock_movements_user        FOREIGN KEY (user_id) REFERENCES users (id),
        CONSTRAINT ck_stock_movements_type        CHECK (type IN ('in', 'use', 'out')),
        CONSTRAINT ck_stock_movements_quantity    CHECK (quantity > 0),
        CONSTRAINT ck_stock_movements_exit_reason CHECK (exit_reason IN ('discard', 'return', 'loss')),
        CONSTRAINT ck_stock_movements_exit_type   CHECK (type = 'out' OR exit_reason IS NULL)
    );

-- Index: the movements of one item in time order
IF NOT EXISTS (SELECT 1 FROM sys.indexes WHERE name = 'ix_stock_movements_item_time')
    CREATE INDEX ix_stock_movements_item_time ON stock_movements (item_id, moved_at);
