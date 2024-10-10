package org.example.clientes.storage.csv;

import io.reactivex.rxjava3.core.Observable;
import org.example.clientes.model.Tarjeta;
import org.junit.jupiter.api.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StorageTarjetaCsvImplTest {

    private StorageTarjetaCsvImpl storage;
    private File testFile;

    @BeforeEach
    public void setUp() throws IOException {
        storage = new StorageTarjetaCsvImpl();
        testFile = File.createTempFile("StorageTarjetaCsvImplTest", ".csv");
        Files.write(testFile.toPath(),
                ("ID,Nombre Titular,Numero Tarjeta,Fecha Caducidad,CreatedAt,UpdatedAt\n1,John Doe,1234567890123456,2025-12-31,2024-01-01,2024-01-02\n2,Jane Smith,6543210987654321,2024-11-30,2024-01-03,2024-01-04").getBytes());
    }

    @AfterEach
    public void tearDown() throws IOException {
        Files.deleteIfExists(testFile.toPath());
    }

    @Test
    void testImportFile() {
        Observable<Tarjeta> tarjetas = storage.importFile(testFile);
        List<Tarjeta> tarjetaList = tarjetas.toList().blockingGet();

        assertAll("tarjetas",
                () -> assertEquals(2, tarjetaList.size()),
                () -> assertEquals(1L, tarjetaList.getFirst().getId()),
                () -> assertEquals("John Doe", tarjetaList.getFirst().getNombreTitular()),
                () -> assertEquals(LocalDate.parse("2024-01-01"), tarjetaList.getFirst().getCreatedAt()),
                () -> assertEquals(LocalDate.parse("2024-01-02"), tarjetaList.getFirst().getUpdatedAt()),
                () -> assertEquals(2L, tarjetaList.get(1).getId()),
                () -> assertEquals("Jane Smith", tarjetaList.get(1).getNombreTitular()),
                () -> assertEquals(LocalDate.parse("2024-01-03"), tarjetaList.get(1).getCreatedAt()),
                () -> assertEquals(LocalDate.parse("2024-01-04"), tarjetaList.get(1).getUpdatedAt())
        );
    }

    @Test
    void testExportFile() throws IOException {
        List<Tarjeta> tarjetaList = List.of(
                Tarjeta.builder()
                        .id(1L)
                        .nombreTitular("John Shena")
                        .numeroTarjeta("1234567890123456")
                        .fechaCaducidad(LocalDate.parse("2025-12-31"))
                        .createdAt(LocalDateTime.parse("2024-01-01"))
                        .updatedAt(LocalDateTime.parse("2024-01-02"))
                        .build(),
                Tarjeta.builder()
                        .id(2L)
                        .nombreTitular("Will Smith")
                        .numeroTarjeta("6543210987654321")
                        .fechaCaducidad(LocalDate.parse("2024-11-30"))
                        .createdAt(LocalDateTime.parse("2024-01-03"))
                        .updatedAt(LocalDateTime.parse("2024-01-04"))
                        .build()
        );

        Observable<Tarjeta> tarjetaObservable = Observable.fromIterable(tarjetaList);

        File exportFile = File.createTempFile("ExportedTarjetas", ".csv");
        exportFile.deleteOnExit();

        storage.exportFile(exportFile, tarjetaObservable);

        List<String> lines = Files.readAllLines(exportFile.toPath());

        assertAll("exportedTarjetas",
                () -> assertEquals(3, lines.size()),
                () -> assertEquals("ID,Nombre Titular,Numero Tarjeta,Fecha Caducidad,CreatedAt,UpdatedAt", lines.getFirst()),
                () -> assertEquals("1,John Shena,1234567890123456,2025-12-31,2024-01-01,2024-01-02", lines.get(1)),
                () -> assertEquals("2,Will Smith,6543210987654321,2024-11-30,2024-01-03,2024-01-04", lines.get(2))
        );
    }
}
