-- v14: create the fixed extra price catalogue (H4Y-103, RF-07, RF-09).
-- Run after v13. Existing entries and their prices are kept on a repeated run.
USE hotel4you;
SET XACT_ABORT ON;

BEGIN TRY
    BEGIN TRANSACTION;

    IF OBJECT_ID('dbo.extra_prices', 'U') IS NULL
        CREATE TABLE dbo.extra_prices (
            id    SMALLINT      IDENTITY NOT NULL,
            name  NVARCHAR(100) NOT NULL,
            type  VARCHAR(20)   NOT NULL,
            price DECIMAL(8,2)  NULL,
            CONSTRAINT pk_extra_prices PRIMARY KEY (id),
            CONSTRAINT uq_extra_prices_name UNIQUE (name),
            CONSTRAINT ck_extra_prices_type CHECK (type IN ('ROOM_EXTRA', 'PAID_SERVICE')),
            CONSTRAINT ck_extra_prices_price CHECK (price IS NULL OR price >= 0)
        );

    IF NOT EXISTS (SELECT 1 FROM dbo.extra_prices WHERE name = N'Vista mar')
        INSERT INTO dbo.extra_prices (name, type, price) VALUES (N'Vista mar', 'ROOM_EXTRA', NULL);
    IF NOT EXISTS (SELECT 1 FROM dbo.extra_prices WHERE name = N'Vista montanha')
        INSERT INTO dbo.extra_prices (name, type, price) VALUES (N'Vista montanha', 'ROOM_EXTRA', NULL);
    IF NOT EXISTS (SELECT 1 FROM dbo.extra_prices WHERE name = N'Bebidas')
        INSERT INTO dbo.extra_prices (name, type, price) VALUES (N'Bebidas', 'ROOM_EXTRA', NULL);
    IF NOT EXISTS (SELECT 1 FROM dbo.extra_prices WHERE name = N'Lavandaria')
        INSERT INTO dbo.extra_prices (name, type, price) VALUES (N'Lavandaria', 'ROOM_EXTRA', NULL);
    IF NOT EXISTS (SELECT 1 FROM dbo.extra_prices WHERE name = N'Comida')
        INSERT INTO dbo.extra_prices (name, type, price) VALUES (N'Comida', 'PAID_SERVICE', NULL);
    IF NOT EXISTS (SELECT 1 FROM dbo.extra_prices WHERE name = N'Eventos culturais')
        INSERT INTO dbo.extra_prices (name, type, price) VALUES (N'Eventos culturais', 'PAID_SERVICE', NULL);
    IF NOT EXISTS (SELECT 1 FROM dbo.extra_prices WHERE name = N'Piscinas privadas')
        INSERT INTO dbo.extra_prices (name, type, price) VALUES (N'Piscinas privadas', 'PAID_SERVICE', NULL);
    IF NOT EXISTS (SELECT 1 FROM dbo.extra_prices WHERE name = N'Spa')
        INSERT INTO dbo.extra_prices (name, type, price) VALUES (N'Spa', 'PAID_SERVICE', NULL);
    IF NOT EXISTS (SELECT 1 FROM dbo.extra_prices WHERE name = N'Salas de conferências')
        INSERT INTO dbo.extra_prices (name, type, price) VALUES (N'Salas de conferências', 'PAID_SERVICE', NULL);

    COMMIT TRANSACTION;
END TRY
BEGIN CATCH
    IF @@TRANCOUNT > 0 ROLLBACK TRANSACTION;
    THROW;
END CATCH;
