package org.example.backend.controller;

import org.bson.types.ObjectId;
import org.example.backend.exception.UserNotFoundException;
import org.example.backend.model.User;
import org.example.backend.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping
    public List<User> getUsersByRole(@RequestParam String role) {
        return userRepository.findByRole(User.Role.valueOf(role.toUpperCase()));
    }

    @GetMapping("/{idOrUsername}")
    public ResponseEntity<User> getUserByIdOrUsername(@PathVariable String idOrUsername) {
        Optional<User> userOptional;
        try {
            // Spróbuj znaleźć po _id (ObjectId)
            ObjectId objectId = new ObjectId(idOrUsername);
            userOptional = userRepository.findById(objectId.toHexString());
        } catch (IllegalArgumentException e) {
            // Jeśli przekazana wartość nie jest poprawnym ObjectId, spróbuj znaleźć po username
            userOptional = userRepository.findByUsername(idOrUsername);
        }

        if (userOptional.isPresent()) {
            return ResponseEntity.ok(userOptional.get());
        } else {
            throw new UserNotFoundException("Użytkownik o ID lub nazwie '" + idOrUsername + "' nie istnieje");
        }
    }
}