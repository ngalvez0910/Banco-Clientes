package org.example.clientes.validators;

import org.example.clientes.model.Usuario;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class UsuarioValidatorTest {

    private final UsuarioValidator validator = new UsuarioValidator();

    @Test
    void validarNombre() {
        String nombre = "Mario";
        Usuario usuario = Usuario.builder()
                .nombre(nombre)
                .build();
        assertTrue(validator.validarNombre(usuario).isRight());
    }

    @Test
    void validarUserName() {
        String userName = "wolverine307";
        Usuario usuario = Usuario.builder()
                .userName(userName)
                .build();
        assertTrue(validator.validarUserName(usuario).isRight());
    }

    @Test
    void validarEmail() {
        String email = "mario@gmail.com";
        Usuario usuario = Usuario.builder()
                .email(email)
                .build();
        assertTrue(validator.validarEmail(usuario).isRight());
    }

    @Test
    void validarUsuario() {
        Usuario usuario = Usuario.builder()
                .nombre("Mario")
                .userName("wolverine307")
                .email("mario@gmail.com")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        assertTrue(validator.validarUsuario(usuario));
    }

    @Test
    void validarNombreConEspacios() {
        String nombre = "Mario Alberto";
        Usuario usuario = Usuario.builder()
                .nombre(nombre)
                .build();
        assertTrue(validator.validarNombre(usuario).isRight());
    }

    @Test
    void validarUserNameConNumerosYLetras() {
        String userName = "superman123";
        Usuario usuario = Usuario.builder()
                .userName(userName)
                .build();
        assertTrue(validator.validarUserName(usuario).isRight());
    }

    @Test
    void validarEmailConSubdominios() {
        String email = "mario@sub.dominio.com";
        Usuario usuario = Usuario.builder()
                .email(email)
                .build();
        assertTrue(validator.validarEmail(usuario).isRight());
    }
    @Test
    void nombreDemasiadoCorto() {
        String nombre = "Ana";
        Usuario usuario = Usuario.builder()
                .nombre(nombre)
                .build();

        assertEquals("El nombre 'Ana' no es válido. Debe tener entre 4 y 80 caracteres y no debe contener números ni caracteres especiales.",
                validator.validarNombre(usuario).getLeft().getMessage());
        assertTrue(validator.validarNombre(usuario).isLeft());
    }

    @Test
    void nombreDemasiadoLargo() {
        String nombre = "A".repeat(81);
        Usuario usuario = Usuario.builder()
                .nombre(nombre)
                .build();

        assertEquals("El nombre '"+nombre+"' no es válido. Debe tener entre 4 y 80 caracteres y no debe contener números ni caracteres especiales.",
                validator.validarNombre(usuario).getLeft().getMessage());
        assertTrue(validator.validarNombre(usuario).isLeft());
    }

    @Test
    void nombreConCaracteresInvalidos() {
        String nombre = "Mario123";
        Usuario usuario = Usuario.builder()
                .nombre(nombre)
                .build();

        assertEquals("El nombre 'Mario123' no es válido. Debe tener entre 4 y 80 caracteres y no debe contener números ni caracteres especiales.",
                validator.validarNombre(usuario).getLeft().getMessage());
        assertTrue(validator.validarNombre(usuario).isLeft());
    }

    @Test
    void userNameDemasiadoCorto() {
        String userName = "abc";
        Usuario usuario = Usuario.builder()
                .userName(userName)
                .build();

        assertEquals("El nombre de usuario 'abc' no es válido. Debe tener entre 4 y 30 caracteres.",
                validator.validarUserName(usuario).getLeft().getMessage());
        assertTrue(validator.validarUserName(usuario).isLeft());
    }

    @Test
    void userNameDemasiadoLargo() {
        String userName = "a".repeat(31);
        Usuario usuario = Usuario.builder()
                .userName(userName)
                .build();

        assertEquals("El nombre de usuario '"+userName+"' no es válido. Debe tener entre 4 y 30 caracteres.",
                validator.validarUserName(usuario).getLeft().getMessage());
        assertTrue(validator.validarUserName(usuario).isLeft());
    }

    @Test
    void emailFormatoInvalido() {
        String email = "mario_at_gmail.com";
        Usuario usuario = Usuario.builder()
                .email(email)
                .build();

        assertEquals("El correo electrónico 'mario_at_gmail.com' no es válido.",
                validator.validarEmail(usuario).getLeft().getMessage());
        assertTrue(validator.validarEmail(usuario).isLeft());
    }

    @Test
    void emailNulo() {
        Usuario usuario = Usuario.builder()
                .email(null)
                .build();

        assertEquals("El correo electrónico 'null' no es válido.",
                validator.validarEmail(usuario).getLeft().getMessage());
        assertTrue(validator.validarEmail(usuario).isLeft());
    }

    @Test
    void validarUsuarioConNombreInvalido() {
        Usuario usuario = Usuario.builder()
                .nombre("Ana")
                .userName("wolverine307")
                .email("mario@gmail.com")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        assertFalse(validator.validarUsuario(usuario));
        assertEquals("El nombre 'Ana' no es válido. Debe tener entre 4 y 80 caracteres y no debe contener números ni caracteres especiales.",
                validator.validarNombre(usuario).getLeft().getMessage());
    }

    @Test
    void validarUsuarioConUserNameInvalido() {
        Usuario usuario = Usuario.builder()
                .nombre("Mario")
                .userName("a")
                .email("mario@gmail.com")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        assertFalse(validator.validarUsuario(usuario));
        assertEquals("El nombre de usuario 'a' no es válido. Debe tener entre 4 y 30 caracteres.",
                validator.validarUserName(usuario).getLeft().getMessage());
    }

    @Test
    void validarUsuarioConEmailInvalido() {
        Usuario usuario = Usuario.builder()
                .nombre("Mario")
                .userName("wolverine307")
                .email("mario_at_gmail.com")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        assertFalse(validator.validarUsuario(usuario));
        assertEquals("El correo electrónico 'mario_at_gmail.com' no es válido.",
                validator.validarEmail(usuario).getLeft().getMessage());
    }

}
