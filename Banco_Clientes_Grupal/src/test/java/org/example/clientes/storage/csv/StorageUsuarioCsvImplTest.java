package org.example.clientes.storage.csv;

import io.reactivex.rxjava3.core.Observable;
import org.example.clientes.errors.UsuarioError;
import org.example.clientes.model.Usuario;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StorageUsuarioCsvImplTest {

    private StorageUsuarioCsvImpl storage;
    private File testFile;

    @BeforeEach
    public void setUp() throws IOException {
        storage = new StorageUsuarioCsvImpl();
        testFile = File.createTempFile("StorageUsuarioCsvImplTest", ".csv");
        Files.write(testFile.toPath(), "ID,Nombre,UserName,Email,createdAt,updatedAt\n1,John Doe,johndoe,john@example.com,2023-10-01T00:00:00,2023-10-05T00:00:00\n2,Jane Smith,janesmith,jane@example.com,2023-09-01T00:00:00,2023-10-01T00:00:00".getBytes());
    }

    @AfterEach
    public void tearDown() throws IOException {
        Files.deleteIfExists(testFile.toPath());
    }

    @Test
    void testImportFile() {
        Observable<Usuario> usuarios = storage.importFile(testFile);
        List<Usuario> usuarioList = usuarios.toList().blockingGet();

        assertAll("usuarios",
                () -> assertEquals(2, usuarioList.size()),
                () -> assertEquals(1L, usuarioList.get(0).getId()),
                () -> assertEquals("John Doe", usuarioList.get(0).getNombre()),
                () -> assertEquals("johndoe", usuarioList.get(0).getUserName()),
                () -> assertEquals(LocalDateTime.of(2023, 10, 1, 0, 0, 0), usuarioList.get(0).getCreatedAt()),
                () -> assertEquals(LocalDateTime.of(2023, 10, 5, 0, 0, 0), usuarioList.get(0).getUpdatedAt()),
                () -> assertEquals(2L, usuarioList.get(1).getId()),
                () -> assertEquals("Jane Smith", usuarioList.get(1).getNombre()),
                () -> assertEquals("janesmith", usuarioList.get(1).getUserName()),
                () -> assertEquals(LocalDateTime.of(2023, 9, 1, 0, 0, 0), usuarioList.get(1).getCreatedAt()),
                () -> assertEquals(LocalDateTime.of(2023, 10, 1, 0, 0, 0), usuarioList.get(1).getUpdatedAt())
        );
    }

    @Test
    void testExportFile() throws IOException {
        List<Usuario> usuarioList = List.of(
                Usuario.builder()
                        .id(1L)
                        .nombre("Pablo Motos")
                        .email("pablomotos@gmail.com")
                        .userName("pablomotos")
                        .createdAt(LocalDateTime.of(2023, 1, 1, 0, 0))
                        .updatedAt(LocalDateTime.of(2023, 10, 1, 0, 0))
                        .build(),
                Usuario.builder()
                        .id(2L)
                        .nombre("Juan Perez")
                        .email("juanperez@gmail.com")
                        .userName("juanperez")
                        .createdAt(LocalDateTime.of(2023, 1, 2, 0, 0))
                        .updatedAt(LocalDateTime.of(2023, 10, 2, 0, 0))
                        .build()
            );

        Observable<Usuario> userObservable = Observable.fromIterable(usuarioList);

        File exportFile = File.createTempFile("ExportedUsuarios", ".csv");
        exportFile.deleteOnExit();

        storage.exportFile(exportFile, userObservable);

        List<String> lines = Files.readAllLines(exportFile.toPath());

        assertAll("exportedUsuarios",
                () -> assertEquals(3, lines.size()),
                () -> assertEquals("ID,Nombre,UserName,Email,createdAt,updatedAt", lines.getFirst()),
                () -> assertEquals("1,Pablo Motos,pablomotos,pablomotos@gmail.com,2023-01-01T00:00,2023-10-01T00:00", lines.get(1)),
                () -> assertEquals("2,Juan Perez,juanperez,juanperez@gmail.com,2023-01-02T00:00,2023-10-02T00:00", lines.get(2))
        );
    }

    @Test
    void testImportFile_ConUsuarioInvalido() throws IOException {
        File invalidTestFile = File.createTempFile("InvalidStorageUsuarioCsvImplTest", ".csv");
        Files.write(invalidTestFile.toPath(),
                "ID,Nombre,UserName,Email,createdAt,updatedAt\n1,John,joh,john@example.com,2023-10-01T00:00:00,2023-10-05T00:00:00\n2,J,js,jane@example.com,2023-09-01T00:00:00,2023-10-01T00:00:00".getBytes());

        Observable<Usuario> usuarios = storage.importFile(invalidTestFile);
        List<Usuario> usuarioList = usuarios.toList().blockingGet();

        assertEquals(0, usuarioList.size());

        invalidTestFile.deleteOnExit();
    }

    @Test
    void testImportFile_StorageError() {
        File nonExistentFile = new File("non-existent-file.csv");

        Observable<Usuario> usuarios = storage.importFile(nonExistentFile);

        Exception exception = assertThrows(Exception.class, () -> usuarios.toList().blockingGet());

        assertAll("Verificar error de almacenamiento en importFile",
                () -> assertInstanceOf(UsuarioError.StorageError.class, exception.getCause()),
                () -> assertEquals("Error al leer el archivo: non-existent-file.csv", exception.getCause().getMessage())
        );
    }
}
