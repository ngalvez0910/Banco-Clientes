package org.example.clientes.errors;

import java.time.LocalDate;

public abstract class TarjetaError extends Exception {
    public TarjetaError(String message) {super(message);}

    public static class TarjetaIdInvalido extends TarjetaError {
        public TarjetaIdInvalido(String id) {
            super("El ID de la tarjeta '" + id + "' no es válido.");
        }
    }

    public static class TarjetaNumeroInvalido extends TarjetaError {
        public TarjetaNumeroInvalido(String numeroTarjeta) {
            super("El número de tarjeta '" + numeroTarjeta + "' no es válido.");
        }
    }

    public static class FechaCaducidadInvalida extends TarjetaError {
        public FechaCaducidadInvalida(LocalDate fechaCaducidad) {
            super("La fecha de caducidad '" + fechaCaducidad + "' no es válida.");
        }
    }

    public static class NombreTitularInvalido extends TarjetaError {
        public NombreTitularInvalido(String nombreTitular) {
            super("El nombre del titular '" + nombreTitular + "' no es válido.");
        }
    }

    public static class TarjetaNoEncontrada extends TarjetaError {
        public TarjetaNoEncontrada(Long id) {
            super("No se encontró la tarjeta con ID '" + id + "'.");
        }
    }
}

