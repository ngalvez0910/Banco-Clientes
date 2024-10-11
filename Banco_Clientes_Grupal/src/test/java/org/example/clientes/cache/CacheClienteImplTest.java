package org.example.clientes.cache;

import org.example.clientes.model.Cliente;
import org.example.clientes.model.Tarjeta;
import org.example.clientes.model.Usuario;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import static org.junit.jupiter.api.Assertions.*;

class CacheClienteImplTest {

    private Cliente Cliente1() {
        return Cliente.builder()
                .id(1L)
                .usuario(Usuario.builder()
                        .id(1L)
                        .nombre("Juan")
                        .userName("juan123")
                        .email("juan.perez@example.com")
                        .createdAt(LocalDateTime.now())
                        .updatedAt(LocalDateTime.now())
                        .build())
                .tarjeta(Collections.singletonList(Tarjeta.builder()
                        .id(1L)
                        .nombreTitular("Juan")
                        .numeroTarjeta("1234-5678-9012-3456")
                        .fechaCaducidad(LocalDate.of(2025, 5, 31))
                        .createdAt(LocalDateTime.now())
                        .updatedAt(LocalDateTime.now())
                        .build()))
                .build();
    }

    private Cliente Cliente2() {
        return Cliente.builder()
                .id(2L)
                .usuario(Usuario.builder()
                        .id(2L)
                        .nombre("Pedro")
                        .userName("pedro456")
                        .email("pedro.sanchez@example.com")
                        .createdAt(LocalDateTime.now())
                        .updatedAt(LocalDateTime.now())
                        .build())
                .tarjeta(Collections.singletonList(Tarjeta.builder()
                        .id(2L)
                        .nombreTitular("Pedro")
                        .numeroTarjeta("9876-5432-1098-7654")
                        .fechaCaducidad(LocalDate.of(2025, 7, 31))
                        .createdAt(LocalDateTime.now())
                        .updatedAt(LocalDateTime.now())
                        .build()))
                .build();
    }

    @Test
    void get() {
        CacheClienteImpl cache = new CacheClienteImpl();
        Cliente cliente1 = Cliente1();

        cache.put(cliente1.getId(), cliente1);

        assertAll( () -> {
            assertNotNull(cache.get(cliente1.getId()), "El cliente debería estar presente en la caché");
            assertEquals(cliente1, cache.get(cliente1.getId()), "El cliente recuperado debería ser igual al almacenado");
        });
    }

    @Test
    void getNotFound() {
        CacheClienteImpl cache = new CacheClienteImpl();
        Long idNoExistente = 3L;

        assertAll( () -> {
            assertNull(cache.get(idNoExistente), "El cliente no debería estar presente en la caché y debería devolver null");
        });
    }

    @Test
    void put() {
        CacheClienteImpl cache = new CacheClienteImpl();
        Cliente cliente1 = Cliente1();
        Cliente cliente2 = Cliente2();

        cache.put(cliente1.getId(), cliente1);
        cache.put(cliente2.getId(), cliente2);

        assertAll( () -> {
            assertEquals(2, cache.size(), "La caché debería contener dos clientes");
            assertEquals(cliente1, cache.get(cliente1.getId()), "El cliente1 recuperado debería ser igual al cliente1 almacenado");
            assertEquals(cliente2, cache.get(cliente2.getId()), "El cliente2 recuperado debería ser igual al cliente2 almacenado");
        });
    }

    @Test
    void putOverrider() {
        CacheClienteImpl cache = new CacheClienteImpl();
        Cliente cliente1 = Cliente1();
        Cliente cliente2 = Cliente2();
        Cliente cliente3 = Cliente.builder().id(3L).build();
        Cliente cliente4 = Cliente.builder().id(4L).build();
        Cliente cliente5 = Cliente.builder().id(5L).build();
        Cliente cliente6 = Cliente.builder().id(6L).build();

        cache.put(cliente1.getId(), cliente1);
        cache.put(cliente2.getId(), cliente2);
        cache.put(cliente3.getId(), cliente3);
        cache.put(cliente4.getId(), cliente4);
        cache.put(cliente5.getId(), cliente5);
        cache.put(cliente6.getId(), cliente6);

        assertAll( () -> {
            assertEquals(5, cache.size(), "La caché debería contener cinco clientes");
            assertFalse(cache.containsKey(cliente1.getId()), "La caché no debería contener cliente1");
            assertTrue(cache.containsKey(cliente2.getId()), "La caché debería contener cliente2");
            assertTrue(cache.containsKey(cliente3.getId()), "La caché debería contener cliente3");
            assertTrue(cache.containsKey(cliente4.getId()), "La caché debería contener cliente4");
            assertTrue(cache.containsKey(cliente5.getId()), "La caché debería contener cliente5");
            assertTrue(cache.containsKey(cliente6.getId()), "La caché debería contener cliente6");
        });
    }

    @Test
    void remove() {
        CacheClienteImpl cache = new CacheClienteImpl();
        Cliente cliente1 = Cliente1();
        Cliente cliente2 = Cliente2();

        cache.put(cliente1.getId(), cliente1);
        cache.put(cliente2.getId(), cliente2);

        cache.remove(cliente1.getId());

        assertAll( () -> {
            assertFalse(cache.containsKey(cliente1.getId()), "La caché no debería contener cliente1");
            assertTrue(cache.containsKey(cliente2.getId()), "La caché debería contener cliente2");
        });
    }

    @Test
    void clear() {
        CacheClienteImpl cache = new CacheClienteImpl();
        Cliente cliente1 = Cliente1();
        Cliente cliente2 = Cliente2();

        cache.put(cliente1.getId(), cliente1);
        cache.put(cliente2.getId(), cliente2);

        assertAll( () -> {
            assertEquals(2, cache.size(), "La caché debería contener dos clientes");
            assertTrue(cache.containsKey(cliente1.getId()), "La caché debería contener cliente1");
            assertTrue(cache.containsKey(cliente2.getId()), "La caché debería contener cliente2");
        });

        cache.clear();

        assertAll( () -> {
            assertEquals(0, cache.size(), "La caché debería estar vacía");
            assertFalse(cache.containsKey(cliente1.getId()), "La caché no debería contener cliente1");
            assertFalse(cache.containsKey(cliente2.getId()), "La caché no debería contener cliente2");
        });
    }

    @Test
    void size() {
        CacheClienteImpl cache = new CacheClienteImpl();
        Cliente cliente1 = Cliente1();
        Cliente cliente2 = Cliente2();
        Cliente cliente3 = Cliente.builder().id(3L).build();

        assertEquals(0, cache.size(), "La caché debería estar vacía al inicio");

        cache.put(cliente1.getId(), cliente1);
        assertEquals(1, cache.size(), "La caché debería contener un cliente");

        cache.put(cliente2.getId(), cliente2);
        assertEquals(2, cache.size(), "La caché debería contener dos clientes");

        cache.put(cliente3.getId(), cliente3);
        assertEquals(3, cache.size(), "La caché debería contener tres clientes");

        cache.clear();
        assertEquals(0, cache.size(), "La caché debería estar vacía después de limpiar");
    }
}
