-- v18: create the reservation_services table (H4Y-110, RF-15, Q-33).
-- A room service request (food, drink, laundry) is one row. used_at is the request time.
-- Run after v14. It needs v9 (reservations) and v14 (extra_prices). You can run it again: it skips what already exists.

USE hotel4you;

IF OBJECT_ID('dbo.reservations', 'U') IS NULL OR OBJECT_ID('dbo.extra_prices', 'U') IS NULL
    THROW 50004, 'Execute primeiro os scripts v9 (reservas) e v14 (preços extra).', 1;

IF OBJECT_ID('dbo.reservation_services', 'U') IS NULL
    CREATE TABLE dbo.reservation_services (
        id             INT          IDENTITY NOT NULL,
        reservation_id INT          NOT NULL,
        extra_price_id SMALLINT     NOT NULL,
        unit_price     DECIMAL(8,2) NULL,
        used_at        DATETIME2(0) NULL,
        CONSTRAINT pk_reservation_services PRIMARY KEY (id),
        CONSTRAINT fk_reservation_services_reservation FOREIGN KEY (reservation_id) REFERENCES dbo.reservations(id),
        CONSTRAINT fk_reservation_services_extra_price FOREIGN KEY (extra_price_id) REFERENCES dbo.extra_prices(id),
        CONSTRAINT ck_reservation_services_unit_price CHECK (unit_price IS NULL OR unit_price >= 0)
    );

IF NOT EXISTS (SELECT 1 FROM sys.indexes WHERE name = 'ix_reservation_services_reservation'
        AND object_id = OBJECT_ID('dbo.reservation_services'))
    CREATE INDEX ix_reservation_services_reservation ON dbo.reservation_services (reservation_id);
