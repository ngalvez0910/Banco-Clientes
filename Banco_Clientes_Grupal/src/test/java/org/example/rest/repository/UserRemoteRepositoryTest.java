package org.example.rest.repository;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import org.example.clientes.model.Usuario;
import org.example.rest.UserApiRest;
import org.example.rest.responses.createUpdateDelete.Request;
import org.example.rest.responses.createUpdateDelete.UserCreate;
import org.example.rest.responses.createUpdateDelete.UserDelete;
import org.example.rest.responses.getAll.UserGetAll;
import org.example.rest.responses.getById.UserGetById;
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

    @Test
    void getAllEmptyList() throws IOException {
        var response = Response.success(List.of());
        var call = mock(Call.class);
        when(call.execute()).thenReturn(response);
        when(userApiRest.getAllSync()).thenReturn(call);

        Optional<List<Usuario>> result = userRemoteRepository.getAllSync();

        assertTrue(result.isPresent());
        assertTrue(result.get().isEmpty());

        verify(userApiRest, times(1)).getAllSync();
        verify(call, times(1)).execute();

    }

    @Test
    void getAllSyncOtherError() throws IOException {
        var call = mock(Call.class);
        when(call.execute()).thenThrow(new IOException("Error interno del servidor"));
        when(userApiRest.getAllSync()).thenReturn(call);

        Optional<List<Usuario>> result = userRemoteRepository.getAllSync();

        assertFalse(result.isPresent());

        verify(userApiRest, times(1)).getAllSync();
        verify(call, times(1)).execute();

    }
    @Test
    public void testGetAllSyncServerInternalError() throws IOException {
        // Given
        var mockCall = mock(Call.class);
        var mockResponse = mock(Response.class);
        when(mockCall.execute()).thenReturn(mockResponse);
        when(mockResponse.isSuccessful()).thenReturn(false);
        when(mockResponse.code()).thenReturn(500);
        when(userApiRest.getAllSync()).thenReturn(mockCall);

        // When
        Optional<List<Usuario>> result = userRemoteRepository.getAllSync();

        // Then
        assertFalse(result.isPresent());
        verify(userApiRest, times(1)).getAllSync();
        verify(mockCall, times(1)).execute();
    }

    @Test
    public void testGetAllSyncSuccessNullBody() throws IOException {
        var mockCall = mock(Call.class);
        var mockResponse = mock(Response.class);
        when(mockCall.execute()).thenReturn(mockResponse);
        when(mockResponse.isSuccessful()).thenReturn(true);
        when(mockResponse.body()).thenReturn(null);
        when(userApiRest.getAllSync()).thenReturn(mockCall);

        Optional<List<Usuario>> result = userRemoteRepository.getAllSync();

        assertFalse(result.isPresent());
        verify(userApiRest, times(1)).getAllSync();
        verify(mockCall, times(1)).execute();
    }

    @Test
    void getByIdSync() throws IOException {
        var id = 1;
        var user = UserGetById.builder().id(id).name("Test 01").username("test01user").email("test01user@mail.com").build();
        var response = Response.success(user);
        var call = mock(Call.class);
        when(call.execute()).thenReturn(response);
        when(userApiRest.getByIdSync(id)).thenReturn(call);

        Optional<Usuario> result = userRemoteRepository.getByIdSync(id);

        assertTrue(result.isPresent());
        Usuario expectedResult = Usuario.builder()
               .id(user.getId())
               .nombre(user.getName())
               .userName(user.getUsername())
               .email(user.getEmail())
               .build();

        assertEquals(expectedResult, result.get());

        verify(userApiRest, times(1)).getByIdSync(1L);
        verify(call, times(1)).execute();
    }

    @Test
    void getByIdNotFound() throws IOException {

        var id = 1;
        var response = Response.error(404, ResponseBody.create(null, String.valueOf(MediaType.get("application/json"))));
        var call = mock(Call.class);
        when(call.execute()).thenReturn(response);
        when(userApiRest.getByIdSync(id)).thenReturn(call);

        Optional<Usuario> result = userRemoteRepository.getByIdSync(id);

        assertFalse(result.isPresent());

        verify(userApiRest, times(1)).getByIdSync(1L);
        verify(call, times(1)).execute();
    }

    @Test
    void getByIdSyncServerInternalError() throws IOException {
        var id = 1;
        var call = mock(Call.class);
        when(call.execute()).thenThrow(new IOException("Error interno del servidor"));
        when(userApiRest.getByIdSync(id)).thenReturn(call);

        Optional<Usuario> result = userRemoteRepository.getByIdSync(id);

        assertFalse(result.isPresent());

        verify(userApiRest, times(1)).getByIdSync(1L);
        verify(call, times(1)).execute();
    }

    @Test
    public void getByIdSyncOtherError() throws IOException {
        // Given
        long userId = 123;
        var mockCall = mock(Call.class);
        var mockResponse = mock(Response.class);
        when(mockCall.execute()).thenReturn(mockResponse);
        when(mockResponse.isSuccessful()).thenReturn(false);
        when(mockResponse.code()).thenReturn(5000);
        when(userApiRest.getByIdSync(userId)).thenReturn(mockCall);

        // When
        Optional<Usuario> result = userRemoteRepository.getByIdSync(userId);

        // Then
        assertFalse(result.isPresent());
        verify(userApiRest, times(1)).getByIdSync(userId);
        verify(mockCall, times(1)).execute();

    }


    @Test
    void createUserSync() throws IOException {

        var user = Usuario.builder().nombre("Test 01").userName("test01user").email("test01user@mail.com").build();
        var response = Response.success(UserCreate.builder().id(1L).name("Test 01").username("test01user").email("test01user@mail.com").build());
        var call = mock(Call.class);
        when(call.execute()).thenReturn(response);
        when(userApiRest.createUserSync(any(Request.class))).thenReturn(call);

        Optional<Usuario> result = userRemoteRepository.createUserSync(user);

        assertTrue(result.isPresent());
        Usuario expectedResult = Usuario.builder()
               .id(response.body().getId())
               .nombre(response.body().getName())
               .userName(response.body().getUsername())
               .email(response.body().getEmail())
               .build();

        assertAll(
                () -> assertEquals(expectedResult.getId(), result.get().getId()),
                () -> assertEquals(expectedResult.getNombre(), result.get().getNombre()),
                () -> assertEquals(expectedResult.getUserName(), result.get().getUserName()),
                () -> assertEquals(expectedResult.getEmail(), result.get().getEmail())
        );

        verify(userApiRest, times(1)).createUserSync(any(Request.class));
        verify(call, times(1)).execute();

    }

    @Test
    public void createUserSyncNotSuccessfulInternalServerError() throws IOException {

        var user = Usuario.builder().nombre("Test 01").userName("test01user").email("test01user@mail.com").build();
        var call = mock(Call.class);
        var response = Response.error(500, mock(ResponseBody.class));
        when(call.execute()).thenReturn(response);
        when(userApiRest.createUserSync(any(Request.class))).thenReturn(call);

        Optional<Usuario> result = userRemoteRepository.createUserSync(user);

        assertFalse(result.isPresent());

        verify(userApiRest, times(1)).createUserSync(any(Request.class));
        verify(call, times(1)).execute();
    }
    @Test
    public void createUserSyncNotSuccessfulOtherError() throws IOException {

        var user = Usuario.builder().nombre("Test 01").userName("test01user").email("test01user@mail.com").build();
        var call = mock(Call.class);
        var response = Response.error(5000, mock(ResponseBody.class));
        when(call.execute()).thenReturn(response);
        when(userApiRest.createUserSync(any(Request.class))).thenReturn(call);

        Optional<Usuario> result = userRemoteRepository.createUserSync(user);

        assertFalse(result.isPresent());

        verify(userApiRest, times(1)).createUserSync(any(Request.class));
        verify(call, times(1)).execute();
    }

    @Test 
    void createUserSyncOtherError() throws IOException{

        var user = Usuario.builder().nombre("Test 01").userName("test01user").email("test01user@mail.com").build();
        var call = mock(Call.class);
        when(call.execute()).thenThrow(new IOException("Error interno del servidor"));
        when(userApiRest.createUserSync(any(Request.class))).thenReturn(call);

        Optional<Usuario> result = userRemoteRepository.createUserSync(user);

        assertFalse(result.isPresent());

        verify(userApiRest, times(1)).createUserSync(any(Request.class));
        verify(call, times(1)).execute();
    } 
    
    @Test
    void updateUserSync() throws IOException {

        var id = 1L;
        var user = Usuario.builder().nombre("Test 01").userName("test01user").email("test01user@mail.com").build();
        var response = Response.success(UserCreate.builder().id(id).name("Test 01").username("test01user").email("test01user@mail.com").build());
        var call = mock(Call.class);
        when(call.execute()).thenReturn(response);
        when(userApiRest.updateUserSync(eq(id), any(Request.class))).thenReturn(call);

        Optional<Usuario> result = userRemoteRepository.updateUserSync(id, user);

        assertTrue(result.isPresent());
        Usuario expectedResult = Usuario.builder()
               .id(response.body() != null ? response.body().getId() : null)
               .nombre(response.body().getName())
               .userName(response.body().getUsername())
               .email(response.body().getEmail())
               .build();

        assertAll(
                () -> assertEquals(expectedResult.getId(), result.get().getId()),
                () -> assertEquals(expectedResult.getNombre(), result.get().getNombre()),
                () -> assertEquals(expectedResult.getUserName(), result.get().getUserName()),
                () -> assertEquals(expectedResult.getEmail(), result.get().getEmail())
        );

        verify(userApiRest, times(1)).updateUserSync(eq(id), any(Request.class));
        verify(call, times(1)).execute();

    }
    @Test
    void updateUserSyncInternalServerError() throws IOException {

        var id = 1L;
        var user = Usuario.builder().nombre("Test 01").userName("test01user").email("test01user@mail.com").build();
        var call = mock(Call.class);
        when(call.execute()).thenThrow(new IOException("Error interno del servidor"));
        when(userApiRest.updateUserSync(eq(id), any(Request.class))).thenReturn(call);

        Optional<Usuario> result = userRemoteRepository.updateUserSync(id, user);

        assertFalse(result.isPresent());

        verify(userApiRest, times(1)).updateUserSync(eq(id), any(Request.class));
        verify(call, times(1)).execute();
    }
    @Test
    void updateUserSyncNotFound() throws IOException {
        var id = 12L;
        var user = Usuario.builder().nombre("Test 01").userName("test01user").email("test01user@mail.com").build();
        var call = mock(Call.class);
        when(call.execute()).thenReturn(Response.error(404, ResponseBody.create(null, String.valueOf(MediaType.get("application/json")))));
        when(userApiRest.updateUserSync(eq(id), any(Request.class))).thenReturn(call);
        Optional<Usuario> result = userRemoteRepository.updateUserSync(id, user);

        assertFalse(result.isPresent());

        verify(userApiRest, times(1)).updateUserSync(eq(id), any(Request.class));
        verify(call, times(1)).execute();
    }

    @Test
    public void updateUserSyncOtherError() throws IOException {

        long userId = 123L;
        var user = Usuario.builder().nombre("Test 01").userName("test01user").email("test01user@mail.com").build();
        var mockCall = mock(Call.class);
        var mockResponse = mock(Response.class);
        when(mockCall.execute()).thenReturn(mockResponse);
        when(mockResponse.isSuccessful()).thenReturn(false);
        when(mockResponse.code()).thenReturn(5000);
        when(userApiRest.updateUserSync(eq(userId), any(Request.class))).thenReturn(mockCall);

        Optional<Usuario> result = userRemoteRepository.updateUserSync(userId, user);

        assertFalse(result.isPresent());
        verify(userApiRest, times(1)).updateUserSync(eq(userId), any(Request.class));
        verify(mockCall, times(1)).execute();

    }
    @Test
    void deleteUserSync() throws IOException {

        var id = 1L;
        var response = Response.success(UserDelete.builder().id(id).build());
        var call = mock(Call.class);
        when(call.execute()).thenReturn(response);
        when(userApiRest.deleteUserSync(id)).thenReturn(call);

        Optional<Usuario> result = userRemoteRepository.deleteUserSync(id);

        assertTrue(result.isPresent());
        // delete no devuelve user cuando es correcto

        verify(userApiRest, times(1)).deleteUserSync(id);
        verify(call, times(1)).execute();

    }

    @Test
    void deleteUserSyncNotFound() throws IOException {

        var id = 12L;
        var call = mock(Call.class);
        when(call.execute()).thenReturn(Response.error(404, ResponseBody.create(null, String.valueOf(MediaType.get("application/json")))));
        when(userApiRest.deleteUserSync(id)).thenReturn(call);

        Optional<Usuario> result = userRemoteRepository.deleteUserSync(id);

        assertFalse(result.isPresent());

        verify(userApiRest, times(1)).deleteUserSync(id);
        verify(call, times(1)).execute();

    }

    @Test
    void deleteUserSyncOtherError() throws IOException {

        var id = 1L;
        var call = mock(Call.class);
        when(call.execute()).thenReturn(Response.error(400, ResponseBody.create(null, String.valueOf(MediaType.get("application/json")))));
        when(userApiRest.deleteUserSync(id)).thenReturn(call);

        Optional<Usuario> result = userRemoteRepository.deleteUserSync(id);

        assertFalse(result.isPresent());

        verify(userApiRest, times(1)).deleteUserSync(id);
        verify(call, times(1)).execute();

    }
    @Test
    void deleteUserSyncInternalServerError() throws IOException {

        var id = 1L;
        var call = mock(Call.class);
        when(call.execute()).thenThrow(new IOException("Error interno del servidor"));
        when(userApiRest.deleteUserSync(id)).thenReturn(call);

        Optional<Usuario> result = userRemoteRepository.deleteUserSync(id);

        assertFalse(result.isPresent());

        verify(userApiRest, times(1)).deleteUserSync(id);
        verify(call, times(1)).execute();
        
    }

}
