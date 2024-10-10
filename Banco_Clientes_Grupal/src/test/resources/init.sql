CREATE TABLE IF NOT EXISTS tarjetas (
    id SERIAL PRIMARY KEY,
    nombreTitular VARCHAR(255) NOT NULL,
    numeroTarjeta VARCHAR(255) NOT NULL,
    fechaCaducidad TIMESTAMP NOT NULL,
    createdAt TIMESTAMP NOT NULL,
    updatedAt TIMESTAMP NOT NULL
);

INSERT INTO tarjetas (id, nombreTitular, numeroTarjeta, fechaCaducidad, createdAt, updatedAt)
VALUES (1,'Ana', '1234567890123456', '2025-01-01 00:00:00', NOW(), NOW()),
       (2,'Maria', '2468101213141516', '2028-06-01 00:00:00', NOW(), NOW());