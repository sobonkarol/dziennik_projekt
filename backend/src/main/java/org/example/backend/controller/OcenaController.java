package org.example.backend.controller;

import org.example.backend.exception.OcenaNotFoundException;
import org.example.backend.model.Ocena;
import org.example.backend.model.User;
import org.example.backend.repository.OcenaRepository;
import org.example.backend.repository.UserRepository;
import org.example.backend.security.JwtUtil;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Tag(name = "Oceny", description = "Zarządzanie ocenami uczniów")
@RestController
@RequestMapping("/api/oceny")
public class OcenaController {

    private final OcenaRepository ocenaRepository;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    public OcenaController(OcenaRepository ocenaRepository, UserRepository userRepository, JwtUtil jwtUtil) {
        this.ocenaRepository = ocenaRepository;
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
    }

    @Operation(summary = "Pobierz wszystkie oceny (dla nauczyciela wszystkie, dla ucznia tylko swoje)")
    @GetMapping
    public List<Ocena> getAll(@RequestHeader("Authorization") String authorizationHeader) {
        String token = authorizationHeader.substring(7);
        String role = jwtUtil.extractRole(token);
        String username = jwtUtil.extractUsername(token);

        if (role.equals("NAUCZYCIEL")) {
            return ocenaRepository.findAll();
        } else if (role.equals("UCZEN")) {
            User uczen = userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("Użytkownik nie istnieje"));
            return ocenaRepository.findByUczenId(uczen.getId());
        } else {
            throw new RuntimeException("Nieznana rola");
        }
    }

    @Operation(summary = "Pobierz oceny konkretnego ucznia")
    @GetMapping("/uczen/{uczenId}")
    public List<Ocena> getOcenyUcznia(@PathVariable String uczenId) {
        return ocenaRepository.findByUczenId(uczenId);
    }

    @Operation(summary = "Dodaj nową ocenę")
    @PostMapping
    public Ocena addOcena(@RequestBody Ocena ocena) {
        ocena.setData(LocalDate.now());
        return ocenaRepository.save(ocena);
    }

    @Operation(summary = "Aktualizuj ocenę")
    @PutMapping("/{id}")
    public Ocena updateOcena(@PathVariable String id, @RequestBody Ocena updatedOcena) {
        Optional<Ocena> existingOcena = ocenaRepository.findById(id);
        if (existingOcena.isPresent()) {
            Ocena ocenaToUpdate = existingOcena.get();
            ocenaToUpdate.setPrzedmiot(updatedOcena.getPrzedmiot());
            ocenaToUpdate.setWartosc(updatedOcena.getWartosc());
            ocenaToUpdate.setData(LocalDate.now());
            return ocenaRepository.save(ocenaToUpdate);
        } else {
            throw new OcenaNotFoundException("Ocena o ID '" + id + "' nie istnieje");
        }
    }

    @Operation(summary = "Usuń ocenę")
    @DeleteMapping("/{id}")
    public void deleteOcena(@PathVariable String id) {
        ocenaRepository.deleteById(id);
    }
}