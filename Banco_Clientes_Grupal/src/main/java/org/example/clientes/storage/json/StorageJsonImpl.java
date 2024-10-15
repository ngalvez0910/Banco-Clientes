package org.example.clientes.storage.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.core.type.TypeReference;
import io.reactivex.rxjava3.core.Observable;
import org.example.clientes.dto.ClienteDto;
import org.example.clientes.mappers.ClienteMapper;
import org.example.clientes.model.Cliente;
import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementación de la interfaz StorageJson para manejar operaciones de
 * importación y exportación de objetos Cliente en formato JSON.
 */
public class StorageJsonImpl implements StorageJson<Cliente> {

    private final Logger logger; // Logger para registrar eventos
    private final ObjectMapper objectMapper; // Mapeador de objetos JSON

    /**
     * Constructor que inicializa el objeto ObjectMapper y el Logger.
     *
     * @param objectMapper Mapeador de objetos JSON para convertir entre objetos Cliente y su representación en JSON.
     * @param logger para registrar eventos y errores.
     */
    public StorageJsonImpl(ObjectMapper objectMapper, Logger logger) {
        this.objectMapper = objectMapper;
        // Habilita el pretty print
        this.objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        this.logger = logger;
    }

    /**
     * Importa una lista de objetos Cliente desde un archivo JSON.
     *
     * @param file El archivo JSON del que se importarán los clientes.
     * @return Un Observable que emite los clientes importados.
     */
    @Override
    public Observable<Cliente> importFile(File file) {
        return Observable.create(emitter -> {
            try {
                logger.info("Iniciando la importación de clientes desde el archivo: {}", file.getName());

                List<ClienteDto> clienteDtos = objectMapper.readValue(file, new TypeReference<>() {
                });

                for (ClienteDto dto : clienteDtos) {
                    Cliente cliente = ClienteMapper.fromDto(dto);
                    emitter.onNext(cliente);
                }
                emitter.onComplete();
                logger.info("Clientes importados correctamente desde el archivo: {}", file.getName());
            } catch (IOException e) {
                logger.error("Error al importar clientes desde el archivo: {}", file.getName(), e);
                emitter.onError(e);
            }
        });
    }

    /**
     * Exporta una lista de objetos Cliente a un archivo JSON.
     *
     * @param file  El archivo al que se exportarán los clientes.
     * @param items Un Observable que emite los clientes a exportar.
     */
    @Override
    public void exportFile(File file, Observable<Cliente> items) {
        items.toList().subscribe(
                clienteList -> {
                    try {
                        logger.info("Iniciando la exportación de clientes al archivo: {}", file.getName());

                        List<ClienteDto> clienteDtos = clienteList.stream()
                                .map(ClienteMapper::toDto)
                                .collect(Collectors.toList());

                        // Asegúrate de que el ObjectMapper está configurado para pretty print
                        objectMapper.writeValue(file, clienteDtos); // Aquí se debe ver la salida formateada

                        logger.info("Clientes exportados correctamente al archivo: {}", file.getName());
                    } catch (IOException e) {
                        logger.error("Error al exportar clientes al archivo: {}", file.getName(), e);
                    }
                },
                throwable -> logger.error("Error durante el procesamiento de la lista de clientes", throwable)
        );
    }
}
