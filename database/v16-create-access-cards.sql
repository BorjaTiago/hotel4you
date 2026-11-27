-- v16: create the access_cards and access_logs tables and add the first master card (H4Y-109).
-- Run after v13 (and v3 for the first manager). It needs v6 (rooms), v2 (users), v4 (locations) and v9 (reservations).
-- You can run it again: it skips what already exists.
-- The places a client card may open come from reservation_locations (v13). There is no new table for them.
-- The master card is for the first manager. It has a card number only. No login data is in this script.

USE hotel4you;

IF OBJECT_ID('dbo.reservations', 'U') IS NULL OR OBJECT_ID('dbo.users', 'U') IS NULL
        OR OBJECT_ID('dbo.locations', 'U') IS NULL OR OBJECT_ID('dbo.rooms', 'U') IS NULL
    THROW 50004, 'Execute primeiro os scripts v2 (utilizadores), v4 (locais), v6 (quartos) e v9 (reservas).', 1;

-- Table: access_cards
IF OBJECT_ID('dbo.access_cards', 'U') IS NULL
    CREATE TABLE dbo.access_cards (
        id             INT IDENTITY NOT NULL,
        card_number VARCHAR(20) NOT NULL,
        reservation_id INT NULL,
        user_id        SMALLINT NULL,
        is_master      BIT NOT NULL CONSTRAINT df_access_cards_master DEFAULT 0,
        active         BIT NOT NULL CONSTRAINT df_access_cards_active DEFAULT 1,
        blocked_at     DATETIME2(0) NULL,
        CONSTRAINT pk_access_cards PRIMARY KEY (id),
        CONSTRAINT uq_access_cards_number UNIQUE (card_number),
        CONSTRAINT fk_access_cards_reservation FOREIGN KEY (reservation_id) REFERENCES dbo.reservations(id),
        CONSTRAINT fk_access_cards_user FOREIGN KEY (user_id) REFERENCES dbo.users(id),
        CONSTRAINT ck_access_cards_owner CHECK (
            (reservation_id IS NOT NULL AND user_id IS NULL)
            OR (reservation_id IS NULL AND user_id IS NOT NULL)),
        CONSTRAINT ck_access_cards_master CHECK (is_master = 0 OR user_id IS NOT NULL)
    );

-- Table: access_logs
IF OBJECT_ID('dbo.access_logs', 'U') IS NULL
    CREATE TABLE dbo.access_logs (
        id             INT IDENTITY NOT NULL,
        access_card_id INT NOT NULL,
        location_id    TINYINT NULL,
        room_id        SMALLINT NULL,
        entered_at     DATETIME2(0) NOT NULL,
        CONSTRAINT pk_access_logs PRIMARY KEY (id),
        CONSTRAINT fk_access_logs_card FOREIGN KEY (access_card_id) REFERENCES dbo.access_cards(id),
        CONSTRAINT fk_access_logs_location FOREIGN KEY (location_id) REFERENCES dbo.locations(id),
        CONSTRAINT fk_access_logs_room FOREIGN KEY (room_id) REFERENCES dbo.rooms(id),
        CONSTRAINT ck_access_logs_place CHECK (
            (location_id IS NOT NULL AND room_id IS NULL)
            OR (location_id IS NULL AND room_id IS NOT NULL))
    );

IF NOT EXISTS (SELECT 1 FROM sys.indexes
        WHERE object_id = OBJECT_ID('dbo.access_logs') AND name = 'ix_access_logs_card_entered')
    CREATE INDEX ix_access_logs_card_entered ON dbo.access_logs(access_card_id, entered_at);

IF NOT EXISTS (SELECT 1 FROM sys.indexes
        WHERE object_id = OBJECT_ID('dbo.access_logs') AND name = 'ix_access_logs_room_entered')
    CREATE INDEX ix_access_logs_room_entered ON dbo.access_logs(room_id, entered_at);

-- First master card: one card for the first manager. It skips when a master card exists or no manager exists.
IF NOT EXISTS (SELECT 1 FROM dbo.access_cards WHERE is_master = 1)
    INSERT INTO dbo.access_cards (card_number, user_id, is_master)
    SELECT TOP 1 '90001', u.id, 1
    FROM dbo.users u JOIN dbo.roles r ON r.id = u.role_id
    WHERE r.name = 'MANAGER'
    ORDER BY u.id;
