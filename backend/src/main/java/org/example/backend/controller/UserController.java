package org.example.backend.controller;

import org.bson.types.ObjectId;
import org.example.backend.exception.UserNotFoundException;
import org.example.backend.model.User;
import org.example.backend.repository.UserRepository;
import org.example.backend.security.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    public UserController(UserRepository userRepository, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
    }

    @GetMapping
    public List<User> getUsersByRole(@RequestHeader("Authorization") String authorizationHeader) {
        String token = authorizationHeader.substring(7); // usuń "Bearer "
        String role = jwtUtil.extractRole(token);
        String username = jwtUtil.extractUsername(token);

        if (role.equals("NAUCZYCIEL")) {
            return userRepository.findByRole(User.Role.UCZEN); // nauczyciel widzi wszystkich uczniów
        } else if (role.equals("UCZEN")) {
            User uczen = userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("Użytkownik nie istnieje"));
            return List.of(uczen); // uczeń widzi tylko siebie
        } else {
            throw new RuntimeException("Nieznana rola");
        }
    }

    @GetMapping("/{idOrUsername}")
    public ResponseEntity<User> getUserByIdOrUsername(@PathVariable String idOrUsername) {
        Optional<User> userOptional;
        try {
            ObjectId objectId = new ObjectId(idOrUsername);
            userOptional = userRepository.findById(objectId.toHexString());
        } catch (IllegalArgumentException e) {
            // Jeśli przekazana wartość nie jest poprawnym ObjectId, spróbuj znaleźć po username
            userOptional = userRepository.findByUsername(idOrUsername);
        }

        return userOptional.map(ResponseEntity::ok)
                .orElseThrow(() -> new UserNotFoundException("Użytkownik o ID lub nazwie '" + idOrUsername + "' nie istnieje"));
    }
}