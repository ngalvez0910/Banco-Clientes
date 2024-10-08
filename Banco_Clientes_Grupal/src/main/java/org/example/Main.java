package org.example;

import org.example.rest.RetrofitClient;
import org.example.rest.UserApiRest;
import org.example.rest.repository.UserRemoteRepository;
import org.example.rest.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {

    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        System.out.println("Hello world!");
        logger.debug("userApiRest: " + UserApiRest.API_USERS_URL);

        var retrofit = RetrofitClient.getClient(UserApiRest.API_USERS_URL);
        var userApiRest = retrofit.create(UserApiRest.class);
        UserRemoteRepository userRemoteRepository = new UserRemoteRepository(userApiRest);
        UserService userService = new UserService(userRemoteRepository);


        System.out.println(".............................");

        var usuarios = userService.getAllAsync();
        usuarios
                .peek(lista->lista.forEach(System.out::println))
                .peekLeft(error-> System.out.println("Error recuperando todos los usuarios"+ error.getMessage()));

       /*  var usuarios2 = userService.getAllAsync();
        usuarios2.forEach(System.out::println);*/

        /* var usuarios3 = userRemoteRepository.getAllSync();
        usuarios3.forEach(System.out::println);*/

        int id = 1;
        var usuario = userService.getByIdAsync(id);
        usuario
                .peek(user-> {System.out.println("Usuario 1 Encontrado " + user);})
                .peekLeft(error-> {System.out.println("Error: " + error.getCode() + " - " + error.getMessage());});

        userService.getByIdAsync(100)
                .peek(user-> {System.out.println("Usuario 2 No Encontrado " + user);})
                .peekLeft(error -> {System.out.println("Error: "+ error.getCode() +": " + error.getMessage());});

        userService.getByIdAsync(1)
                .peek(user-> {System.out.println("Usuario 2 Encontrado " + user);})
                .peekLeft(error -> {System.out.println("Error: "+ error.getCode() +": " + error.getMessage());});

    }
}