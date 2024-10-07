package org.example.clientes.validators;

import io.vavr.control.Either;
import org.example.clientes.errors.TarjetaError;
import org.example.clientes.model.Tarjeta;
import java.time.LocalDate;

public class TarjetaValidator {

    public Either<TarjetaError.TarjetaNumeroInvalido, String> validarNumeroTarjeta(Tarjeta tarjeta) {
        String numeroTarjeta = tarjeta.getNumeroTarjeta().replaceAll("\\s+", "");
        if (numeroTarjeta.length() != 16 || !numeroTarjeta.matches("\\d+")) {
            return Either.left(new TarjetaError.TarjetaNumeroInvalido(numeroTarjeta));
        }
        if (!esValidoLuhn(numeroTarjeta)) {
            return Either.left(new TarjetaError.TarjetaNumeroInvalido(numeroTarjeta));
        }
        return Either.right("El número de tarjeta '" + numeroTarjeta + "' es válido.");
    }

    private boolean esValidoLuhn(String numeroTarjeta) {
        int[] numeros = numeroTarjeta.chars()
                .map(Character::getNumericValue)
                .toArray();
        for (int i = numeros.length - 2; i >= 0; i -= 2) {
            int doble = numeros[i] * 2;
            if (doble > 9) {
                doble = doble - 9;
            }
            numeros[i] = doble;
        }
        int suma = java.util.Arrays.stream(numeros).sum();
        return suma % 10 == 0;
    }

    public Either<TarjetaError.FechaCaducidadInvalida, String> validarFechaCaducidad(Tarjeta tarjeta) {
        LocalDate fechaActual = LocalDate.now();
        if (tarjeta.getFechaCaducidad().isBefore(fechaActual)) {
            return Either.left(new TarjetaError.FechaCaducidadInvalida(tarjeta.getFechaCaducidad()));
        }
        return Either.right("La fecha de caducidad es válida.");
    }

    public Either<TarjetaError.NombreTitularInvalido, String> validarNombreTitular(Tarjeta tarjeta) {
        String nombreTitular = tarjeta.getNombreTitular();
        if (nombreTitular == null || nombreTitular.trim().isEmpty()) {
            return Either.left(new TarjetaError.NombreTitularInvalido(nombreTitular));
        }
        return Either.right("El nombre del titular '" + nombreTitular + "' es válido.");
    }

    public Either<TarjetaError.TarjetaIdInvalido, String> validarIdTarjeta(Tarjeta tarjeta) {
        String idTarjeta = String.valueOf(tarjeta.getId());
        if (idTarjeta == null || idTarjeta.trim().isEmpty()) {
            return Either.left(new TarjetaError.TarjetaIdInvalido(idTarjeta));
        }
        return Either.right("El ID de la tarjeta '" + idTarjeta + "' es válido.");
    }

    public boolean validate(Tarjeta tarjeta) {
        Either<TarjetaError.TarjetaIdInvalido, String> idResult = validarIdTarjeta(tarjeta);
        if (idResult.isLeft()) {
            return false;
        }
        Either<TarjetaError.TarjetaNumeroInvalido, String> numeroResult = validarNumeroTarjeta(tarjeta);
        if (numeroResult.isLeft()) {
            return false;
        }
        Either<TarjetaError.FechaCaducidadInvalida, String> fechaResult = validarFechaCaducidad(tarjeta);
        if (fechaResult.isLeft()) {
            return false;
        }
        Either<TarjetaError.NombreTitularInvalido, String> nombreResult = validarNombreTitular(tarjeta);
        if (nombreResult.isLeft()) {
            return false;
        }
        return true;
    }
}
