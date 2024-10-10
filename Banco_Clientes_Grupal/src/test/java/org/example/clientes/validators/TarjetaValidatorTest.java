package org.example.clientes.validators;

import org.example.clientes.model.Tarjeta;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;

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
                .createdAt(LocalDateTime.now().minusDays(1))
                .updatedAt(LocalDateTime.now())
                .build();

        assertTrue(validator.validate(tarjeta));
    }

    @Test
    void validarTarjetaConUpdatedAtInvalido() {
        Tarjeta tarjeta = Tarjeta.builder()
                .numeroTarjeta("4532 7233 6544 2231")
                .fechaCaducidad(LocalDate.parse("2025-12-31"))
                .nombreTitular("Juan Pérez")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now().minusDays(1))
                .build();

        assertFalse(validator.validate(tarjeta));
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
                .nombreTitular("")
                .build();
        assertTrue(validator.validarNombreTitular(tarjeta).isLeft());
        assertEquals("El nombre del titular '' no es válido.",
                validator.validarNombreTitular(tarjeta).getLeft().getMessage());
    }

    @Test
    void invalidaTarjeta() {
        String numeroTarjeta = "4532 7233 6544 2232";
        Tarjeta tarjeta = Tarjeta.builder()
                .numeroTarjeta(numeroTarjeta)
                .fechaCaducidad(LocalDate.parse("2025-12-31"))
                .id(1L)
                .build();

        assertFalse(validator.validate(tarjeta));
    }

    @Test
    void numeroTarjetaNulo() {
        Tarjeta tarjeta = Tarjeta.builder()
                .numeroTarjeta("")
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
                .numeroTarjeta("")
                .fechaCaducidad(LocalDate.parse("2025-12-31"))
                .build();
        assertFalse(validator.validate(tarjeta));
    }

    @Test
    void numeroCorrectoFechaIncorrecta() {
        String numeroTarjeta = "4532 7233 6544 2231";
        Tarjeta tarjeta = Tarjeta.builder()
                .numeroTarjeta(numeroTarjeta)
                .fechaCaducidad(LocalDate.parse("2020-01-01"))
                .build();
        assertFalse(validator.validate(tarjeta));
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
    void testLuhnAlgorithmTrue() {
        String numeroTarjeta = "4532723365442231";
        assertTrue(validator.esValidoLuhn(numeroTarjeta));
    }

    @Test
    void testLuhnAlgorithmFalse() {
        String numeroTarjeta = "4532 7233 6544 2233";
        assertFalse(validator.esValidoLuhn(numeroTarjeta));
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
        String numeroTarjeta = "4532 7233 6544 ABCD";
        Tarjeta tarjeta = Tarjeta.builder()
                .numeroTarjeta(numeroTarjeta)
                .build();
        assertTrue(validator.validarNumeroTarjeta(tarjeta).isLeft());
    }

    @Test
    void validarTarjetaConNumeroValidoYFechaCaducidadPasada() {
        Tarjeta tarjeta = Tarjeta.builder()
                .numeroTarjeta("4532 7233 6544 2231")
                .fechaCaducidad(LocalDate.parse("2020-01-01"))
                .build();
        assertFalse(validator.validate(tarjeta));
        assertEquals("La fecha de caducidad '" + tarjeta.getFechaCaducidad() + "' no es válida.",
                validator.validarFechaCaducidad(tarjeta).getLeft().getMessage());
    }
}
