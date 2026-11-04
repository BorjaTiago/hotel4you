-- H4Y-72: reservations section for the single shared v7 from Sprint 04 planning.
-- Integration fragment, not a separate numbered migration.
-- Combine with Lukas's H4Y-69 customers section in v7, after that section.
-- The shared script must select the hotel4you database before this section.
-- No customer implementation, seed bookings or changes to existing rows belong here.

IF OBJECT_ID('dbo.customers', 'U') IS NULL
    THROW 50001, 'A secção customers do v7 tem de preceder reservations.', 1;
IF OBJECT_ID('dbo.rooms', 'U') IS NULL OR OBJECT_ID('dbo.users', 'U') IS NULL
    THROW 50002, 'Execute primeiro os scripts de utilizadores e quartos.', 1;

IF OBJECT_ID('dbo.reservations', 'U') IS NULL
BEGIN
    CREATE TABLE dbo.reservations (
        id                  INT IDENTITY NOT NULL,
        customer_id         INT NOT NULL,
        room_id             SMALLINT NOT NULL,
        start_date          DATE NOT NULL,
        end_date            DATE NOT NULL,
        status              VARCHAR(15) NOT NULL DEFAULT 'reserved',
        checked_in_at       DATETIME2(0) NULL,
        checked_out_at      DATETIME2(0) NULL,
        created_by_user_id  SMALLINT NOT NULL,
        CONSTRAINT pk_reservations PRIMARY KEY (id),
        CONSTRAINT fk_reservations_customer FOREIGN KEY (customer_id) REFERENCES dbo.customers(id),
        CONSTRAINT fk_reservations_room FOREIGN KEY (room_id) REFERENCES dbo.rooms(id),
        CONSTRAINT fk_reservations_user FOREIGN KEY (created_by_user_id) REFERENCES dbo.users(id),
        CONSTRAINT ck_reservations_dates CHECK (end_date > start_date),
        CONSTRAINT ck_reservations_status CHECK (
            status IN ('reserved', 'cancelled', 'checked_in', 'checked_out')),
        CONSTRAINT ck_reservations_check_in CHECK (
            (status IN ('checked_in', 'checked_out') AND checked_in_at IS NOT NULL)
            OR (status IN ('reserved', 'cancelled') AND checked_in_at IS NULL)),
        CONSTRAINT ck_reservations_check_out CHECK (
            (status = 'checked_out' AND checked_out_at IS NOT NULL)
            OR (status <> 'checked_out' AND checked_out_at IS NULL)),
        CONSTRAINT ck_reservations_checkout_order CHECK (checked_out_at >= checked_in_at)
    );
END;

IF NOT EXISTS (SELECT 1 FROM sys.indexes
        WHERE object_id = OBJECT_ID('dbo.reservations') AND name = 'ix_reservations_room_dates')
    CREATE INDEX ix_reservations_room_dates ON dbo.reservations(room_id, start_date, end_date);
