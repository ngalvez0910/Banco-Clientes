package org.example.clientes.storage.json;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import org.example.clientes.errors.ClienteError;
import org.example.clientes.model.Cliente;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class StorageJsonClienteImpl implements StorageJson<Cliente> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Observable<Cliente> importFile(File file) {
        return Observable.<Cliente>create(emitter -> {
            try {
                List<Cliente> clientes = objectMapper.readValue(file, new TypeReference<>() {});

                for (Cliente cliente : clientes) {
                    emitter.onNext(cliente);
                }
                emitter.onComplete();
            } catch (IOException e) {
                emitter.onError(new ClienteError.StorageError("importar", file.getName()));
            }
        }).subscribeOn(Schedulers.io());
    }


    @Override
    public void exportFile(File file, Observable<Cliente> items) {
        items.toList()
                .observeOn(Schedulers.io())
                .subscribe(clientes -> {
                    try (FileWriter writer = new FileWriter(file)) {
                        objectMapper.writeValue(writer, clientes);
                    } catch (IOException e) {
                        throw new ClienteError.StorageError("exportar", file.getName());
                    }
                }, throwable -> {
                    if (throwable instanceof ClienteError) {
                        System.err.println(throwable.getMessage());
                    } else {
                        throwable.printStackTrace();
                    }
                });
    }
}
