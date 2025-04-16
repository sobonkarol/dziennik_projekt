package org.example.backend;

import org.example.backend.controller.UserController;
import org.example.backend.model.User;
import org.example.backend.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserControllerTest {

    @Test
    void testGetUsersByRole() {
        UserRepository userRepository = mock(UserRepository.class);
        UserController userController = new UserController(userRepository);

        User uczen = new User("janek", "haslo", User.Role.UCZEN, "Jan", "Kowalski");
        when(userRepository.findByRole(User.Role.UCZEN)).thenReturn(List.of(uczen));

        List<User> result = userController.getUsersByRole("UCZEN");

        assertEquals(1, result.size());
        assertEquals("janek", result.get(0).getUsername());
    }

    @Test
    void testGetUserByUsername() {
        UserRepository userRepository = mock(UserRepository.class);
        UserController userController = new UserController(userRepository);

        // Użytkownik bez ID — szukamy po username
        User user = new User("admin", "haslo", User.Role.NAUCZYCIEL, "Adam", "Nowak");

        when(userRepository.findById("admin")).thenReturn(Optional.empty());
        when(userRepository.findByUsername("admin")).thenReturn(Optional.of(user));

        ResponseEntity<User> response = userController.getUserByIdOrUsername("admin");

        assertNotNull(response.getBody());
        assertEquals("Adam", response.getBody().getFirstName());
        assertEquals("Nowak", response.getBody().getLastName());
    }
}