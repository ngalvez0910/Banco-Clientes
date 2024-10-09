package org.example.clientes.storage.csv;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import org.example.clientes.errors.UsuarioError;
import org.example.clientes.model.Usuario;
import org.example.clientes.validators.UsuarioValidator;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDate;
import java.util.List;

public class StorageUsuarioCsvImpl implements StorageCsv<Usuario> {

    private final UsuarioValidator usuarioValidator = new UsuarioValidator();

    @Override
    public Observable<Usuario> importFile(File file) {
        return Observable.<Usuario>create(emitter -> {
            try {
                List<String> lines = Files.readAllLines(file.toPath());
                for (int i = 1; i < lines.size(); i++) {
                    String line = lines.get(i);
                    Usuario usuario = parseLine(line.split(","));
                    if (!usuarioValidator.validarUsuario(usuario)) {
                        System.err.println("Usuario no válido: " + usuario);
                        continue;
                    }
                    emitter.onNext(usuario);
                }
                emitter.onComplete();
            } catch (IOException e) {
                emitter.onError(new UsuarioError.StorageError("leer", file.getName()));
            }
        }).subscribeOn(Schedulers.io());
    }

    @Override
    public void exportFile(File file, Observable<Usuario> items) {
        items.subscribeOn(Schedulers.io())
                .blockingSubscribe(usuario -> {
                    try (FileWriter writer = new FileWriter(file, true)) {
                        if (file.length() == 0) {
                            writer.write("ID,Nombre,UserName,Email,createdAt,updatedAt\n");
                        }
                        String formattedUsuario = String.format("%d,%s,%s,%s,%s,%s%n",
                                usuario.getId(),
                                usuario.getNombre(),
                                usuario.getUserName(),
                                usuario.getEmail(),
                                usuario.getCreatedAt(),
                                usuario.getUpdatedAt());
                        writer.write(formattedUsuario);
                    } catch (IOException e) {
                        throw new UsuarioError.StorageError("escribir", file.getName());
                    }
                }, Throwable::printStackTrace);
    }

    private Usuario parseLine(String[] parts) {
        return Usuario.builder()
                .id(Long.parseLong(parts[0]))
                .nombre(parts[1])
                .userName(parts[2])
                .email(parts[3])
                .createdAt(LocalDate.parse(parts[4]))
                .updatedAt(LocalDate.parse(parts[5]))
                .build();
    }
}
