package org.example.clientes.storage.csv;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import org.example.clientes.errors.UsuarioError;
import org.example.clientes.model.Usuario;
import org.example.clientes.validators.UsuarioValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Implementación de la interfaz StorageCsv para manejar operaciones de almacenamiento
 * de usuarios en archivos CSV.
 */
public class StorageUsuarioCsvImpl implements StorageCsv<Usuario> {

    private final UsuarioValidator usuarioValidator = new UsuarioValidator();
    private final Logger logger = LoggerFactory.getLogger(StorageUsuarioCsvImpl.class);

    /**
     * Importa datos de un archivo CSV y emite objetos Usuario.
     *
     * @param file Archivo CSV desde el que se importan los usuarios.
     * @return Observable de Usuario, emitiendo cada usuario encontrado en el archivo.
     */
    @Override
    public Observable<Usuario> importFile(File file) {
        return Observable.<Usuario>create(emitter -> {
            try {
                List<String> lines = Files.readAllLines(file.toPath());
                // Procesa cada línea del archivo, omitiendo el encabezado
                for (int i = 1; i < lines.size(); i++) {
                    String line = lines.get(i);
                    Usuario usuario = parseLine(line.split(","));
                    // Valida el usuario antes de emitirlo
                    if (!usuarioValidator.validarUsuario(usuario)) {
                        logger.error("Usuario no válido: {}", usuario);
                        continue;
                    }
                    emitter.onNext(usuario);
                }
                emitter.onComplete();
            } catch (IOException e) {
                logger.error("Error al leer el archivo: {}", file.getName(), e);
                emitter.onError(new UsuarioError.StorageError("leer", file.getName()));
            }
        }).subscribeOn(Schedulers.io());
    }

    /**
     * Exporta una lista de usuarios a un archivo CSV.
     *
     * @param file Archivo CSV donde se exportarán los usuarios.
     * @param items Observable de Usuario que se exportará al archivo.
     */
    @Override
    public void exportFile(File file, Observable<Usuario> items) {
        items.subscribeOn(Schedulers.io())
                .blockingSubscribe(usuario -> {
                    try (FileWriter writer = new FileWriter(file, true)) {
                        // Escribe el encabezado si el archivo está vacío
                        if (file.length() == 0) {
                            writer.write("ID,Nombre,UserName,Email,createdAt,updatedAt\n");
                        }
                        // Formatea el usuario como una línea CSV
                        String formattedUsuario = String.format("%d,%s,%s,%s,%s,%s%n",
                                usuario.getId(),
                                usuario.getNombre(),
                                usuario.getUserName(),
                                usuario.getEmail(),
                                usuario.getCreatedAt(),
                                usuario.getUpdatedAt());
                        writer.write(formattedUsuario);
                    } catch (IOException e) {
                        logger.error("Error al escribir en el archivo: {}", file.getName(), e);
                        throw new UsuarioError.StorageError("escribir", file.getName());
                    }
                }, error -> logger.error("Error en exportFile: ", error));
    }

    /**
     * Parsea una línea del archivo CSV y crea un objeto Usuario.
     *
     * @param parts Array de cadenas que representan los campos del usuario.
     * @return Objeto Usuario creado a partir de los datos proporcionados.
     */
    private Usuario parseLine(String[] parts) {
        return Usuario.builder()
                .id(Long.parseLong(parts[0]))
                .nombre(parts[1])
                .userName(parts[2])
                .email(parts[3])
                .createdAt(LocalDateTime.parse(parts[4]))
                .updatedAt(LocalDateTime.parse(parts[5]))
                .build();
    }
}
