package org.example.clientes.storage.csv;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import org.example.clientes.model.Usuario;
import org.example.common.Storage;

import java.io.File;
import java.nio.file.Files;
import java.util.List;

public class StorageUsuarioCsvImpl implements Storage {

    @Override
    public Observable<Usuario> importFile(File file) {
        return Observable.<Usuario>create(emitter -> {
            List<String> lines = Files.readAllLines(file.toPath());
            for (int i = 1; i < lines.size(); i++) {
                String line = lines.get(i);
                Usuario usuario = parseLine(line.split(","));
                emitter.onNext(usuario);
            }
            emitter.onComplete();
        })
        .subscribeOn(Schedulers.io());
    }

    @Override
    public void exportFile(File file, Observable items) {
    }

    private Usuario parseLine(String[] parts) {
        return Usuario.builder()
                .id(Long.parseLong(parts[0]))
                .nombre(parts[1])
                .userName(parts[2])
                .email(parts[3])
                .build();
    }
}
