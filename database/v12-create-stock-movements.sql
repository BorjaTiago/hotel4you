-- v12: create the stock_movements table for the stock entries (H4Y-90, RF-29).
-- Run after v7 (items) and v2 (users). You can run it again: it skips what already exists.

USE hotel4you;

-- Table: stock_movements
IF OBJECT_ID('stock_movements', 'U') IS NULL
    CREATE TABLE stock_movements (
        id       INT          IDENTITY NOT NULL,
        item_id  SMALLINT     NOT NULL,
        type     VARCHAR(5)   NOT NULL,
        quantity SMALLINT     NOT NULL,
        moved_at DATETIME2(0) NOT NULL,
        user_id  SMALLINT     NOT NULL,
        CONSTRAINT pk_stock_movements          PRIMARY KEY (id),
        CONSTRAINT fk_stock_movements_item     FOREIGN KEY (item_id) REFERENCES items (id),
        CONSTRAINT fk_stock_movements_user     FOREIGN KEY (user_id) REFERENCES users (id),
        CONSTRAINT ck_stock_movements_type     CHECK (type IN ('in', 'use', 'out')),
        CONSTRAINT ck_stock_movements_quantity CHECK (quantity > 0)
    );
