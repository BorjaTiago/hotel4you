-- v4: create the floors and locations tables, and add the floors and locations of the hotel.
-- Run after v3. You can run it again: it skips what already exists.

USE hotel4you;

-- Table: floors
IF OBJECT_ID('floors') IS NULL
    CREATE TABLE floors (
        id          TINYINT       IDENTITY NOT NULL,
        number      SMALLINT      NOT NULL,
        description NVARCHAR(100) NOT NULL,
        CONSTRAINT pk_floors        PRIMARY KEY (id),
        CONSTRAINT uq_floors_number UNIQUE (number)
    );

-- Table: locations
IF OBJECT_ID('locations') IS NULL
    CREATE TABLE locations (
        id                   TINYINT     IDENTITY NOT NULL,
        name                 VARCHAR(50) NOT NULL,
        floor_id             TINYINT     NOT NULL,
        guest_card_access    BIT         NOT NULL CONSTRAINT df_locations_guest_card_access DEFAULT 0,
        requires_reservation BIT         NOT NULL CONSTRAINT df_locations_requires_reservation DEFAULT 0,
        CONSTRAINT pk_locations            PRIMARY KEY (id),
        CONSTRAINT uq_locations_floor_name UNIQUE (floor_id, name),
        CONSTRAINT fk_locations_floor      FOREIGN KEY (floor_id) REFERENCES floors (id)
    );

-- The 5 floors
IF NOT EXISTS (SELECT 1 FROM floors WHERE number = -1) INSERT INTO floors (number, description) VALUES (-1, N'Garagem e arrecadação');
IF NOT EXISTS (SELECT 1 FROM floors WHERE number = 0)  INSERT INTO floors (number, description) VALUES (0, N'Receção, bar e salas de conferência');
IF NOT EXISTS (SELECT 1 FROM floors WHERE number = 1)  INSERT INTO floors (number, description) VALUES (1, N'Restaurante e piscina');
IF NOT EXISTS (SELECT 1 FROM floors WHERE number = 2)  INSERT INTO floors (number, description) VALUES (2, N'Quartos');
IF NOT EXISTS (SELECT 1 FROM floors WHERE number = 3)  INSERT INTO floors (number, description) VALUES (3, N'Quartos');

-- Locations of floor -1 (no client card access)
IF NOT EXISTS (SELECT 1 FROM locations WHERE floor_id = (SELECT id FROM floors WHERE number = -1) AND name = 'Garagem')
    INSERT INTO locations (name, floor_id, guest_card_access, requires_reservation) VALUES ('Garagem', (SELECT id FROM floors WHERE number = -1), 0, 0);
IF NOT EXISTS (SELECT 1 FROM locations WHERE floor_id = (SELECT id FROM floors WHERE number = -1) AND name = 'Arrecadação')
    INSERT INTO locations (name, floor_id, guest_card_access, requires_reservation) VALUES ('Arrecadação', (SELECT id FROM floors WHERE number = -1), 0, 0);

-- Locations of floor 0 (no client card access)
IF NOT EXISTS (SELECT 1 FROM locations WHERE floor_id = (SELECT id FROM floors WHERE number = 0) AND name = 'Receção')
    INSERT INTO locations (name, floor_id, guest_card_access, requires_reservation) VALUES ('Receção', (SELECT id FROM floors WHERE number = 0), 0, 0);
IF NOT EXISTS (SELECT 1 FROM locations WHERE floor_id = (SELECT id FROM floors WHERE number = 0) AND name = 'Bar')
    INSERT INTO locations (name, floor_id, guest_card_access, requires_reservation) VALUES ('Bar', (SELECT id FROM floors WHERE number = 0), 0, 0);
IF NOT EXISTS (SELECT 1 FROM locations WHERE floor_id = (SELECT id FROM floors WHERE number = 0) AND name = 'Salas de conferência')
    INSERT INTO locations (name, floor_id, guest_card_access, requires_reservation) VALUES ('Salas de conferência', (SELECT id FROM floors WHERE number = 0), 0, 0);

-- Locations of floor 1 (client card opens them, with a reservation)
IF NOT EXISTS (SELECT 1 FROM locations WHERE floor_id = (SELECT id FROM floors WHERE number = 1) AND name = 'Restaurante')
    INSERT INTO locations (name, floor_id, guest_card_access, requires_reservation) VALUES ('Restaurante', (SELECT id FROM floors WHERE number = 1), 1, 1);
IF NOT EXISTS (SELECT 1 FROM locations WHERE floor_id = (SELECT id FROM floors WHERE number = 1) AND name = 'Piscina')
    INSERT INTO locations (name, floor_id, guest_card_access, requires_reservation) VALUES ('Piscina', (SELECT id FROM floors WHERE number = 1), 1, 1);
