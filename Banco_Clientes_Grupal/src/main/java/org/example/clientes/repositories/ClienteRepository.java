package org.example.clientes.repositories;

import org.example.clientes.model.Cliente;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Optional;

/**
 * Interfaz que define las operaciones CRUD para el manejo de clientes.
 *
 * @author Jaime León, Natalia González, German Fernandez, Alba García, Mario de Domingo
 * @version 1.0-SNAPSHOT
 */
public interface ClienteRepository {

    /**
     * Obtiene una lista de todos los clientes.
     *
     * @return Lista de {@link Cliente} que representan todos los clientes en la base de datos.
     */
    List<Cliente> getAll();

    /**
     * Obtiene un cliente por su ID.
     *
     * @param id ID del cliente que se desea obtener.
     * @return Un objeto {@link Optional} que contiene el cliente si se encuentra, o vacío si no.
     */
    Optional<Cliente> getById(long id);

    /**
     * Crea un nuevo cliente en la base de datos.
     *
     * @param cliente El cliente a crear.
     * @return El cliente creado con su ID generado.
     */
    Cliente create(Cliente cliente);

    /**
     * Actualiza un cliente existente en la base de datos.
     *
     * @param id ID del cliente a actualizar.
     * @param cliente El objeto {@link Cliente} con los nuevos datos.
     * @return El cliente actualizado.
     */
    Cliente update(long id, Cliente cliente);

    /**
     * Elimina un cliente de la base de datos.
     *
     * @param id ID del cliente a eliminar.
     * @return {@code true} si se eliminó con éxito, {@code false} si no se encontró el cliente.
     */
    boolean delete(long id);

    /**
     * Elimina todos los clientes de la base de datos.
     *
     * @return {@code true} si se eliminaron clientes, {@code false} si no había clientes para eliminar.
     */
    boolean deleteAll();
}
