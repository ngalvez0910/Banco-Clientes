package org.example.rest.repository;

import org.example.clientes.mappers.UsuarioMapper;
import org.example.clientes.model.Usuario;
import org.example.rest.UserApiRest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class UserRemoteRepository {

    private final UserApiRest userApiRest;
    private final Logger logger = LoggerFactory.getLogger(UserRemoteRepository.class);

    public UserRemoteRepository(UserApiRest userApiRest) {
        this.userApiRest = userApiRest;
    }

    public Optional<List<Usuario>> getAllSync(){
        logger.debug("UserRemoteRepository: Devolviendo todos los usuarios de la API");
        var call = userApiRest.getAllSync();
        try {
            var response = call.execute();
            if (!response.isSuccessful()) {
                logger.error("Error recuperando todos los usuarios: " + response.code());
                return Optional.empty();
            }
            var body = response.body();
            if (body != null) {
                return Optional.of(body.stream()
                        .map(UsuarioMapper::toUserFromCreate)
                        .toList());
            } else {
                logger.error("Response body es null");
                return Optional.empty();
            }
        } catch (Exception e) {
            logger.error("Error recuperando todos los usuarios de la API", e.getMessage());
            return Optional.empty();
        }
    }

    public Optional<Usuario> getByIdSync(long id) {
        logger.debug("UserRemoteRepository: Recuperando el usuario con id " + id);
        var call = userApiRest.getByIdSync(id);
        try {
            var response = call.execute();
            if (!response.isSuccessful()) {
                if (response.code() == 404) {
                    logger.debug("UserRemoteRepository: Error, usuario no encontrado con id " + id);
                    return Optional.empty();
                } else {
                    throw new Exception("Error: " + response.code());
                }
            }
            return Optional.of(UsuarioMapper.toUserFromCreate(response.body()));
        } catch (Exception e) {
            logger.error("Error recuperando el usuario con id: " + id, e.getMessage());
            return Optional.empty();
        }
    }

    public Optional<Usuario> createUserSync(Usuario user) {
        logger.debug("UserRemoteRepository: Creando un nuevo usuario con username: " + user.getUserName());
        var call = userApiRest.createUserSync(UsuarioMapper.toRequest(user));
        try {
            var response = call.execute();
            if (!response.isSuccessful()) {
                if (response.code() == 500) {
                    logger.debug("UserRemoteRepository: Error interno servidor" + response.code());
                    return Optional.empty();
                } else {
                    logger.debug("UserRemoteRepository: Error creando usuario " + response.code());
                    return Optional.empty();
                }
            }
            var timeStamp = LocalDateTime.now();
            return Optional.of(UsuarioMapper.toUserFromCreate(response.body(),timeStamp,timeStamp));
        } catch (Exception e) {
            logger.debug("UserRemoteRepository: Error creando usuario" + user + e.getMessage());
            return Optional.empty();
        }
    }

    public Optional<Usuario> updateUserSync(long id, Usuario user) {
        logger.debug("UserRemoteRepository: Actualizando al usuario con id " + id);
        var call = userApiRest.updateUserSync(id, UsuarioMapper.toRequest(user));
        try {
            var response = call.execute();
            if (!response.isSuccessful()) {
                if (response.code() == 404) {
                    logger.debug("UserRemoteRepository: Error, usuario no encontrado al actualizar con id " + id);
                    return Optional.empty();
                } else {
                    logger.debug("UserRemoteRepository: Error actualizando usuario " + response.code());
                    return Optional.empty();
                }
            }
            var timeStamp = LocalDateTime.now();
            return Optional.of(UsuarioMapper.toUserFromCreate(response.body(), timeStamp));
        } catch (Exception e) {
            logger.debug("UserRemoteRepository: Error actualizando usuario con id: " + id + e.getMessage());
            return Optional.empty();
        }
    }

    public Optional<Usuario> deleteUserSync(long id){
        logger.debug("UserRemoteRepository: Eliminando al usuario con id " + id);
        var call = userApiRest.deleteUserSync(id);
        try {
            var response = call.execute();
            if (!response.isSuccessful()) {
                if (response.code() == 404) {
                    logger.debug("UserRemoteRepository: Error, usuario no encontrado al eliminar con id " + id);
                    return Optional.empty();
                } else {
                    logger.debug("UserRemoteRepository: Error eliminando usuario " + response.code());
                    return Optional.empty();
                }
            }
            return Optional.of(UsuarioMapper.toUserFromDelete(response.body()));
        } catch (Exception e) {
            logger.debug("UserRemoteRepository: Error eliminando usuario con id: " + id + e.getMessage());
            return Optional.empty();
        }
    }
}
