package org.example.backend.controller;

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
    public String register(@RequestBody Map<String, String> body) {
        String username = body.get("username");
        String password = passwordEncoder.encode(body.get("password"));
        String firstName = body.get("firstName");
        String lastName = body.get("lastName");
        User.Role role = User.Role.valueOf(body.get("role").toUpperCase());

        User user = new User(username, password, role, firstName, lastName);
        userRepository.save(user);

        return "Zarejestrowano pomyślnie";
    }

    @Operation(summary = "Logowanie i generowanie tokenu JWT")
    @PostMapping("/login")
    public Map<String, String> login(@RequestBody Map<String, String> body) {
        String username = body.get("username");
        String password = body.get("password");

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Nieprawidłowy login"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Nieprawidłowe hasło");
        }

        String token = jwtUtil.generateToken(user.getUsername(), user.getRole().name());

        return Map.of("token", token);
    }
}