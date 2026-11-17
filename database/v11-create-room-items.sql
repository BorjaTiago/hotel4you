-- v11: create the room_items table for the items placed in each room.
-- Run after v6 and v7. You can run it again: it skips what already exists.

USE hotel4you;

-- Table: room_items. No cascade delete: a room or item with rows here cannot be deleted (error 547).
IF OBJECT_ID('room_items', 'U') IS NULL
    CREATE TABLE room_items (
        room_id  SMALLINT NOT NULL,
        item_id  SMALLINT NOT NULL,
        quantity TINYINT  NOT NULL,
        CONSTRAINT pk_room_items          PRIMARY KEY (room_id, item_id),
        CONSTRAINT fk_room_items_room     FOREIGN KEY (room_id) REFERENCES rooms(id),
        CONSTRAINT fk_room_items_item     FOREIGN KEY (item_id) REFERENCES items(id),
        CONSTRAINT ck_room_items_quantity CHECK (quantity > 0)
    );
