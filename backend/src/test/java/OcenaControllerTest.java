import org.example.backend.controller.OcenaController;
import org.example.backend.model.Ocena;
import org.example.backend.model.User;
import org.example.backend.repository.OcenaRepository;
import org.example.backend.repository.UserRepository;
import org.example.backend.security.JwtUtil;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OcenaControllerTest {

    private final OcenaRepository ocenaRepository = mock(OcenaRepository.class);
    private final UserRepository userRepository = mock(UserRepository.class);
    private final JwtUtil jwtUtil = mock(JwtUtil.class);
    private final OcenaController ocenaController = new OcenaController(ocenaRepository, userRepository, jwtUtil);

    @Test
    void getAll_Nauczyciel_ShouldReturnAll() {
        when(jwtUtil.extractRole(anyString())).thenReturn("NAUCZYCIEL");
        when(ocenaRepository.findAll()).thenReturn(List.of(new Ocena()));

        List<Ocena> result = ocenaController.getAll("Bearer token");

        assertEquals(1, result.size());
    }

    @Test
    void getAll_Uczen_ShouldReturnOwnGrades() {
        User user = new User();
        user.setId("uczenId");
        when(jwtUtil.extractRole(anyString())).thenReturn("UCZEN");
        when(jwtUtil.extractUsername(anyString())).thenReturn("student");
        when(userRepository.findByUsername("student")).thenReturn(Optional.of(user));
        when(ocenaRepository.findByUczenId("uczenId")).thenReturn(List.of(new Ocena()));

        List<Ocena> result = ocenaController.getAll("Bearer token");

        assertEquals(1, result.size());
    }

    @Test
    void getOcenyUcznia_ShouldReturnGrades() {
        when(ocenaRepository.findByUczenId(anyString())).thenReturn(List.of(new Ocena()));

        List<Ocena> result = ocenaController.getOcenyUcznia("id");

        assertEquals(1, result.size());
    }

    @Test
    void addOcena_ShouldSaveAndReturnOcena() {
        Ocena ocena = new Ocena();
        when(ocenaRepository.save(any(Ocena.class))).thenReturn(ocena);

        Ocena result = ocenaController.addOcena(new Ocena());

        assertNotNull(result);
    }

    @Test
    void updateOcena_ShouldUpdateOcena() {
        Ocena ocena = new Ocena();
        when(ocenaRepository.findById(anyString())).thenReturn(Optional.of(ocena));
        when(ocenaRepository.save(any(Ocena.class))).thenReturn(ocena);

        Ocena result = ocenaController.updateOcena("id", new Ocena());

        assertNotNull(result);
    }

    @Test
    void deleteOcena_ShouldDelete() {
        doNothing().when(ocenaRepository).deleteById(anyString());

        assertDoesNotThrow(() -> ocenaController.deleteOcena("id"));
    }
}