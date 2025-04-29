package org.example.backend.controller;

import org.example.backend.dto.RegisterRequest;
import org.example.backend.dto.LoginRequest;
import org.example.backend.model.User;
import org.example.backend.repository.UserRepository;
import org.example.backend.security.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;

import java.util.Map;

@Tag(name = "Autoryzacja", description = "Rejestracja i logowanie użytkowników")
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @Operation(summary = "Rejestracja nowego użytkownika")
    @PostMapping("/register")
    public String register(@RequestBody RegisterRequest request) {
        String username = request.getUsername();
        String password = passwordEncoder.encode(request.getPassword());
        String firstName = request.getFirstName();
        String lastName = request.getLastName();
        User.Role role = User.Role.valueOf(request.getRole().toUpperCase());

        if (username == null || username.length() < 4 || username.contains(" ")) {
            throw new RuntimeException("Login musi mieć co najmniej 4 znaki i nie może zawierać spacji");
        }

        if (userRepository.findByUsername(username).isPresent()) {
            throw new RuntimeException("Login jest już zajęty");
        }

        User user = new User(username, password, role, firstName, lastName);
        userRepository.save(user);

        return "Zarejestrowano pomyślnie";
    }

    @Operation(summary = "Logowanie i generowanie tokenu JWT")
    @PostMapping("/login")
    public Map<String, String> login(@RequestBody LoginRequest request) {
        String username = request.getUsername();
        String password = request.getPassword();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Nieprawidłowy login"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Nieprawidłowe hasło");
        }

        String token = jwtUtil.generateToken(user.getUsername(), user.getRole().name());

        return Map.of("token", token);
    }
}