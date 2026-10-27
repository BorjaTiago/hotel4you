-- v6: create rooms after the floors and room_types tables.
-- Run after the floor and room type scripts. The manager registers the rooms.

USE hotel4you;

IF OBJECT_ID('rooms', 'U') IS NULL
    CREATE TABLE rooms (
        id           SMALLINT IDENTITY NOT NULL,
        number       SMALLINT NOT NULL,
        floor_id     TINYINT NOT NULL,
        room_type_id TINYINT NOT NULL,
        CONSTRAINT pk_rooms PRIMARY KEY (id),
        CONSTRAINT uq_rooms_number UNIQUE (number),
        CONSTRAINT fk_rooms_floor FOREIGN KEY (floor_id) REFERENCES floors (id),
        CONSTRAINT fk_rooms_room_type FOREIGN KEY (room_type_id) REFERENCES room_types (id)
    );
