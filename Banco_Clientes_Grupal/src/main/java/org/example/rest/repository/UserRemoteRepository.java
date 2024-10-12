package org.example.rest.repository;

import org.example.clientes.mappers.UsuarioMapper;
import org.example.clientes.model.Usuario;
import org.example.exceptions.UserNotFoundException;
import org.example.rest.UserApiRest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

public class UserRemoteRepository {

    private final UserApiRest userApiRest;
    private final Logger logger = LoggerFactory.getLogger(UserRemoteRepository.class);

    public UserRemoteRepository(UserApiRest userApiRest) {
        this.userApiRest = userApiRest;
    }

    public List<Usuario> getAllSyncSinOptional(){
        logger.debug("UserRemoteRepository: Devolviendo todos los usuarios de la API");
        var call = userApiRest.getAllSync();
        try {
            var response = call.execute();
            if (!response.isSuccessful()) {
                throw new Exception("Error: " + response.code());
            }
            return response.body().stream()
                    .map(UsuarioMapper::toUserFromCreate)
                    .toList();
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }

    public Optional<List<Usuario>> getAllSync(){
        logger.debug("UserRemoteRepository: Devolviendo todos los usuarios de la API");
        var call = userApiRest.getAllSync();
        try {
            var response = call.execute();
            if (!response.isSuccessful()) {
                logger.error("Error: " + response.code());
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
            logger.error("Error recuperando todos los usuarios de la API", e);
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
                    return Optional.empty();
                } else {
                    throw new Exception("Error: " + response.code());
                }
            }
            return Optional.of(UsuarioMapper.toUserFromCreate(response.body()));
        } catch (Exception e) {
            logger.error("Error fetching user with id: " + id, e);
            return Optional.empty();
        }
    }

    public Usuario getByIdSyncSinOptional(long id) {
        logger.debug("UserRemoteRepository: Recuperando el usuario con id " + id);
        var call = userApiRest.getByIdSync(id);
        try {
            var response = call.execute();
            if (!response.isSuccessful()) {
                if (response.code() == 404) {
                    throw new UserNotFoundException("User not found with id: " + id);
                } else {
                    throw new Exception("Error: " + response.code());
                }
            }
            return UsuarioMapper.toUserFromCreate(response.body());
        } catch (Exception e) {
            throw new UserNotFoundException("User not found with id: " + id);
        }
    }
    public Usuario createUserSync(Usuario user) {
        logger.debug("UserRemoteRepository: Creando un nuevo usuario con username: " + user.getUserName());
        var call = userApiRest.createUserSync(UsuarioMapper.toRequest(user));
        try {
            var response = call.execute();
            if (!response.isSuccessful()) {
                throw new Exception("Error: " + response.code());
            }
            return UsuarioMapper.toUserFromCreate(response.body());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Usuario updateUserSync(long id, Usuario user) {
        logger.debug("UserRemoteRepository: Actualizando al usuario con id " + id);
        var call = userApiRest.updateUserSync(id, UsuarioMapper.toRequest(user));
        try {
            var response = call.execute();
            if (!response.isSuccessful()) {
                if (response.code() == 404) {
                    throw new UserNotFoundException("Usuario no encontrado al actualizar con id: " + id);
                } else {
                    throw new Exception("Error: " + response.code());
                }
            }
            return UsuarioMapper.toUserFromCreate(response.body());
        } catch (Exception e) {
            throw new UserNotFoundException("Usuario no encontrado al actualizar con id: " + id);
        }
    }
    public Usuario deleteUserSync(long id){
        logger.debug("UserRemoteRepository: Eliminando al usuario con id " + id);
        var call = userApiRest.deleteUserSync(id);
        try {
            var response = call.execute();
            if (!response.isSuccessful()) {
                if (response.code() == 404) {
                    throw new UserNotFoundException("Usuario no encontrado al eliminar con id: " + id);
                } else {
                    throw new Exception("Error: " + response.code());
                }
            }
            return UsuarioMapper.toUserFromDelete(response.body());
        } catch (Exception e) {
            throw new UserNotFoundException("Usuario no encontrado al eliminar con id: " + id);
        }


    }

}
