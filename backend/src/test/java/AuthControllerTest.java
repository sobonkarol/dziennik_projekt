import org.example.backend.controller.AuthController;
import org.example.backend.dto.RegisterRequest;
import org.example.backend.dto.LoginRequest;
import org.example.backend.model.User;
import org.example.backend.repository.UserRepository;
import org.example.backend.security.JwtUtil;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthControllerTest {

    private final UserRepository userRepository = mock(UserRepository.class);
    private final PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);
    private final JwtUtil jwtUtil = mock(JwtUtil.class);
    private final AuthController controller = new AuthController(userRepository, passwordEncoder, jwtUtil);

    @Test
    void register_ShouldSaveUser() {
        RegisterRequest request = new RegisterRequest();
        request.setUsername("student");
        request.setPassword("password");
        request.setRole("UCZEN");
        request.setFirstName("Jan");
        request.setLastName("Kowalski");

        when(userRepository.findByUsername("student")).thenReturn(Optional.empty());
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");

        String response = controller.register(request);

        assertEquals("Zarejestrowano pomyślnie", response);
    }

    @Test
    void login_ShouldReturnToken() {
        LoginRequest request = new LoginRequest();
        request.setUsername("student");
        request.setPassword("password");

        User user = new User();
        user.setUsername("student");
        user.setPassword("encodedPassword");
        user.setRole(User.Role.UCZEN); // ← DODAJ TO!

        when(userRepository.findByUsername("student")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("password", "encodedPassword")).thenReturn(true);
        when(jwtUtil.generateToken(anyString(), anyString())).thenReturn("token");

        Map<String, String> response = controller.login(request);

        assertEquals("token", response.get("token"));
    }
}