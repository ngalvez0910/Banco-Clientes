package org.example.rest.repository;

import org.example.clientes.model.Usuario;
import org.example.rest.UserApiRest;
import org.example.rest.responses.getAll.UserGetAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)

class UserRemoteRepositoryTest {
    @Mock
    private UserApiRest userApiRest;

    @InjectMocks
    private UserRemoteRepository userRemoteRepository;

    @Test
    void getAll() throws IOException {

        var list = List.of(
                UserGetAll.builder().id(1).name("Test 01").username("test01user").email("test01user@mail.com").build(),
                UserGetAll.builder().id(2).name("Test 02").username("test02user").email("test02user@mail.com").build()
        );

        var response = Response.success(list);
        var call = mock(Call.class);
        when(call.execute()).thenReturn(response);
        when(userApiRest.getAllSync()).thenReturn(call);

        Optional<List<Usuario>> result = userRemoteRepository.getAllSync();

        assertTrue(result.isPresent());
        List<Usuario> expectedResult = list.stream()
                .map(userGetAll -> Usuario.builder()
                        .id((long) userGetAll.getId())
                        .nombre(userGetAll.getName())
                        .userName(userGetAll.getUsername())
                        .email(userGetAll.getEmail())
                        .build())
                .collect(Collectors.toList());

        assertAll(
                () -> assertEquals(2, result.get().size()),
                () -> assertEquals("Test 01", result.get().get(0).getNombre()),
                () -> assertEquals("Test 02", result.get().get(1).getNombre()),
                () -> assertEquals("test01user", result.get().get(0).getUserName()),
                () -> assertEquals("test02user", result.get().get(1).getUserName()),
                () -> assertEquals("test01user@mail.com", result.get().get(0).getEmail()),
                () -> assertEquals("test02user@mail.com", result.get().get(1).getEmail()),
                () -> assertEquals(result.get(), expectedResult)
        );

        verify(userApiRest, times(1)).getAllSync();
        verify(call, times(1)).execute();
    }
}