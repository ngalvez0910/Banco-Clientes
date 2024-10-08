package org.example.clientes.storage.csv;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import org.example.clientes.model.Tarjeta;
import org.example.common.Storage;

import java.io.File;
import java.nio.file.Files;
import java.time.LocalDate;
import java.util.List;

public class StorageTarjetaCsvImpl implements Storage {

    @Override
    public Observable<Tarjeta> importFile(File file) {
        return Observable.<Tarjeta>create(emitter -> {
            List<String> lines = Files.readAllLines(file.toPath());

            for (int i = 1; i < lines.size(); i++) {
                String line = lines.get(i);
                Tarjeta tarjeta = parseLine(line.split(","));
                emitter.onNext(tarjeta);
            }

            emitter.onComplete();
        })
        .subscribeOn(Schedulers.io());
    }

    @Override
    public void exportFile(File file, Observable items) {
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
