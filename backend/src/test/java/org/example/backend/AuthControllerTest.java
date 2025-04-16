package org.example.backend;

import org.example.backend.controller.AuthController;
import org.example.backend.model.User;
import org.example.backend.repository.UserRepository;
import org.example.backend.security.JwtUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private AuthController authController;

    @Test
    void shouldRegisterUserSuccessfully() {
        Map<String, String> body = Map.of(
                "username", "janek",
                "password", "haslo123",
                "role", "UCZEN"
        );

        String response = authController.register(body);

        assertEquals("Zarejestrowano pomyślnie", response);
        verify(userRepository).save(any(User.class));
    }

    @Test
    void shouldLoginUserAndReturnToken() {
        User user = new User("janek", "hashed", User.Role.UCZEN, "Jan", "Kowalski");

        when(userRepository.findByUsername("janek")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("haslo123", "hashed")).thenReturn(true);
        when(jwtUtil.generateToken("janek", "UCZEN")).thenReturn("mockedToken");

        Map<String, String> result = authController.login(Map.of(
                "username", "janek",
                "password", "haslo123"
        ));

        assertEquals("mockedToken", result.get("token"));
    }
}