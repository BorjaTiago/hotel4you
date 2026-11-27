-- v20: save room extras with their prices per night (H4Y-107, RF-07).
-- Run after v14. Repeated runs keep existing extras and prices.

USE hotel4you;

IF OBJECT_ID('dbo.reservations', 'U') IS NULL OR OBJECT_ID('dbo.extra_prices', 'U') IS NULL
    THROW 50004, N'Execute primeiro os scripts v9 (reservas) e v14 (preços extra).', 1;

IF OBJECT_ID('dbo.reservation_extras', 'U') IS NULL
    CREATE TABLE dbo.reservation_extras (
        reservation_id INT          NOT NULL,
        extra_price_id SMALLINT     NOT NULL,
        unit_price     DECIMAL(8,2) NOT NULL,
        CONSTRAINT pk_reservation_extras PRIMARY KEY (reservation_id, extra_price_id),
        CONSTRAINT fk_reservation_extras_reservation FOREIGN KEY (reservation_id) REFERENCES dbo.reservations(id),
        CONSTRAINT fk_reservation_extras_extra_price FOREIGN KEY (extra_price_id) REFERENCES dbo.extra_prices(id),
        CONSTRAINT ck_reservation_extras_price CHECK (unit_price >= 0)
    );
