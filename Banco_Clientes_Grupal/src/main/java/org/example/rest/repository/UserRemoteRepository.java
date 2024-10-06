package org.example.rest.repository;

import org.example.exceptions.UserNotFoundException;
import org.example.rest.UserApiRest;
import org.example.usuarios.mappers.UsuarioMapper;
import org.example.usuarios.models.Usuario;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class UserRemoteRepository {

    private final UserApiRest userApiRest;
    private final Logger logger = LoggerFactory.getLogger(UserRemoteRepository.class);

    public UserRemoteRepository(UserApiRest userApiRest) {
        this.userApiRest = userApiRest;
    }

    public List<Usuario> getAllSync(){
        var call = userApiRest.getAllSync();
        try {
            var response = call.execute();
            if (!response.isSuccessful()) {
                throw new Exception("Error: " + response.code());
            }
            return response.body().getData().stream()
                    .map(UsuarioMapper::toUserFromCreate)
                    .toList();
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }

    public Usuario getByIdSync(int id) {
        logger.debug("UserRemoteRepository: Recuperando el usuario con id" + id);
        var call = userApiRest.getByIdSync(id);
        try {
            var response = call.execute();
            if (!response.isSuccessful()) {
                // con el codigo podemos saber que ha pasado
                // throw new Exception("Error: " + response.code()); // Aquí deberíamos lanzar una excepción
                if (response.code() == 404) {
                    throw new UserNotFoundException("User not found with id: " + id);
                } else {
                    throw new Exception("Error: " + response.code());
                }
            }
            return UsuarioMapper.toUserFromCreate(response.body().getData());
        } catch (Exception e) {
            throw new UserNotFoundException("User not found with id: " + id);
        }
    }
}
