package org.example.clientes.validators;

import org.example.clientes.model.Tarjeta;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class TarjetaValidatorTest {
    private final TarjetaValidator validator = new TarjetaValidator();

    @Test
    void validarNumeroTarjeta() {
        String numeroTarjeta = "4532 7233 6544 2231";
        Tarjeta tarjeta = Tarjeta.builder()
                .numeroTarjeta(numeroTarjeta)
                .build();
        assertTrue(validator.validarNumeroTarjeta(tarjeta).isRight());
    }

    @Test
    void validarFechaCaducidad() {
        Tarjeta tarjeta = Tarjeta.builder()
                .fechaCaducidad(LocalDate.parse("2025-12-31"))
                .build();
        assertTrue(validator.validarFechaCaducidad(tarjeta).isRight());
    }

    @Test
    void validarNombreTitularValido() {
        Tarjeta tarjeta = Tarjeta.builder()
                .nombreTitular("Mario de Domingo")
                .build();
        assertTrue(validator.validarNombreTitular(tarjeta).isRight());
    }

    @Test
    void validarTarjetaCompletaValida() {
        Tarjeta tarjeta = Tarjeta.builder()
                .numeroTarjeta("4532 7233 6544 2231")
                .fechaCaducidad(LocalDate.parse("2025-12-31"))
                .nombreTitular("Mario de Domingo")
                .id(1L)
                .build();

        assertTrue(validator.validate(tarjeta));
    }

    @Test
    void invalidoNumeroTarjeta() {
        String numeroTarjeta = "4532723365442232";
        Tarjeta tarjeta = Tarjeta.builder()
                .numeroTarjeta(numeroTarjeta)
                .build();
        assertTrue(validator.validarNumeroTarjeta(tarjeta).isLeft());
        assertEquals("El número de tarjeta '" + numeroTarjeta + "' no es válido.",
                validator.validarNumeroTarjeta(tarjeta).getLeft().getMessage());
    }

    @Test
    void invalidaFechaCaducidad() {
        Tarjeta tarjeta = Tarjeta.builder()
                .fechaCaducidad(LocalDate.parse("2020-01-01"))
                .build();
        assertTrue(validator.validarFechaCaducidad(tarjeta).isLeft());
        assertEquals("La fecha de caducidad '" + tarjeta.getFechaCaducidad() + "' no es válida.",
                validator.validarFechaCaducidad(tarjeta).getLeft().getMessage());
    }

    @Test
    void invalidaNombreTitular() {
        Tarjeta tarjeta = Tarjeta.builder()
                .nombreTitular("") // Nombre vacío
                .build();
        assertTrue(validator.validarNombreTitular(tarjeta).isLeft());
        assertEquals("El nombre del titular '' no es válido.",
                validator.validarNombreTitular(tarjeta).getLeft().getMessage());
    }

    @Test
    void invalidaTarjeta() {
        String numeroTarjeta = "4532 7233 6544 2232"; // Número inválido
        Tarjeta tarjeta = Tarjeta.builder()
                .numeroTarjeta(numeroTarjeta)
                .fechaCaducidad(LocalDate.parse("2025-12-31"))
                .id(1L)
                .build();

        assertFalse(validator.validate(tarjeta)); // Se espera false porque el número es inválido
    }

    @Test
    void numeroTarjetaNulo() {
        Tarjeta tarjeta = Tarjeta.builder()
                .numeroTarjeta("") // Número vacío
                .build();
        assertTrue(validator.validarNumeroTarjeta(tarjeta).isLeft());
    }

    @Test
    void fechaCaducidadNula() {
        Tarjeta tarjeta = Tarjeta.builder()
                .fechaCaducidad(null)
                .build();
        assertTrue(validator.validarFechaCaducidad(tarjeta).isLeft());
        assertEquals("La fecha de caducidad 'null' no es válida.", validator.validarFechaCaducidad(tarjeta).getLeft().getMessage());
    }

    @Test
    void tarjetaNula() {
        Tarjeta tarjeta = Tarjeta.builder()
                .numeroTarjeta("") // Número inválido
                .fechaCaducidad(LocalDate.parse("2025-12-31"))
                .build();
        assertFalse(validator.validate(tarjeta)); // Se espera false por el número inválido
    }

    @Test
    void numeroCorrectoFechaIncorrecta() {
        String numeroTarjeta = "4532 7233 6544 2231";
        Tarjeta tarjeta = Tarjeta.builder()
                .numeroTarjeta(numeroTarjeta)
                .fechaCaducidad(LocalDate.parse("2020-01-01")) // Fecha en el pasado
                .build();
        assertFalse(validator.validate(tarjeta)); // Se espera false por la fecha inválida
    }

    @Test
    void validarIdTarjetaNulo() {
        Tarjeta tarjeta = Tarjeta.builder()
                .id(null)
                .numeroTarjeta("4532 7233 6544 2231")
                .fechaCaducidad(LocalDate.parse("2025-12-31"))
                .build();
        assertEquals("El ID de la tarjeta 'null' no es válido.",
                validator.validarIdTarjeta(tarjeta).getLeft().getMessage()
        );
        assertTrue(validator.validarIdTarjeta(tarjeta).isLeft());
    }

    @Test
    void testLuhnAlgorithm() {
        String numeroTarjeta = "4532 7233 6544 2232";
        assertFalse(validator.esValidoLuhn(numeroTarjeta));
    }

    @Test
    void validarNumeroTarjetaConEspacios() {
        String numeroTarjeta = "4532 7233 6544 2231";
        Tarjeta tarjeta = Tarjeta.builder()
                .numeroTarjeta(numeroTarjeta)
                .build();
        assertTrue(validator.validarNumeroTarjeta(tarjeta).isRight());
    }

    @Test
    void validarNumeroTarjetaSinEspacios() {
        String numeroTarjeta = "4532723365442231";
        Tarjeta tarjeta = Tarjeta.builder()
                .numeroTarjeta(numeroTarjeta)
                .build();
        assertTrue(validator.validarNumeroTarjeta(tarjeta).isRight());
    }

    @Test
    void numeroTarjetaConCaracteresNoNumericos() {
        String numeroTarjeta = "4532 7233 6544 ABCD"; // Caracteres no numéricos
        Tarjeta tarjeta = Tarjeta.builder()
                .numeroTarjeta(numeroTarjeta)
                .build();
        assertTrue(validator.validarNumeroTarjeta(tarjeta).isLeft());
    }

    @Test
    void fechaCaducidadConFormatoIncorrecto() {
        Tarjeta tarjeta = Tarjeta.builder()
                .fechaCaducidad(LocalDate.parse("2025-12-31"))
                .build();
        assertTrue(validator.validarFechaCaducidad(tarjeta).isRight());
    }

    @Test
    void validarTarjetaConNumeroValidoYFechaCaducidadPasada() {
        Tarjeta tarjeta = Tarjeta.builder()
                .numeroTarjeta("4532 7233 6544 2231")
                .fechaCaducidad(LocalDate.parse("2020-01-01")) // Fecha en el pasado
                .build();
        assertFalse(validator.validate(tarjeta)); // Se espera false
        assertEquals("La fecha de caducidad '" + tarjeta.getFechaCaducidad() + "' no es válida.",
                validator.validarFechaCaducidad(tarjeta).getLeft().getMessage());
    }
}
