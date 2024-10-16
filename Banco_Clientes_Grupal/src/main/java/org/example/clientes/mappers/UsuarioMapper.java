package org.example.clientes.mappers;

import org.example.clientes.dto.UsuarioDto;
import org.example.clientes.model.Usuario;

import org.example.rest.responses.createUpdateDelete.UserCreate;
import org.example.rest.responses.createUpdateDelete.UserDelete;
import org.example.rest.responses.getAll.UserGetAll;
import org.example.rest.responses.getById.UserGetById;
import org.example.rest.responses.createUpdateDelete.Request;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Clase encargada de mapear entre las entidades Usuario y UsuarioDto,
 * así como entre diferentes objetos de respuesta relacionados con el usuario.
 *
 * @author Jaime León, Natalia González, German Fernandez, Alba García, Mario de Domingo
 */
public class UsuarioMapper {
    /**
     * Convierte un objeto UsuarioDto en un objeto Usuario.
     *
     * @param usuarioDto El objeto UsuarioDto a convertir
     * @return El objeto Usuario resultante, o null si el objeto UsuarioDto es null
     */
    public static Usuario toEntity(UsuarioDto usuarioDto) {
        if (usuarioDto == null) {
            return null;
        }

        return Usuario.builder()
                .id(usuarioDto.getId())
                .nombre(usuarioDto.getNombre())
                .userName(usuarioDto.getUserName())
                .email(usuarioDto.getEmail())
                .createdAt(LocalDateTime.parse(usuarioDto.getCreatedAt()))
                .updatedAt(LocalDateTime.parse(usuarioDto.getUpdatedAt()))
                .build();
    }

    /**
     * Convierte un objeto Usuario en un objeto UsuarioDto.
     *
     * @param usuario El objeto Usuario a convertir
     * @return El objeto UsuarioDto resultante, o null si el objeto Usuario es null
     */
    public static UsuarioDto toDto(Usuario usuario) {
        if (usuario == null) {
            return null;
        }

        return UsuarioDto.builder()
                .id(usuario.getId())
                .nombre(usuario.getNombre())
                .userName(usuario.getUserName())
                .email(usuario.getEmail())
                .createdAt(usuario.getCreatedAt().toString())
                .updatedAt(usuario.getUpdatedAt().toString())
                .build();
    }

    /**
     * Convierte un objeto UserGetAll en un objeto Usuario.
     *
     * @param userGetAll El objeto UserGetAll a convertir
     * @return El objeto Usuario resultante
     */
    public static Usuario toUserFromCreate(UserGetAll userGetAll) {
        return Usuario.builder()
                .id((long) userGetAll.getId())
                .nombre(userGetAll.getName())
                .userName(userGetAll.getUsername())
                .email(userGetAll.getEmail())
                .build();
    }

    /**
     * Convierte un objeto UserCreate en un objeto Usuario.
     *
     * @param userCreate El objeto UserCreate a convertir
     * @return El objeto Usuario resultante
     */
    public static Usuario toUserFromCreate(UserCreate userCreate) {
        return Usuario.builder()
                .id((long) userCreate.getId())
                .nombre(userCreate.getName())
                .userName(userCreate.getUsername())
                .email(userCreate.getEmail())
                .build();
    }

    /**
     * Convierte un objeto UserCreate en un objeto Usuario, incluyendo fechas de creación y actualización.
     *
     * @param userCreate El objeto UserCreate a convertir
     * @param createdAt La fecha de creación a establecer
     * @param updatedAt La fecha de actualización a establecer
     * @return El objeto Usuario resultante
     */
    public static Usuario toUserFromCreate(UserCreate userCreate, LocalDateTime createdAt, LocalDateTime updatedAt) {
        return Usuario.builder()
                .id((long) userCreate.getId())
                .nombre(userCreate.getName())
                .userName(userCreate.getUsername())
                .email(userCreate.getEmail())
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .build();
    }

    /**
     * Convierte un objeto UserCreate en un objeto Usuario, incluyendo solo la fecha de actualización.
     *
     * @param userCreate El objeto UserCreate a convertir
     * @param updatedAt La fecha de actualización a establecer
     * @return El objeto Usuario resultante
     */
    public static Usuario toUserFromCreate(UserCreate userCreate, LocalDateTime updatedAt) {
        return Usuario.builder()
                .id((long) userCreate.getId())
                .nombre(userCreate.getName())
                .userName(userCreate.getUsername())
                .email(userCreate.getEmail())
                .updatedAt(updatedAt)
                .build();
    }

    /**
     * Convierte un objeto UserGetById en un objeto Usuario.
     *
     * @param userGetById El objeto UserGetById a convertir
     * @return El objeto Usuario resultante
     */
    public static Usuario toUserFromCreate(UserGetById userGetById) {
        return Usuario.builder()
                .id((long) userGetById.getId())
                .nombre(userGetById.getName())
                .userName(userGetById.getUsername())
                .email(userGetById.getEmail())
                .build();
    }

    /**
     * Convierte un objeto Usuario en un objeto Request.
     *
     * @param user El objeto Usuario a convertir
     * @return El objeto Request resultante
     */
    public static Request toRequest(Usuario user) {
        return Request.builder()
                .name(user.getNombre())
                .username(user.getUserName())
                .email(user.getEmail())
                .build();
    }

    /**
     * Convierte un objeto UserDelete en un objeto Usuario.
     *
     * @param body El objeto UserDelete a convertir
     * @return El objeto Usuario resultante
     */
    public static Usuario toUserFromDelete(UserDelete body) {
        return Usuario.builder()
                .build();
    }
}
