package org.example.backend.controller;

import org.example.backend.exception.OcenaNotFoundException;
import org.example.backend.model.Ocena;
import org.example.backend.repository.OcenaRepository;
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

    public OcenaController(OcenaRepository ocenaRepository) {
        this.ocenaRepository = ocenaRepository;
    }

    @Operation(summary = "Pobierz wszystkie oceny")
    @GetMapping
    public List<Ocena> getAll() {
        return ocenaRepository.findAll();
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