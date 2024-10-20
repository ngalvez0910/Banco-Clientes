package org.example.clientes.services;

import io.vavr.control.Either;
import org.example.clientes.errors.ClienteError;
import org.example.clientes.model.Cliente;
import org.example.clientes.model.Notificacion;
import reactor.core.publisher.Flux;

import java.util.List;

/**
 * Interfaz para el servicio de gestión de clientes.
 * Proporciona métodos para realizar operaciones CRUD sobre los clientes,
 * incluyendo la obtención, creación, actualización y eliminación de clientes.
 *
 * @author Jaime León, Natalia González, German Fernandez, Alba García, Mario de Domingo
 * @version 1.0-SNAPSHOT
 */
public interface ClienteService {

    /**
     * Obtiene la lista de todos los clientes.
     *
     * @return un {@link Either} que contiene un {@link ClienteError} si ocurre un error, o una lista de {@link Cliente} si la operación es exitosa.
     */
    Either<ClienteError, List<Cliente>> getAll();

    /**
     * Obtiene un cliente por su identificador.
     *
     * @param id el identificador del cliente
     * @return un {@link Either} que contiene un {@link ClienteError} si ocurre un error, o el {@link Cliente} correspondiente si la operación es exitosa.
     */
    Either<ClienteError, Cliente> getById(long id);

    /**
     * Crea un nuevo cliente.
     *
     * @param cliente el cliente a crear
     * @return un {@link Either} que contiene un {@link ClienteError} si ocurre un error, o el {@link Cliente} creado si la operación es exitosa.
     */
    Either<ClienteError, Cliente> create(Cliente cliente);

    /**
     * Actualiza un cliente existente.
     *
     * @param id el identificador del cliente a actualizar
     * @param cliente el cliente con los nuevos datos
     * @return un {@link Either} que contiene un {@link ClienteError} si ocurre un error, o el {@link Cliente} actualizado si la operación es exitosa.
     */
    Either<ClienteError, Cliente> update(long id, Cliente cliente);

    /**
     * Elimina un cliente por su identificador.
     *
     * @param id el identificador del cliente a eliminar
     * @return un {@link Either} que contiene un {@link ClienteError} si ocurre un error, o el {@link Cliente} eliminado si la operación es exitosa.
     */
    Either<ClienteError, Cliente> delete(long id);
}
