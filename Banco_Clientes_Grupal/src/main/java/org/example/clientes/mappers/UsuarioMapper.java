package org.example.clientes.mappers;

import org.example.clientes.dto.UsuarioDto;
import org.example.clientes.model.Usuario;

import org.example.rest.responses.createUpdateDelete.UserCreate;
import org.example.rest.responses.createUpdateDelete.UserDelete;
import org.example.rest.responses.getAll.UserGetAll;
import org.example.rest.responses.getById.UserGetById;
import org.example.rest.responses.createUpdateDelete.Request;

public class UsuarioMapper {
    public static Usuario toEntity(UsuarioDto usuarioDto) {
        if (usuarioDto == null) {
            return null;
        }

        return Usuario.builder()
                .id(usuarioDto.getId())
                .nombre(usuarioDto.getNombre())
                .userName(usuarioDto.getUserName())
                .email(usuarioDto.getEmail())
                .build();
    }

    public static UsuarioDto toDto(Usuario usuario) {
        if (usuario == null) {
            return null;
        }

        return UsuarioDto.builder()
                .id(usuario.getId())
                .nombre(usuario.getNombre())
                .userName(usuario.getUserName())
                .email(usuario.getEmail())
                .build();
    }
    public static Usuario toUserFromCreate(UserGetAll userGetAll) {
        return Usuario.builder()
                .id((long) userGetAll.getId())
                .nombre (userGetAll.getName())
                .userName(userGetAll.getUsername())
                .email(userGetAll.getEmail())
                .build();
    }

    public static Usuario toUserFromCreate(UserCreate userCreate) {
        return Usuario.builder()
                .id((long) userCreate.getId())
                .nombre (userCreate.getName())
                .userName(userCreate.getUsername())
                .email(userCreate.getEmail())
                .build();
    }

    public static Usuario toUserFromCreate(UserGetById userGetById) {
        return Usuario.builder()
                .id((long) userGetById.getId())
                .nombre(userGetById.getName())
                .userName(userGetById.getUsername())
                .email(userGetById.getEmail())
                .build();
    }

    public static Request toRequest(Usuario user) {
        return Request.builder()
                .name(user.getNombre())
                .username(user.getUserName())
                .email(user.getEmail())
                .build();
    }

    public static Usuario toUserFromDelete(UserDelete body) {
        return Usuario.builder()
               .build();
    }
}
