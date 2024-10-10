package org.example.clientes.storage.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import io.reactivex.rxjava3.core.Observable;
import org.example.clientes.model.Cliente;
import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class StorageJsonImpl implements StorageJson<Cliente> {

    private final Logger logger;
    private final ObjectMapper objectMapper;

    public StorageJsonImpl(ObjectMapper objectMapper, Logger logger) {
        this.objectMapper = objectMapper;
        this.logger = logger;
    }

    @Override
    public Observable<Cliente> importFile(File file) {
        return Observable.create(emitter -> {
            try {
                logger.info("Iniciando la importación de clientes desde el archivo: {}", file.getName());

                List<Cliente> clientes = objectMapper.readValue(file, new TypeReference<List<Cliente>>() {});

                for (Cliente cliente : clientes) {
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

    @Override
    public void exportFile(File file, Observable<Cliente> items) {
        items.toList().subscribe(
                clienteList -> {
                    try {
                        logger.info("Iniciando la exportación de clientes al archivo: {}", file.getName());

                        objectMapper.writeValue(file, clienteList);

                        logger.info("Clientes exportados correctamente al archivo: {}", file.getName());
                    } catch (IOException e) {
                        logger.error("Error al exportar clientes al archivo: {}", file.getName(), e);
                    }
                },
                throwable -> logger.error("Error durante el procesamiento de la lista de clientes", throwable)
        );
    }
}
