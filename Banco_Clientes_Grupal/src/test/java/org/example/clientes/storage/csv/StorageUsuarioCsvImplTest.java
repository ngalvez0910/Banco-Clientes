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
    void testImportFile_WithInvalidUsers() throws IOException {
        // Crear un archivo de prueba con usuarios inválidos
        File invalidTestFile = File.createTempFile("InvalidStorageUsuarioCsvImplTest", ".csv");
        Files.write(invalidTestFile.toPath(),
                "ID,Nombre,UserName,Email\n1,John,joh,john@example.com\n2,J,js,jane@example.com".getBytes());

        Observable<Usuario> usuarios = storage.importFile(invalidTestFile);
        List<Usuario> usuarioList = usuarios.toList().blockingGet();

        // Solo deberíamos obtener 0 usuarios válidos, ya que todos son inválidos
        assertEquals(0, usuarioList.size());

        invalidTestFile.deleteOnExit(); // Limpiar el archivo de prueba
    }

    @Test
    void testExportFile() throws IOException {
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

        Observable<Usuario> userObservable = Observable.fromIterable(usuarioList);

        File exportFile = File.createTempFile("ExportedUsuarios", ".csv");
        exportFile.deleteOnExit();

        storage.exportFile(exportFile, userObservable);

        List<String> lines = Files.readAllLines(exportFile.toPath());

        assertAll("exportedUsuarios",
                () -> assertEquals(3, lines.size()),
                () -> assertEquals("ID,Nombre,UserName,Email", lines.get(0)),
                () -> assertEquals("1,Pablo Motos,pablomotos,pablomotos@gmail.com", lines.get(1)),
                () -> assertEquals("2,Juan Perez,juanperez,juanperez@gmail.com", lines.get(2))
        );
    }

    @Test
    void testImportFile_StorageError() {
        File nonExistentFile = new File("non-existent-file.csv");

        Observable<Usuario> usuarios = storage.importFile(nonExistentFile);

        Exception exception = assertThrows(Exception.class, () -> usuarios.toList().blockingGet());

        assertAll("Verificar error de almacenamiento en importFile",
                () -> assertInstanceOf(UsuarioError.StorageError.class, exception.getCause()),
                () -> assertEquals("Error al leer el archivo : non-existent-file.csv", exception.getCause().getMessage())
        );
    }

    @Test
    void testExportFile_StorageError() throws IOException {
        File readOnlyFile = File.createTempFile("readOnlyFile", ".csv");
        readOnlyFile.setReadOnly();

        List<Usuario> usuarioList = List.of(
                Usuario.builder()
                        .id(1L)
                        .nombre("Carlos")
                        .email("carlos@example.com")
                        .userName("carloss")
                        .build()
        );

        Observable<Usuario> usuarioObservable = Observable.fromIterable(usuarioList);
        Exception exception = assertThrows(Exception.class, () -> storage.exportFile(readOnlyFile, usuarioObservable));

        assertAll("Verificar error de almacenamiento en exportFile",
                () -> assertInstanceOf(UsuarioError.StorageError.class, exception.getCause()),
                () -> assertEquals("Error al escribir el archivo : " + readOnlyFile.getName(), exception.getCause().getMessage())
        );

        readOnlyFile.deleteOnExit();
    }
}
