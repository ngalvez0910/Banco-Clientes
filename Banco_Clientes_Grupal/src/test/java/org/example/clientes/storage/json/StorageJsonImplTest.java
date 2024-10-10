package org.example.clientes.storage.json;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.reactivex.rxjava3.core.Observable;
import org.example.clientes.model.Cliente;
import org.example.clientes.model.Tarjeta;
import org.example.clientes.model.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class StorageJsonImplTest {

    private StorageJsonImpl storageJson;
    private ObjectMapper objectMapper;
    private Logger logger;

    @BeforeEach
    public void setUp() {
        objectMapper = mock(ObjectMapper.class);
        logger = mock(Logger.class);
        storageJson = new StorageJsonImpl(objectMapper, logger);
    }

    @Test
    void testImportFile_Success() throws IOException {
        File file = new File("clientes.json");

        Usuario usuario = Usuario.builder()
                .id(1L)
                .nombre("Juan Pérez")
                .userName("juanp")
                .email("juan.perez@example.com")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        Tarjeta tarjeta1 = Tarjeta.builder()
                .id(1L)
                .nombreTitular("Juan Pérez")
                .numeroTarjeta("1234-5678-9012-3456")
                .fechaCaducidad(LocalDate.of(2025, 5, 31))
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        Tarjeta tarjeta2 = Tarjeta.builder()
                .id(2L)
                .nombreTitular("Juan Pérez")
                .numeroTarjeta("9876-5432-1098-7654")
                .fechaCaducidad(LocalDate.of(2026, 12, 31))
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        Cliente cliente1 = Cliente.builder()
                .id(1L)
                .usuario(usuario)
                .tarjeta(Arrays.asList(tarjeta1, tarjeta2))
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        Cliente cliente2 = Cliente.builder()
                .id(2L)
                .usuario(usuario)
                .tarjeta(Arrays.asList(tarjeta1, tarjeta2))
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        List<Cliente> clientes = Arrays.asList(cliente1, cliente2);

        when(objectMapper.readValue(file, new TypeReference<List<Cliente>>() {})).thenReturn(clientes);

        Observable<Cliente> result = storageJson.importFile(file);

        assertNotNull(result);
        assertEquals(2, result.toList().blockingGet().size());
        verify(logger).info("Iniciando la importación de clientes desde el archivo: {}", file.getName());
    }

    @Test
    void testImportFile_Error() throws IOException {
        File file = new File("clientes.json");
        when(objectMapper.readValue(any(File.class), any(TypeReference.class))).thenThrow(new IOException("Error de lectura"));

        Observable<Cliente> result = storageJson.importFile(file);

        result.test().assertError(IOException.class);
        verify(logger).error("Error al importar clientes desde el archivo: {}", file.getName(), any(IOException.class));
    }

    @Test
    void testExportFile_Success() throws IOException {
        File file = new File("clientes_export.json");

        Usuario usuario = Usuario.builder()
                .id(1L)
                .nombre("Juan Pérez")
                .userName("juanp")
                .email("juan.perez@example.com")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        Tarjeta tarjeta = Tarjeta.builder()
                .id(1L)
                .nombreTitular("Juan Pérez")
                .numeroTarjeta("1234-5678-9012-3456")
                .fechaCaducidad(LocalDate.of(2025, 5, 31))
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        Cliente cliente1 = Cliente.builder()
                .id(1L)
                .usuario(usuario)
                .tarjeta(Collections.singletonList(tarjeta))
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        List<Cliente> clientes = Collections.singletonList(cliente1);

        storageJson.exportFile(file, Observable.fromIterable(clientes));

        verify(objectMapper).writeValue(eq(file), any(List.class));
        verify(logger).info("Iniciando la exportación de clientes al archivo: {}", file.getName());
    }

    @Test
    void testExportFile_Error() throws IOException {
        File file = new File("clientes_export.json");

        Usuario usuario = Usuario.builder()
                .id(1L)
                .nombre("Juan Pérez")
                .userName("juanp")
                .email("juan.perez@example.com")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        Tarjeta tarjeta = Tarjeta.builder()
                .id(1L)
                .nombreTitular("Juan Pérez")
                .numeroTarjeta("1234-5678-9012-3456")
                .fechaCaducidad(LocalDate.of(2025, 5, 31))
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        Cliente cliente1 = Cliente.builder()
                .id(1L)
                .usuario(usuario)
                .tarjeta(Collections.singletonList(tarjeta))
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        List<Cliente> clientes = Collections.singletonList(cliente1);

        doThrow(new IOException("Error de escritura")).when(objectMapper).writeValue(any(File.class), any(List.class));

        storageJson.exportFile(file, Observable.fromIterable(clientes));

        verify(logger).error("Error al exportar clientes al archivo: {}", file.getName(), any(IOException.class));
    }
}