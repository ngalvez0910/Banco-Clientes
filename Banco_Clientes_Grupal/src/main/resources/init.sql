-- Eliminar las tablas si existen
DROP TABLE IF EXISTS Tarjeta;
DROP TABLE IF EXISTS Usuario;

-- Crear la tabla Usuario
CREATE TABLE IF NOT EXISTS Usuario (
    id TEXT PRIMARY KEY DEFAULT (lower(hex(randomblob(16)))),
    nombre VARCHAR(255) NOT NULL,
    userName VARCHAR(255) NOT NULL UNIQUE,
    email VARCHAR(255) NOT NULL UNIQUE,
    createdAt TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updatedAt TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
    );

-- Crear la tabla Tarjeta
CREATE TABLE IF NOT EXISTS Tarjeta (
    id TEXT PRIMARY KEY DEFAULT (lower(hex(randomblob(16)))),
    numeroTarjeta VARCHAR(16) NOT NULL,
    nombreTitular TEXT NOT NULL,
    fechaCaducidad DATE NOT NULL,
    createdAt TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updatedAt TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (clientID) REFERENCES Cliente(id) ON DELETE CASCADE
    );