package org.example.backend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Schema(description = "Logowanie użytkownika")
public class LoginRequest {

    // Gettery i settery
    @Schema(description = "Login użytkownika", example = "jan.kowalski")
    private String username;

    @Schema(description = "Hasło użytkownika", example = "SuperHaslo123")
    private String password;

}