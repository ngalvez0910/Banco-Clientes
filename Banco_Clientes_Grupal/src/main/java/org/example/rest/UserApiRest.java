package org.example.rest;

import org.example.rest.responses.getAll.UserGetAll;
import org.example.rest.responses.getById.UserGetById;
import retrofit2.Call;
import retrofit2.http.*;
import org.example.rest.responses.createUpdateDelete.Response;
import org.example.rest.responses.createUpdateDelete.Request;


import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface UserApiRest {
    String API_USERS_URL = "https://jsonplaceholder.typicode.com/";

    @GET("users")
    Call<List<UserGetAll>> getAllSync();

    @GET("users/{id}")
    Call<UserGetById> getByIdSync(@Path("id") int id);

    @POST("users")
    CompletableFuture<Response> createUser(@Body Request user);

    @PUT("users/{id}")
    CompletableFuture<Response> updateUser(@Path("id") int id, @Body Request user);

    @DELETE("users/{id}")
    CompletableFuture<Response> deleteUser(@Path("id") int id);

}
