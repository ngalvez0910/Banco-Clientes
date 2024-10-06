package org.example.rest.service;

import io.vavr.control.Either;

import org.example.errors.UserError;
import org.example.errors.UserNoUsersFound;
import org.example.errors.UserNotFoundError;
import org.example.exceptions.UserNoUsersFoundException;
import org.example.exceptions.UserNotFoundException;
import org.example.rest.repository.UserRemoteRepository;
import org.example.usuarios.models.Usuario;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

public class UserService {

    private final UserRemoteRepository userRepository;
    private final Logger logger = LoggerFactory.getLogger(UserService.class);

    public UserService(UserRemoteRepository userRepository) { this.userRepository = userRepository; }

    public Either<UserError, List<Usuario>> getAllAsyncEither() {
        logger.debug("UserService: Recuperando todos los usuarios");
        CompletableFuture<List<Usuario>> completableFuture = CompletableFuture.supplyAsync(()->
                userRepository.getAllSync());
        try {
            return Either.right(completableFuture.get(100,MILLISECONDS));
        } catch (UserNoUsersFoundException e) {
            return Either.left(new UserNoUsersFound());
        } catch (Exception e) {
            return Either.left(new UserError("Error obteniendo la lista de usuarios", 500));
        }
    }
    public List<Usuario> getAllAsync() {
        // Es el Sync pero lanzado con un CompletableFuture
        CompletableFuture<List<Usuario>> completableFuture = CompletableFuture.supplyAsync(userRepository::getAllSync);
        // Se debe hacer el try catch y las cosas necesarias
        try {
            return completableFuture.get(1000, MILLISECONDS);
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }

    }

    public Either<UserError, Usuario> getByIdAsyncEither(int id) {
        logger.debug("UserService: Recuperando el usuario con id " + id);
        CompletableFuture<Usuario> completableFuture = CompletableFuture.supplyAsync(() ->
                userRepository.getByIdSync(id));
        try {
            return Either.right(completableFuture.get(100, MILLISECONDS));
        } catch (UserNotFoundException e) {
            return Either.left(new UserNotFoundError(id));
        } catch (Exception e) {
            return Either.left(new UserError("Error obteniendo usuario", 500));
        }
    }

}
