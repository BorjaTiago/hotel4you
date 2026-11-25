-- v15: add the exit reason to stock_movements, for the stock use and exit (H4Y-106, RF-29, Q-34).
-- Run after v12. Do not change v12.
-- You can run it again: it skips what already exists. An old database can already have
-- exit_reason and ck_stock_movements_exit_reason; the script then adds only the missing rule.
-- The new column allows NULL, so old rows stay valid: an entry ('in') has no reason.
-- Each GO starts a new batch, so the next batch sees the new column.

USE hotel4you;

IF OBJECT_ID('stock_movements', 'U') IS NULL
    THROW 50150, 'v15: the stock_movements table does not exist. Run v12 first.', 1;
GO

IF COL_LENGTH('stock_movements', 'exit_reason') IS NULL
    ALTER TABLE stock_movements ADD exit_reason VARCHAR(10) NULL;
GO

IF OBJECT_ID('ck_stock_movements_exit_reason', 'C') IS NULL
    ALTER TABLE stock_movements ADD CONSTRAINT ck_stock_movements_exit_reason
        CHECK (exit_reason IN ('discard', 'return', 'loss'));
GO

-- An 'out' must have a reason. An 'in' or a 'use' has no reason.
IF OBJECT_ID('ck_stock_movements_type_reason', 'C') IS NULL
    ALTER TABLE stock_movements ADD CONSTRAINT ck_stock_movements_type_reason
        CHECK ((type = 'out' AND exit_reason IS NOT NULL)
            OR (type <> 'out' AND exit_reason IS NULL));
GO
