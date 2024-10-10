package org.example.clientes.storage.csv;

import io.reactivex.rxjava3.core.Observable;
import org.example.clientes.errors.TarjetaError;
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
                ("ID,Nombre Titular,Numero Tarjeta,Fecha Caducidad,CreatedAt,UpdatedAt\n1,John Doe,1234567890123456,2025-12-31,2024-01-01T00:00,2024-01-02T00:00\n2,Jane Smith,6543210987654321,2024-11-30,2024-01-03T00:00,2024-01-04T00:00").getBytes());
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
                () -> assertEquals(LocalDateTime.parse("2024-01-01T00:00"), tarjetaList.getFirst().getCreatedAt()),
                () -> assertEquals(LocalDateTime.parse("2024-01-02T00:00"), tarjetaList.getFirst().getUpdatedAt()),
                () -> assertEquals(2L, tarjetaList.get(1).getId()),
                () -> assertEquals("Jane Smith", tarjetaList.get(1).getNombreTitular()),
                () -> assertEquals(LocalDateTime.parse("2024-01-03T00:00"), tarjetaList.get(1).getCreatedAt()),
                () -> assertEquals(LocalDateTime.parse("2024-01-04T00:00"), tarjetaList.get(1).getUpdatedAt())
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
                        .createdAt(LocalDateTime.parse("2023-10-01T00:00"))
                        .updatedAt(LocalDateTime.parse("2023-10-02T00:00"))
                        .build(),
                Tarjeta.builder()
                        .id(2L)
                        .nombreTitular("Will Smith")
                        .numeroTarjeta("6543210987654321")
                        .fechaCaducidad(LocalDate.parse("2024-11-30"))
                        .createdAt(LocalDateTime.parse("2023-10-01T00:00"))
                        .updatedAt(LocalDateTime.parse("2023-10-02T00:00"))
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
                () -> assertEquals("1,John Shena,1234567890123456,2025-12-31,2023-10-01T00:00,2023-10-02T00:00", lines.get(1)),
                () -> assertEquals("2,Will Smith,6543210987654321,2024-11-30,2023-10-01T00:00,2023-10-02T00:00", lines.get(2))
        );
    }

    @Test
    void testImportFile_ConTarjetaInvalido() throws IOException {
        File invalidTestFile = File.createTempFile("InvalidStorageTarjetaCsvImplTest", ".csv");
        Files.write(invalidTestFile.toPath(), "ID,Nombre Titular,Numero Tarjeta,Fecha Caducidad,CreatedAt,UpdatedAt\n1,I,0000000000000000,2024-12-31,2024-01-01T00:00,2024-01-02T00:00\n2,A,0000000000000001,2024-12-31,InvalidDate,InvalidDate".getBytes());

        Observable<Tarjeta> tarjetas = storage.importFile(invalidTestFile);
        List<Tarjeta> tarjetaList = tarjetas.toList().blockingGet();

        assertEquals(1, tarjetaList.size());

        invalidTestFile.deleteOnExit();
    }

    @Test
    void testImportFile_StorageError() {
        File nonExistentFile = new File("non-existent-file.csv");

        Observable<Tarjeta> tarjetas = storage.importFile(nonExistentFile);

        Exception exception = assertThrows(Exception.class, () -> tarjetas.toList().blockingGet());

        assertAll("Verificar error de almacenamiento en importFile",
                () -> assertInstanceOf(TarjetaError.StorageError.class, exception.getCause()),
                () -> assertEquals("Error al leer el archivo: non-existent-file.csv", exception.getCause().getMessage())
        );
    }
}
