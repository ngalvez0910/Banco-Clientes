package org.example.clientes.storage.json;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

class StorageJsonClienteImplTest {

    private StorageJsonClienteImpl storageJsonCliente;
    private File testFile;
    @BeforeEach
    public void setUp() throws IOException {
        storageJsonCliente = new StorageJsonClienteImpl();
        testFile = Files.createTempFile("clientes", ".json").toFile();
    }
    @Test
    void importFileSuccessfully() throws IOException {

    }
}