package org.example.clientes.storage.json;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.reactivex.rxjava3.core.Observable;
import org.example.clientes.dto.ClienteDto;
import org.example.clientes.mappers.ClienteMapper;
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

        Tarjeta tarjeta = Tarjeta.builder()
                .id(1L)
                .nombreTitular("Juan Pérez")
                .numeroTarjeta("1234-5678-9012-3456")
                .fechaCaducidad(LocalDate.of(2025, 5, 31))
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        Cliente cliente = Cliente.builder()
                .id(1L)
                .usuario(usuario)
                .tarjeta(Collections.singletonList(tarjeta))
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        List<ClienteDto> clientes = Collections.singletonList(ClienteMapper.toDto(cliente));

        when(objectMapper.readValue(eq(file), any(TypeReference.class))).thenReturn(clientes);

        Observable<Cliente> result = storageJson.importFile(file);

        assertNotNull(result);
        assertEquals(1, result.toList().blockingGet().size());
        verify(logger).info("Iniciando la importación de clientes desde el archivo: {}", file.getName());
    }

    @Test
    void testImportFile_Error() throws IOException {
        File file = new File("clientes.json");

        // Simular error al leer el archivo
        when(objectMapper.readValue(any(File.class), any(TypeReference.class))).thenThrow(new IOException("Error de lectura"));

        Observable<Cliente> result = storageJson.importFile(file);

        result.test().assertError(IOException.class);
        verify(logger).error(eq("Error al importar clientes desde el archivo: {}"), eq(file.getName()), any(IOException.class));
    }

    @Test
    void testExportFile_Success() throws IOException {
        File file = new File("clientes_export.json");

        // Crear Cliente para exportar
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

        Cliente cliente = Cliente.builder()
                .id(1L)
                .usuario(usuario)
                .tarjeta(Collections.singletonList(tarjeta))
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        List<Cliente> clientes = Collections.singletonList(cliente);

        // Ejecutar exportación
        storageJson.exportFile(file, Observable.fromIterable(clientes));

        // Verificar que el ObjectMapper intentó escribir los datos correctos
        verify(objectMapper).writeValue(eq(file), any(List.class));
        verify(logger).info("Iniciando la exportación de clientes al archivo: {}", file.getName());
    }

    @Test
    void testExportFile_Error() throws IOException {
        File file = new File("clientes_export.json");

        // Simular error durante la exportación
        doThrow(new IOException("Error de escritura")).when(objectMapper).writeValue(any(File.class), any(List.class));

        // Datos de ejemplo
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

        Cliente cliente = Cliente.builder()
                .id(1L)
                .usuario(usuario)
                .tarjeta(Collections.singletonList(tarjeta))
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        List<Cliente> clientes = Collections.singletonList(cliente);

        // Ejecutar exportación con error
        storageJson.exportFile(file, Observable.fromIterable(clientes));

        // Verificar que el logger registró el error correctamente
        verify(logger).error(eq("Error al exportar clientes al archivo: {}"), eq(file.getName()), any(IOException.class));
    }
}
