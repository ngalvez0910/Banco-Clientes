package org.example.rest;

import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

/**
 * Clase que gestiona la creación de un cliente Retrofit para realizar peticiones HTTP.
 *
 * Utiliza Jackson como convertidor para manejar la serialización y deserialización de objetos JSON.
 *
 * @author Jaime León, Natalia González, German Fernandez, Alba García, Mario de Domingo
 * @version 1.0-SNAPSHOT
 */
public class RetrofitClient {

    private static Retrofit retrofit = null;

    /**
     * Obtiene una instancia de Retrofit para la URL base especificada.
     * Si no existe una instancia previa, se crea un nuevo cliente Retrofit.
     *
     * @param baseUrl la URL base para las peticiones HTTP
     * @return una instancia de Retrofit configurada con la URL base y el convertidor Jackson
     */
    public static Retrofit getClient(String baseUrl) {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(JacksonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
