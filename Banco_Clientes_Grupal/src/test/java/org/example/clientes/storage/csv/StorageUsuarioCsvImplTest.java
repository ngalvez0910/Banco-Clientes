package org.example.clientes.storage.csv;

import io.reactivex.rxjava3.core.Observable;
import org.example.clientes.model.Tarjeta;
import org.example.clientes.model.Usuario;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StorageUsuarioCsvImplTest {

    private StorageUsuarioCsvImpl storage;
    private File testFile;

    @BeforeEach
    public void setUp() throws IOException {
        storage = new StorageUsuarioCsvImpl();
        testFile = File.createTempFile("StorageUsuarioCsvImplTest", ".csv");
        Files.write(testFile.toPath(),
                "ID,Nombre,UserName,Email\n1,John Doe,johndoe,john@example.com\n2,Jane Smith,janesmith,jane@example.com".getBytes());
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
                () -> assertEquals(2L, usuarioList.get(1).getId()),
                () -> assertEquals("Jane Smith", usuarioList.get(1).getNombre()),
                () -> assertEquals("janesmith", usuarioList.get(1).getUserName())
        );
    }

    @Test
    void testExportFile() throws IOException{
        List<Usuario> usuarioList = List.of(
                Usuario.builder()
                    .id(1L)
                    .nombre("Pablo Motos")
                    .email("pablomotos@gmail.com")
                    .userName("pablomotos")
                    .build(),
                Usuario.builder()
                    .id(2L)
                    .nombre("Juan Perez")
                    .email("juanperez@gmail.com")
                    .userName("juanperez")
                    .build()
        );

        Observable<Usuario> userOvservable = Observable.fromIterable(usuarioList);

        File exportFile = File.createTempFile("ExportedUsuarios", ".csv");
        exportFile.deleteOnExit();

        storage.exportFile(exportFile, userOvservable);

        List<String> lines = Files.readAllLines(exportFile.toPath());

        assertAll("exportedUsuarios",
            () -> assertEquals(3, lines.size()),
            () -> assertEquals("ID,Nombre,UserName,Email", lines.get(0)),
            () -> assertEquals("1,Pablo Motos,pablomotos,pablomotos@gmail.com", lines.get(1)),
            () -> assertEquals("2,Juan Perez,juanperez,juanperez@gmail.com", lines.get(2))
        );

    }
}