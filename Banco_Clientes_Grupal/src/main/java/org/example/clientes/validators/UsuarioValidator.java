package org.example.clientes.validators;

import io.vavr.control.Either;
import org.example.clientes.errors.UsuarioError;
import org.example.clientes.model.Usuario;

import java.util.regex.Pattern;

public class UsuarioValidator {

    public Either<UsuarioError.NombreInvalido, String> validarNombre(Usuario usuario) {
        String nombre = usuario.getNombre();
        if (nombre == null || nombre.length() < 4 || nombre.length() > 80 || !nombre.matches("^[a-zA-Z\\s]+$")) {
            return Either.left(new UsuarioError.NombreInvalido(nombre));
        }
        return Either.right("El nombre es válido.");
    }

    public Either<UsuarioError.UserNameInvalido, String> validarUserName(Usuario usuario) {
        String userName = usuario.getUserName();
        if (userName == null || userName.length() < 4 || userName.length() > 30) {
            return Either.left(new UsuarioError.UserNameInvalido(userName));
        }
        return Either.right("El nombre de usuario es válido.");
    }

    public Either<UsuarioError.EmailInvalido, String> validarEmail(Usuario usuario) {
        Pattern emailRegex =
                Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
        String email = usuario.getEmail();
        if (email == null || !emailRegex.matcher(email).matches()) {
            return Either.left(new UsuarioError.EmailInvalido(email));
        }
        return Either.right("El correo electrónico es válido.");
    }

    public boolean validarUsuario(Usuario usuario) {
        Either<UsuarioError.NombreInvalido, String> nombre = validarNombre(usuario);
        if (nombre.isLeft()) {
            return false;
        }
        Either<UsuarioError.UserNameInvalido, String> userName = validarUserName(usuario);
        if (userName.isLeft()) {
            return false;
        }
        Either<UsuarioError.EmailInvalido, String> email = validarEmail(usuario);
        if (email.isLeft()) {
            return false;
        }
        return true;
    }
}

