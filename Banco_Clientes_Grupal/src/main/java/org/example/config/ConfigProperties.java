package org.example.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Clase que gestiona la carga de un archivo de propiedades y permite acceder a sus valores.
 *
 * @author Jaime León, Natalia González, German Fernandez, Alba García, Mario de Domingo
 * @version 1.0-SNAPSHOT
 */
public class ConfigProperties {
    private final Properties properties = new Properties();

    /**
     * Constructor que carga las propiedades desde un archivo especificado.
     *
     * @param fileName El nombre del archivo de propiedades que se va a cargar.
     */
    public ConfigProperties(String fileName) {
        try (InputStream input = getClass().getClassLoader().getResourceAsStream(fileName)) {
            if (input == null) {
                System.out.println("Lo siento, no se pudo encontrar " + fileName);
                return;
            }
            properties.load(input);
        } catch (IOException ex) {
            ex.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * Obtiene el valor de una propiedad especificada por su clave, o devuelve un valor por defecto si no se encuentra.
     *
     * @param key          La clave de la propiedad.
     * @param defaultValue El valor por defecto que se devolverá si la propiedad no se encuentra.
     * @return El valor de la propiedad o el valor por defecto si no existe.
     */
    public String getProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }
}
