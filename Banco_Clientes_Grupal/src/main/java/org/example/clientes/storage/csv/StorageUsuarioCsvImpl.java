package org.example.clientes.storage.csv;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import org.example.clientes.errors.TarjetaError;
import org.example.clientes.model.Usuario;
import org.example.common.Storage;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

public class StorageUsuarioCsvImpl implements Storage<Usuario> {

    @Override
    public Observable<Usuario> importFile(File file) {
        return Observable.<Usuario>create(emitter -> {
            try {
                List<String> lines = Files.readAllLines(file.toPath());
                for (int i = 1; i < lines.size(); i++) {
                    String line = lines.get(i);
                    Usuario usuario = parseLine(line.split(","));
                    emitter.onNext(usuario);
                }
                emitter.onComplete();
            } catch (IOException e) {
                emitter.onError(new TarjetaError.StorageError("leer"));
            }
        }).subscribeOn(Schedulers.io());
    }

    @Override
    public void exportFile(File file, Observable<Usuario> items) {
        items.subscribeOn(Schedulers.io())
                .subscribe(usuario -> {
                    try (FileWriter writer = new FileWriter(file, true)) {
                        if (file.length() == 0) {
                            writer.write("ID,Nombre,UserName,Email%n");
                        }
                        String formattedUsuario = String.format("%d,%s,%s,%s%n",
                                usuario.getId(),
                                usuario.getNombre(),
                                usuario.getUserName(),
                                usuario.getEmail());
                        writer.write(formattedUsuario);
                    } catch (IOException e) {
                        throw new TarjetaError.StorageError("escribir");
                    }
                }, Throwable::printStackTrace);
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
