DROP TABLE IF EXISTS Tarjeta;
DROP TABLE IF EXISTS Usuario;

CREATE TABLE IF NOT EXISTS Usuario (
   id INTEGER PRIMARY KEY AUTOINCREMENT,
   nombre VARCHAR(255) NOT NULL,
    userName VARCHAR(255) NOT NULL UNIQUE,
    email VARCHAR(255) NOT NULL UNIQUE,
    createdAt TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updatedAt TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
    );

CREATE TABLE IF NOT EXISTS Tarjeta (
   id TEXT PRIMARY KEY DEFAULT (lower(hex(randomblob(16)))),
    numeroTarjeta VARCHAR(16) NOT NULL,
    nombreTitular TEXT NOT NULL,
    fechaCaducidad DATE NOT NULL,
    createdAt TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updatedAt TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (nombreTitular) REFERENCES Usuario(nombre) ON DELETE CASCADE
    );


INSERT INTO Usuario (nombre, userName, email, createdAt, updatedAt)
VALUES ('Ana', 'anaUser', 'ana@example.com', DATETIME('now'), DATETIME('now')),
       ('Maria', 'mariaUser', 'maria@example.com', DATETIME('now'), DATETIME('now'));


INSERT INTO Tarjeta (nombreTitular, numeroTarjeta, fechaCaducidad, createdAt, updatedAt)
VALUES ('Ana', '1234567890123456', '2025-01-01 00:00:00', DATETIME('now'), DATETIME('now')),
       ('Maria', '2468101213141516', '2028-06-01 00:00:00', DATETIME('now'), DATETIME('now'));