-- v21: save the price of paid services included in a reservation (H4Y-108, RF-09).
-- Run after v18 and v20. A repeat run keeps existing inclusions and requests.

USE hotel4you;

IF OBJECT_ID('dbo.reservation_services', 'U') IS NULL
    THROW 50005, N'Execute primeiro o script v18 (serviços da reserva).', 1;

IF OBJECT_ID('dbo.ck_reservation_services_included_price', 'C') IS NULL
    ALTER TABLE dbo.reservation_services ADD CONSTRAINT ck_reservation_services_included_price
        CHECK (used_at IS NOT NULL OR unit_price IS NOT NULL);

IF NOT EXISTS (SELECT 1 FROM sys.indexes
        WHERE object_id = OBJECT_ID('dbo.reservation_services') AND name = 'uq_reservation_services_included')
    CREATE UNIQUE INDEX uq_reservation_services_included
        ON dbo.reservation_services(reservation_id, extra_price_id) WHERE used_at IS NULL;
