package org.example.rest;

import org.example.rest.responses.createUpdateDelete.UserCreate;
import org.example.rest.responses.createUpdateDelete.UserDelete;
import org.example.rest.responses.getAll.UserGetAll;
import org.example.rest.responses.getById.UserGetById;
import retrofit2.Call;
import retrofit2.http.*;
import org.example.rest.responses.createUpdateDelete.Request;


import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Interfaz que define las operaciones CRUD para interactuar con la API de usuarios.
 *
 * Esta API utiliza Retrofit para realizar peticiones HTTP sincrónicas a la URL base.
 * Permite obtener, crear, actualizar y eliminar usuarios.
 *
 * @author Jaime León, Natalia González, German Fernandez, Alba García, Mario de Domingo
 * @version 1.0-SNAPSHOT
 */
public interface UserApiRest {
    String API_USERS_URL = "https://jsonplaceholder.typicode.com/";

    /**
     * Obtiene la lista de todos los usuarios.
     *
     * @return una llamada de Retrofit con una lista de objetos UserGetAll
     */
    @GET("users")
    Call<List<UserGetAll>> getAllSync();

    /**
     * Obtiene un usuario por su ID.
     *
     * @param id el ID del usuario a obtener
     * @return una llamada de Retrofit con el usuario especificado por su ID
     */
    @GET("users/{id}")
    Call<UserGetById> getByIdSync(@Path("id") long id);

    /**
     * Crea un nuevo usuario.
     *
     * @param user el objeto Request con la información del nuevo usuario
     * @return una llamada de Retrofit con el usuario creado
     */
    @POST("users")
    Call<UserCreate> createUserSync(@Body Request user);

    /**
     * Actualiza un usuario existente.
     *
     * @param id el ID del usuario a actualizar
     * @param user el objeto Request con la información actualizada del usuario
     * @return una llamada de Retrofit con el usuario actualizado
     */
    @PUT("users/{id}")
    Call<UserCreate> updateUserSync(@Path("id") long id, @Body Request user);

    /**
     * Elimina un usuario por su ID.
     *
     * @param id el ID del usuario a eliminar
     * @return una llamada de Retrofit con la confirmación de eliminación
     */
    @DELETE ("users/{id}")
    Call<UserDelete> deleteUserSync(@Path("id") long id);
}