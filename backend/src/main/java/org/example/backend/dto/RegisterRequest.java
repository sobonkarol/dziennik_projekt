package org.example.backend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Schema(description = "Rejestracja użytkownika")
public class RegisterRequest {

    // Gettery i settery
    @Schema(description = "Login użytkownika", example = "jan.kowalski")
    private String username;

    @Schema(description = "Hasło użytkownika", example = "SuperHaslo123")
    private String password;

    @Schema(description = "Imię użytkownika", example = "Jan")
    private String firstName;

    @Schema(description = "Nazwisko użytkownika", example = "Kowalski")
    private String lastName;

    @Schema(description = "Rola użytkownika", example = "UCZEN lub NAUCZYCIEL")
    private String role;

}