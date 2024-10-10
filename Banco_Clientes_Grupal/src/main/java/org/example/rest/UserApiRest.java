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

public interface UserApiRest {
    String API_USERS_URL = "https://jsonplaceholder.typicode.com/";

    @GET("users")
    Call<List<UserGetAll>> getAllSync();

    @GET("users/{id}")
    Call<UserGetById> getByIdSync(@Path("id") int id);

    @POST("users")
    Call<UserCreate> createUserSync(@Body Request user);

    @PUT("users/{id}")
    Call<UserCreate> updateUserSync(@Path("id") int id, @Body Request user);

    @DELETE ("users/{id}")
    Call<UserDelete> deleteUserSync(@Path("id") int id);
}
