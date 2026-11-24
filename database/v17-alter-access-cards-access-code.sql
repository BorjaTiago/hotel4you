-- v17: add the hashed access code to the access_cards table (H4Y-132).
-- Run after v16. You can run it again: it skips the column if it exists.
-- The client logs in with the card number and a 6 character code. The table keeps only the hash of the code,
-- made the same way as users.password_hash. The column is NULL for cards that have no code yet,
-- for example the first master card of v16.

USE hotel4you;

IF OBJECT_ID('dbo.access_cards', 'U') IS NULL
    THROW 50005, 'Execute primeiro o script v16 (cartões).', 1;

IF COL_LENGTH('dbo.access_cards', 'access_code_hash') IS NULL
    ALTER TABLE dbo.access_cards ADD access_code_hash VARCHAR(255) NULL;
