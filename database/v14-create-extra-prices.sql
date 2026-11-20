-- v14: create the fixed extra price catalogue (H4Y-103, RF-07, RF-09).
-- Run after v13. Repeated runs add missing entries and keep saved prices.
USE hotel4you;

IF OBJECT_ID('extra_prices', 'U') IS NULL
    CREATE TABLE extra_prices (
        id    SMALLINT      IDENTITY NOT NULL,
        name  NVARCHAR(100) NOT NULL,
        type  VARCHAR(20)   NOT NULL,
        price DECIMAL(8,2)  NULL,
        CONSTRAINT pk_extra_prices       PRIMARY KEY (id),
        CONSTRAINT uq_extra_prices_name  UNIQUE (name),
        CONSTRAINT ck_extra_prices_type  CHECK (type IN ('ROOM_EXTRA', 'PAID_SERVICE')),
        CONSTRAINT ck_extra_prices_price CHECK (price IS NULL OR price >= 0)
    );

-- Insert only missing names, including when a catalogue is partly filled.
INSERT INTO extra_prices (name, type)
SELECT seed.name, seed.type
FROM (VALUES
    (N'Vista mar', 'ROOM_EXTRA'),
    (N'Vista montanha', 'ROOM_EXTRA'),
    (N'Bebidas', 'ROOM_EXTRA'),
    (N'Lavandaria', 'ROOM_EXTRA'),
    (N'Comida', 'PAID_SERVICE'),
    (N'Eventos culturais', 'PAID_SERVICE'),
    (N'Piscinas privadas', 'PAID_SERVICE'),
    (N'Spa', 'PAID_SERVICE'),
    (N'Salas de conferências', 'PAID_SERVICE')
) AS seed(name, type)
WHERE NOT EXISTS (SELECT 1 FROM extra_prices WHERE name = seed.name);
