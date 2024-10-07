package org.example.clientes.validators;

import io.vavr.control.Either;
import org.example.clientes.errors.TarjetaError;
import org.example.clientes.model.Tarjeta;

import java.time.LocalDate;
import java.util.stream.IntStream;

public class TarjetaValidator {

    // Validar si el número de tarjeta es válido usando el algoritmo de Luhn
    public Either<TarjetaError.TarjetaNumeroInvalido, String> validarNumeroTarjeta(Tarjeta tarjeta) {
        String numeroTarjeta = tarjeta.getNumeroTarjeta().replaceAll("\\s+", "");  // Elimina espacios

        if (numeroTarjeta.length() != 16 || !numeroTarjeta.matches("\\d+")) {
            return Either.left(new TarjetaError.TarjetaNumeroInvalido(numeroTarjeta));
        }

        if (!esValidoLuhn(numeroTarjeta)) {
            return Either.left(new TarjetaError.TarjetaNumeroInvalido(numeroTarjeta));
        }

        return Either.right("El número de tarjeta '" + numeroTarjeta + "' es válido.");
    }

    // Método que implementa el algoritmo de Luhn
    private boolean esValidoLuhn(String numeroTarjeta) {
        int[] numeros = numeroTarjeta.chars()
                .map(Character::getNumericValue)
                .toArray();

        for (int i = numeros.length - 2; i >= 0; i -= 2) {
            int doble = numeros[i] * 2;
            if (doble > 9) {
                doble = doble - 9;  // Sumar los dígitos del producto si es mayor a 9
            }
            numeros[i] = doble;
        }

        int suma = IntStream.of(numeros).sum();

        // Si la suma es múltiplo de 10, el número de tarjeta es válido
        return suma % 10 == 0;
    }

    // Validar si la fecha de caducidad es válida (que no esté expirada)
    public Either<TarjetaError.FechaCaducidadInvalida, String> validarFechaCaducidad(Tarjeta tarjeta) {
        LocalDate fechaActual = LocalDate.now();
        if (tarjeta.getFechaCaducidad().isBefore(fechaActual)) {
            return Either.left(new TarjetaError.FechaCaducidadInvalida(tarjeta.getFechaCaducidad()));
        }
        return Either.right("La fecha de caducidad es válida.");
    }

    // Validar nombre del titular
    public Either<TarjetaError.NombreTitularInvalido, String> validarNombreTitular(Tarjeta tarjeta) {
        String nombreTitular = tarjeta.getNombreTitular();
        if (nombreTitular == null || nombreTitular.trim().isEmpty()) {
            return Either.left(new TarjetaError.NombreTitularInvalido(nombreTitular));
        }
        return Either.right("El nombre del titular '" + nombreTitular + "' es válido.");
    }
}
