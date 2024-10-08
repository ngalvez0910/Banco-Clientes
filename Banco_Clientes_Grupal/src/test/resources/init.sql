CREATE TABLE IF NOT EXISTS tarjetas (
    id SERIAL PRIMARY KEY,
    nombreTitular VARCHAR(255) NOT NULL,
    numeroTarjeta VARCHAR(255) NOT NULL,
    fechaCaducidad VARCHAR(255) NOT NULL
);

INSERT INTO tarjetas (nombreTitular, numeroTarjeta, fechaCaducidad)
VALUES ("Ana", "1234567890123456", "01/25"),
       ("Maria", "2468101213141516", "06/28");