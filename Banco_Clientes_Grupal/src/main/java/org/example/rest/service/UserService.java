package org.example.rest.service;

import io.vavr.control.Either;

import org.example.clientes.model.Usuario;
import org.example.rest.errors.UserApiError;
import org.example.exceptions.UserNoUsersFoundException;
import org.example.rest.repository.UserRemoteRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

public class UserService {

    private final UserRemoteRepository userRepository;
    private final Logger logger = LoggerFactory.getLogger(UserService.class);

    public UserService(UserRemoteRepository userRepository) { this.userRepository = userRepository; }

/*    public Either<UserApiError, List<Usuario>> getAllAsync() {
        logger.debug("UserService: Recuperando todos los usuarios");
        CompletableFuture<List<Usuario>> completableFuture = CompletableFuture.supplyAsync(()->
                userRepository.getAllSync());
        try {
            return Either.right(completableFuture.get(10000,MILLISECONDS));
        } catch (UserNoUsersFoundException e) {
            return Either.left(new UserApiError.UserApiNoUsersFound());
        } catch (Exception e) {
            return Either.left(new UserApiError("Error obteniendo la lista de usuarios", 500));
        }
    }*/

    public Either<UserApiError, List<Usuario>> getAllAsync() {
        logger.debug("UserService: Recuperando todos los usuarios");
        CompletableFuture<Optional<List<Usuario>>> completableFuture = CompletableFuture.supplyAsync(userRepository::getAllSync);
        try {
            Optional<List<Usuario>> usuariosOptional = completableFuture.get(10000, MILLISECONDS);
            if (usuariosOptional.isPresent()) {
                return Either.right(usuariosOptional.get());
            } else {
                return Either.left(new UserApiError.UserApiNoUsersFound());
            }
        } catch (Exception e) {
            return Either.left(new UserApiError("Error obteniendo la lista de usuarios", 500));
        }
    }

    public Either<UserApiError, Usuario> getByIdAsync(int id) {
        logger.debug("UserService: Recuperando el usuario con id " + id);
        CompletableFuture<Usuario> completableFuture = CompletableFuture.supplyAsync(() ->
                userRepository.getByIdSync(id));
        try {
            return Either.right(completableFuture.get(10000, MILLISECONDS));
        } catch (Exception e) {
            return Either.left(new UserApiError.UserNotFoundApiError(id));
        }
    }

    public Either<UserApiError, Usuario> createUserAsync (Usuario user){
        logger.debug("UserService: Guardando el usuario con id " + user.getId());
        CompletableFuture<Usuario> completableFuture = CompletableFuture.supplyAsync(
                ()-> userRepository.createUserSync(user));
        try{
            return Either.right(completableFuture.get(10000,MILLISECONDS));
        } catch (Exception e) {
            return Either.left(new UserApiError.UserApiNotCreated(user.getId()));
        }
    }

    public Either<UserApiError, Usuario> updateUserAsync (int id, Usuario user){
        logger.debug("UserService: Actualizando el usuario con id " + id);
        CompletableFuture<Usuario> completableFuture = CompletableFuture.supplyAsync(() ->
                userRepository.updateUserSync(id, user));
        try{
            return Either.right(completableFuture.get(10000, MILLISECONDS));
        } catch (Exception e) {
            return Either.left(new UserApiError.UserApiNotUpdatedError(id));
        }
    }

    public Either<UserApiError, Usuario> deleteUserAsync(int id){
        logger.debug("UserService: Eliminando el usuario con id " + id);
        CompletableFuture<Usuario> completableFuture = CompletableFuture.supplyAsync(
                ()-> userRepository.deleteUserSync(id));
        try{
            return Either.right(completableFuture.get(10000, MILLISECONDS));
        } catch (Exception e) {
            return Either.left(new UserApiError.UserApiNotDeletedError(id));
        }
    }
}
