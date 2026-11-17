-- v11: create the room_items table for the items placed in each room.
-- Run after v6 (rooms) and v7 (items). You can run it again: it skips what already exists.

USE hotel4you;

IF OBJECT_ID('dbo.rooms', 'U') IS NULL OR OBJECT_ID('dbo.items', 'U') IS NULL
    THROW 50003, 'Execute primeiro os scripts v6 (rooms) e v7 (items).', 1;

-- Table: room_items. No cascade delete: a room or item with rows here cannot be deleted (error 547).
IF OBJECT_ID('dbo.room_items', 'U') IS NULL
BEGIN
    CREATE TABLE dbo.room_items (
        room_id  SMALLINT NOT NULL,
        item_id  SMALLINT NOT NULL,
        quantity TINYINT  NOT NULL,
        CONSTRAINT pk_room_items PRIMARY KEY (room_id, item_id),
        CONSTRAINT fk_room_items_room FOREIGN KEY (room_id) REFERENCES dbo.rooms(id),
        CONSTRAINT fk_room_items_item FOREIGN KEY (item_id) REFERENCES dbo.items(id),
        CONSTRAINT ck_room_items_quantity CHECK (quantity > 0)
    );
END;
