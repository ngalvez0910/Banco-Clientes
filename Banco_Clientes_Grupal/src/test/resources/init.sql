CREATE TABLE IF NOT EXISTS tarjetas (
    id SERIAL PRIMARY KEY,
    nombreTitular VARCHAR(255) NOT NULL,
    numeroTarjeta VARCHAR(255) NOT NULL,
    fechaCaducidad VARCHAR(5) NOT NULL
);

INSERT INTO tarjetas (id, nombreTitular, numeroTarjeta, fechaCaducidad)
VALUES (1,'Ana', '1234567890123456', '01/25'),
       (2,'Maria', '2468101213141516', '06/28');