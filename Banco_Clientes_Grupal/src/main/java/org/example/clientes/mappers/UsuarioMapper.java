package org.example.clientes.mappers;

import org.example.clientes.dto.UsuarioDto;
import org.example.clientes.model.Usuario;

import org.example.rest.responses.getAll.UserGetAll;
import org.example.rest.responses.getById.UserGetById;
import org.example.rest.responses.createUpdateDelete.Request;
import org.example.rest.responses.createUpdateDelete.Response;

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

    public static Usuario toUserFromCreate(Response response) {
        return Usuario.builder()
                .id(Long.parseLong(response.getId()))
                .nombre(response.getName())
                .userName(response.getUserName())
                .email(response.getEmail())
                //.createdAt(LocalDateTime.parse(response.getCreatedAt()))
                //.updatedAt(LocalDateTime.parse(response.getUpdatedAt()))
                .build();
    }

    public static Usuario toUserFromUpdate(Response response, int id) {
        return Usuario.builder()
                .id((long) id)
                .nombre(response.getName())
                .userName(response.getUserName())
                .email(response.getEmail())
                //.createdAt(LocalDateTime.parse(response.getCreatedAt()))
                //.updatedAt(LocalDateTime.parse(response.getUpdatedAt()))
                .build();
    }
}
