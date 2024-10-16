package org.example.clientes.storage.csv;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import org.example.clientes.errors.TarjetaError;
import org.example.clientes.model.Tarjeta;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

/**
 * Implementación de la interfaz StorageCsv para manejar operaciones de almacenamiento
 * de tarjetas en archivos CSV.
 *
 * @author Jaime León, Natalia González, German Fernandez, Alba García, Mario de Domingo
 */
public class StorageTarjetaCsvImpl implements StorageCsv<Tarjeta> {

    private final Logger logger = LoggerFactory.getLogger(StorageTarjetaCsvImpl.class);

    /**
     * Importa datos de un archivo CSV y emite objetos Tarjeta.
     *
     * @param file Archivo CSV desde el que se importan las tarjetas.
     * @return Observable de Tarjeta, emitiendo cada tarjeta encontrada en el archivo.
     */
    @Override
    public Observable<Tarjeta> importFile(File file) {
        return Observable.<Tarjeta>create(emitter -> {
            try {
                List<String> lines = Files.readAllLines(file.toPath());

                for (int i = 1; i < lines.size(); i++) {
                    String line = lines.get(i);
                    Tarjeta tarjeta = parseLine(line.split(","));
                    if (tarjeta != null) {
                        emitter.onNext(tarjeta);
                    }
                }
                emitter.onComplete();
            } catch (IOException e) {
                logger.error("Error al leer el archivo: {}", file.getName(), e);
                emitter.onError(new TarjetaError.StorageError("leer", file.getName()));
            }
        }).subscribeOn(Schedulers.io());
    }

    /**
     * Exporta una lista de tarjetas a un archivo CSV.
     *
     * @param file Archivo CSV donde se exportarán las tarjetas.
     * @param items Observable de Tarjeta que se exportará al archivo.
     */
    @Override
    public void exportFile(File file, Observable<Tarjeta> items) {
        items.subscribeOn(Schedulers.io())
                .blockingSubscribe(tarjeta -> {
                    try (FileWriter writer = new FileWriter(file, true)) {
                        if (file.length() == 0) {
                            writer.write("ID,Nombre Titular,Numero Tarjeta,Fecha Caducidad,CreatedAt,UpdatedAt\n");
                        }
                        String formattedTarjeta = String.format("%d,%s,%s,%s,%s,%s%n",
                                tarjeta.getId(),
                                tarjeta.getNombreTitular(),
                                tarjeta.getNumeroTarjeta(),
                                tarjeta.getFechaCaducidad().toString(),
                                tarjeta.getCreatedAt() != null ? tarjeta.getCreatedAt().toString() : "",
                                tarjeta.getUpdatedAt() != null ? tarjeta.getUpdatedAt().toString() : "");

                        writer.write(formattedTarjeta);
                    } catch (IOException e) {
                        logger.error("Error al escribir en el archivo: {}", file.getName(), e);
                        throw new TarjetaError.StorageError("escribir", file.getName());
                    }
                }, error -> logger.error("Error en exportFile: ", error));
    }

    /**
     * Parsea una línea del archivo CSV y crea un objeto Tarjeta.
     *
     * @param parts Array de cadenas que representan los campos de la tarjeta.
     * @return Objeto Tarjeta o null si ocurre un error durante el parseo.
     */
    private Tarjeta parseLine(String[] parts) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

        try {
            return Tarjeta.builder()
                    .id(Long.parseLong(parts[0]))
                    .nombreTitular(parts[1])
                    .numeroTarjeta(parts[2])
                    .fechaCaducidad(LocalDate.parse(parts[3]))
                    .createdAt(parts.length > 4 && !parts[4].isEmpty() ? LocalDateTime.parse(parts[4], formatter) : null)
                    .updatedAt(parts.length > 5 && !parts[5].isEmpty() ? LocalDateTime.parse(parts[5], formatter) : null)
                    .build();
        } catch (NumberFormatException | DateTimeParseException e) {
            logger.error("Error al parsear la línea: {}", String.join(",", parts), e);
            return null;
        }
    }

}
