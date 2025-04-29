import org.example.backend.controller.UserController;
import org.example.backend.model.User;
import org.example.backend.repository.UserRepository;
import org.example.backend.security.JwtUtil;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserControllerTest {

    private final UserRepository userRepository = mock(UserRepository.class);
    private final JwtUtil jwtUtil = mock(JwtUtil.class);
    private final UserController userController = new UserController(userRepository, jwtUtil);

    @Test
    void getUsersByRole_Nauczyciel_ShouldReturnListOfUczniowie() {
        when(jwtUtil.extractRole(anyString())).thenReturn("NAUCZYCIEL");
        when(userRepository.findByRole(User.Role.UCZEN)).thenReturn(List.of(new User()));

        List<User> result = userController.getUsersByRole("Bearer token");

        assertEquals(1, result.size());
    }

    @Test
    void getUsersByRole_Uczen_ShouldReturnSelf() {
        User uczen = new User();
        when(jwtUtil.extractRole(anyString())).thenReturn("UCZEN");
        when(jwtUtil.extractUsername(anyString())).thenReturn("student");
        when(userRepository.findByUsername("student")).thenReturn(Optional.of(uczen));

        List<User> result = userController.getUsersByRole("Bearer token");

        assertEquals(1, result.size());
    }

    @Test
    void getUserByIdOrUsername_ValidId_ShouldReturnUser() {
        User user = new User();
        when(userRepository.findById(anyString())).thenReturn(Optional.of(user));

        ResponseEntity<User> response = userController.getUserByIdOrUsername("507f1f77bcf86cd799439011");

        assertEquals(user, response.getBody());
    }

    @Test
    void getUserByIdOrUsername_ValidUsername_ShouldReturnUser() {
        User user = new User();
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));

        ResponseEntity<User> response = userController.getUserByIdOrUsername("testuser");

        assertEquals(user, response.getBody());
    }
}