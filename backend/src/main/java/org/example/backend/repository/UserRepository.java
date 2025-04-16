package org.example.backend.repository;

import org.example.backend.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByUsername(String username);
    List<User> findByRole(User.Role role);
    // Możesz dodać inne metody wyszukiwania, np. po imieniu i nazwisku, jeśli potrzebujesz
    List<User> findByFirstNameAndLastName(String firstName, String lastName);
}