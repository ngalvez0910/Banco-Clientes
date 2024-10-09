package org.example.clientes.storage.csv;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import org.example.clientes.errors.TarjetaError;
import org.example.clientes.model.Tarjeta;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDate;
import java.util.List;

public class StorageTarjetaCsvImpl implements StorageCsv<Tarjeta> {

    @Override
    public Observable<Tarjeta> importFile(File file) {
        return Observable.<Tarjeta>create(emitter -> {
            try {
                List<String> lines = Files.readAllLines(file.toPath());

                for (int i = 1; i < lines.size(); i++) {
                    String line = lines.get(i);
                    Tarjeta tarjeta = parseLine(line.split(","));
                    emitter.onNext(tarjeta);
                }
                emitter.onComplete();
            } catch (IOException e) {
                emitter.onError(new TarjetaError.StorageError("leer", file.getName()));
            }
        }).subscribeOn(Schedulers.io());
    }

    @Override
    public void exportFile(File file, Observable<Tarjeta> items) {
        items.subscribeOn(Schedulers.io())
                .blockingSubscribe(tarjeta -> {
                    try (FileWriter writer = new FileWriter(file, true)) {
                        if (file.length() == 0) {
                            writer.write("ID,Nombre Titular,Numero Tarjeta,Fecha Caducidad\n");
                        }
                        String formattedTarjeta = String.format("%d,%s,%s,%s%n",
                                tarjeta.getId(),
                                tarjeta.getNombreTitular(),
                                tarjeta.getNumeroTarjeta(),
                                tarjeta.getFechaCaducidad().toString());

                        writer.write(formattedTarjeta);
                    } catch (IOException e) {
                        throw new TarjetaError.StorageError("escribir", file.getName());
                    }
                }, Throwable::printStackTrace);
    }

    private Tarjeta parseLine(String[] parts) {
        return Tarjeta.builder()
                .id(Long.parseLong(parts[0]))
                .nombreTitular(parts[1])
                .numeroTarjeta(parts[2])
                .fechaCaducidad(LocalDate.parse(parts[3]))
                .build();
    }
}
