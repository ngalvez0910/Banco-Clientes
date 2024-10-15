package org.example.clientes.validators;

import io.vavr.control.Either;
import org.example.clientes.errors.TarjetaError;
import org.example.clientes.model.Tarjeta;
import java.time.LocalDate;

/**
 * Clase encargada de validar los atributos de un objeto Tarjeta.
 */
public class TarjetaValidator {

    /**
     * Valida el número de la tarjeta.
     *
     * @param tarjeta La tarjeta que se va a validar.
     * @return Un Either que contiene un error si el número es inválido, o un mensaje de éxito si es válido.
     */
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

    /**
     * Verifica si el número de tarjeta es válido utilizando el algoritmo de Luhn.
     *
     * @param numeroTarjeta El número de la tarjeta a validar.
     * @return true si es válido, false en caso contrario.
     */
    public boolean esValidoLuhn(String numeroTarjeta) {
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

    /**
     * Valida la fecha de caducidad de la tarjeta.
     *
     * @param tarjeta La tarjeta que se va a validar.
     * @return Un Either que contiene un error si la fecha es inválida, o un mensaje de éxito si es válida.
     */
    public Either<TarjetaError.FechaCaducidadInvalida, String> validarFechaCaducidad(Tarjeta tarjeta) {
        LocalDate fechaCaducidad = tarjeta.getFechaCaducidad();
        if (fechaCaducidad == null) {
            return Either.left(new TarjetaError.FechaCaducidadInvalida(null));
        }
        LocalDate fechaActual = LocalDate.now();
        if (fechaCaducidad.isBefore(fechaActual)) {
            return Either.left(new TarjetaError.FechaCaducidadInvalida(fechaCaducidad));
        }
        return Either.right("La fecha de caducidad es válida.");
    }

    /**
     * Valida el nombre del titular de la tarjeta.
     *
     * @param tarjeta La tarjeta que se va a validar.
     * @return Un Either que contiene un error si el nombre es inválido, o un mensaje de éxito si es válido.
     */
    public Either<TarjetaError.NombreTitularInvalido, String> validarNombreTitular(Tarjeta tarjeta) {
        String nombreTitular = tarjeta.getNombreTitular();
        if (nombreTitular == null || nombreTitular.trim().isEmpty()) {
            return Either.left(new TarjetaError.NombreTitularInvalido(nombreTitular));
        }
        return Either.right("El nombre del titular '" + nombreTitular + "' es válido.");
    }

    /**
     * Valida el ID de la tarjeta.
     *
     * @param tarjeta La tarjeta que se va a validar.
     * @return Un Either que contiene un error si el ID es inválidoo un mensaje de éxito si es válido.
     */
    public Either<TarjetaError.TarjetaIdInvalido, String> validarIdTarjeta(Tarjeta tarjeta) {
        if (tarjeta.getId() == null) {
            return Either.left(new TarjetaError.TarjetaIdInvalido(null));
        }
        String idTarjeta = String.valueOf(tarjeta.getId());
        if (idTarjeta.trim().isEmpty()) {
            return Either.left(new TarjetaError.TarjetaIdInvalido("El ID de la tarjeta es vacío."));
        }
        return Either.right("El ID de la tarjeta '" + idTarjeta + "' es válido.");
    }

    /**
     * Valida todos los atributos de la tarjeta.
     *
     * @param tarjeta La tarjeta que se va a validar.
     * @return true si todos los atributos son válidos, false en caso contrario.
     */
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