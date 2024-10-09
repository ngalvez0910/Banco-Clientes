package org.example;

import org.example.clientes.mappers.UsuarioMapper;
import org.example.clientes.model.Usuario;
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
        System.out.println("Recuperando todos los usuarios de la API-REST");
        var usuarios = userService.getAllAsync();
        usuarios
                .peek(lista->lista.forEach(System.out::println))
                .peekLeft(error-> System.out.println("Error recuperando todos los usuarios"+ error.getMessage()));

        System.out.println(".............................");
        System.out.println("Recuperando Usuario existente de la API-REST");
        int id = 1;
        var usuario = userService.getByIdAsync(id);
        usuario
                .peek(user-> {System.out.println("Usuario " + id +" Encontrado " + user);})
                .peekLeft(error-> {System.out.println("Error: " + error.getCode() + " - " + error.getMessage());});

        /*System.out.println(".............................");
        System.out.println("Recuperando Usuario NO existente de la API-REST");
        int id2 = 100;
        userService.getByIdAsync(id2)
                .peek(user-> {System.out.println("Usuario " + id2 +" No Encontrado " + user);})
                .peekLeft(error -> {System.out.println("Error: "+ error.getCode() +": " + error.getMessage());});
*/
        System.out.println(".............................");
        System.out.println("Creando Usuario en la API-REST");
        Usuario user =  Usuario.builder()
                .id(2312312312312132123L)
                .nombre("Prueba")
                .userName("UsuarioPrueba")
                .email("usuarioprueba@mail.com")
                .build();
        userService.createUserAsync(user)
                .peek(userCreated-> { System.out.println("Usuario creado correctamente: " + userCreated);})
                .peekLeft(error-> {System.out.println("Error: "+ error.getCode() +": " + error.getMessage());});



    }
}