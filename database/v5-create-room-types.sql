-- v5: create the room_types table, and add the 3 room types with no base price.
-- Run after v4. You can run it again: it skips what already exists.

USE hotel4you;

-- Table: room_types
IF OBJECT_ID('room_types') IS NULL
    CREATE TABLE room_types (
        id         TINYINT       IDENTITY NOT NULL,
        name       VARCHAR(20)   NOT NULL,
        capacity   TINYINT       NOT NULL,
        base_price DECIMAL(8,2)  NULL,
        CONSTRAINT pk_room_types            PRIMARY KEY (id),
        CONSTRAINT uq_room_types_name       UNIQUE (name),
        CONSTRAINT ck_room_types_capacity   CHECK (capacity > 0),
        CONSTRAINT ck_room_types_base_price CHECK (base_price IS NULL OR base_price >= 0)
    );

-- The 3 room types (the manager sets the base price later)
IF NOT EXISTS (SELECT 1 FROM room_types WHERE name = 'individual') INSERT INTO room_types (name, capacity, base_price) VALUES ('individual', 1, NULL);
IF NOT EXISTS (SELECT 1 FROM room_types WHERE name = 'duplo')      INSERT INTO room_types (name, capacity, base_price) VALUES ('duplo', 2, NULL);
IF NOT EXISTS (SELECT 1 FROM room_types WHERE name = 'familiar')   INSERT INTO room_types (name, capacity, base_price) VALUES ('familiar', 4, NULL);
