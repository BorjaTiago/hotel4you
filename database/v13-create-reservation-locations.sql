-- v13: create the reservation_locations table (restaurant and pool of a reservation, RF-18).
-- Run after v12. It needs v4 (locations) and v9 (reservations). You can run it again: it skips what already exists.

USE hotel4you;

IF OBJECT_ID('dbo.reservations', 'U') IS NULL OR OBJECT_ID('dbo.locations', 'U') IS NULL
    THROW 50003, 'Execute primeiro os scripts v4 (locais) e v9 (reservas).', 1;

IF OBJECT_ID('dbo.reservation_locations', 'U') IS NULL
    CREATE TABLE dbo.reservation_locations (
        reservation_id INT     NOT NULL,
        location_id    TINYINT NOT NULL,
        CONSTRAINT pk_reservation_locations PRIMARY KEY (reservation_id, location_id),
        CONSTRAINT fk_reservation_locations_reservation FOREIGN KEY (reservation_id) REFERENCES dbo.reservations(id),
        CONSTRAINT fk_reservation_locations_location FOREIGN KEY (location_id) REFERENCES dbo.locations(id)
    );
