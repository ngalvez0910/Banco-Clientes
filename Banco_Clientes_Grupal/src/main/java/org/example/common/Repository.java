package org.example.common;

import io.vavr.control.Either;
import io.vavr.control.Option;

import java.util.List;
import java.util.Optional;

/**
 * Interfaz genérica que define los métodos principales de un repositorio para
 * gestionar entidades.
 *
 * @param <T>  El tipo de la entidad gestionada por el repositorio
 * @param <ID> El tipo de identificador utilizado para las entidades
 *
 * @author Jaime León, Natalia González, German Fernandez, Alba García, Mario de Domingo
 * @version 1.0-SNAPSHOT
 */
public interface Repository<T, ID> {

    /**
     * Obtiene una lista con todas las entidades almacenadas en el repositorio.
     *
     * @return Lista de todas las entidades.
     */
    List<T> getAll();

    /**
     * Busca una entidad por su identificador.
     *
     * @param id El identificador de la entidad que se desea obtener
     * @return Un Optional que contiene la entidad si se encuentra, o vacío si no existe
     */
    Optional<T> getById(ID id);

    /**
     * Crea una nueva entidad en el repositorio.
     *
     * @param entity La entidad que se desea crear
     * @return La entidad creada
     */
    T create(T entity);

    /**
     * Actualiza una entidad existente en el repositorio.
     *
     * @param id     El identificador de la entidad que se desea actualizar
     * @param entity La entidad con los nuevos valores a actualizar
     * @return La entidad actualizada
     */
    T update(ID id, T entity);

    /**
     * Elimina una entidad del repositorio por su identificador.
     *
     * @param id El identificador de la entidad que se desea eliminar
     * @return true si la eliminación fue exitosa, false en caso contrario
     */
    Boolean delete(ID id);
}
