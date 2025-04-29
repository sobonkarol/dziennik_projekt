package org.example.backend.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@Document(collection = "users")
public class User {
    @Id
    private String id;

    private String username;
    private String password;
    private Role role;
    private Set<String> ocenyIds = new HashSet<>();
    private String firstName;  // Dodanie imienia
    private String lastName;   // Dodanie nazwiska

    public enum Role {
        UCZEN,
        NAUCZYCIEL
    }

    public User() {}

    public User(String username, String password, Role role, String firstName, String lastName) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.firstName = firstName;
        this.lastName = lastName;
    }

}
