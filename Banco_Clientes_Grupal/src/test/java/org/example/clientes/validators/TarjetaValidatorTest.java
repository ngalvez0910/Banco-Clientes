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
    void validarTarjeta() {
        String numeroTarjeta = "4532 7233 6544 2231";
        Tarjeta tarjeta = Tarjeta.builder()
                .numeroTarjeta(numeroTarjeta)
                .fechaCaducidad(LocalDate.parse("2025-12-31"))
                .id(1L)
                .build();

        assertTrue(validator.validate(tarjeta));
    }

    @Test
    void invalidoNumeroTarjeta() {
        String numeroTarjeta = "4532 7233 6544 2232";
        Tarjeta tarjeta = Tarjeta.builder()
                .numeroTarjeta(numeroTarjeta)
                .build();
        assertTrue(validator.validarNumeroTarjeta(tarjeta).isLeft());
    }

    @Test
    void invalidaFechaCaducidad() {
        Tarjeta tarjeta = Tarjeta.builder()
                .fechaCaducidad(LocalDate.parse("2020-01-01"))
                .build();
        assertTrue(validator.validarFechaCaducidad(tarjeta).isLeft());
    }

    @Test
    void invalidaTarjeta() {
        String numeroTarjeta = "4532 7233 6544 2232";
        Tarjeta tarjeta = Tarjeta.builder()
                .numeroTarjeta(numeroTarjeta)
                .fechaCaducidad(LocalDate.parse("2025-12-31"))
                .build();

        assertTrue(validator.validate(tarjeta));
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
    }

    @Test
    void tarjetaNula() {
        Tarjeta tarjeta = Tarjeta.builder()
                .numeroTarjeta("")
                .fechaCaducidad(LocalDate.parse("2025-12-31"))
                .build();
        assertTrue(validator.validate(tarjeta));
    }

    @Test
    void numeroCorrectoFechaIncorrecta() {
        String numeroTarjeta = "4532 7233 6544 2231";
        Tarjeta tarjeta = Tarjeta.builder()
                .numeroTarjeta(numeroTarjeta)
                .fechaCaducidad(LocalDate.parse("2020-01-01"))
                .build();
        assertTrue(validator.validate(tarjeta));
    }

    @Test
    void validarIdTarjetaNulo() {
        Tarjeta tarjeta = Tarjeta.builder()
                .id(null) // ID nulo
                .numeroTarjeta("4532 7233 6544 2231")
                .fechaCaducidad(LocalDate.parse("2025-12-31"))
                .build();
        assertTrue(validator.validate(tarjeta));
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
        String numeroTarjeta = "4532 7233 6544 ABCD";
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
    void fechaCaducidadConMesInvalido() {
        Tarjeta tarjeta = Tarjeta.builder()
                .fechaCaducidad(LocalDate.parse("2025-13-01"))
                .build();
        assertTrue(validator.validarFechaCaducidad(tarjeta).isLeft());
    }

    @Test
    void validarTarjetaConNumeroValidoYFechaInvalida() {
        Tarjeta tarjeta = Tarjeta.builder()
                .numeroTarjeta("4532 7233 6544 2231")
                .fechaCaducidad(LocalDate.parse("2020-01-01"))
                .build();
        assertTrue(validator.validate(tarjeta));
    }

    @Test
    void validarTarjetaConNumeroValidoYFechaCaducidadPasada() {
        Tarjeta tarjeta = Tarjeta.builder()
                .numeroTarjeta("4532 7233 6544 2231")
                .fechaCaducidad(LocalDate.parse("2020-01-01"))
                .build();
        assertTrue(validator.validate(tarjeta));
    }
}
