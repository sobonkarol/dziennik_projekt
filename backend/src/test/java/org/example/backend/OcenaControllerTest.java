package org.example.backend;

import org.example.backend.controller.OcenaController;
import org.example.backend.model.Ocena;
import org.example.backend.repository.OcenaRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class OcenaControllerTest {

    @Test
    void testGetAll() {
        OcenaRepository ocenaRepository = mock(OcenaRepository.class);
        OcenaController ocenaController = new OcenaController(ocenaRepository);

        Ocena ocena = new Ocena("uczen1", "nauczyciel1", "Matematyka", 5, LocalDate.now());
        when(ocenaRepository.findAll()).thenReturn(List.of(ocena));

        List<Ocena> result = ocenaController.getAll();

        assertEquals(1, result.size());
        assertEquals("Matematyka", result.get(0).getPrzedmiot());
    }

    @Test
    void testGetOcenyUcznia() {
        OcenaRepository ocenaRepository = mock(OcenaRepository.class);
        OcenaController ocenaController = new OcenaController(ocenaRepository);

        Ocena ocena = new Ocena("uczen1", "nauczyciel1", "Biologia", 4, LocalDate.now());
        when(ocenaRepository.findByUczenId("uczen1")).thenReturn(List.of(ocena));

        List<Ocena> result = ocenaController.getOcenyUcznia("uczen1");

        assertEquals(1, result.size());
        assertEquals("Biologia", result.get(0).getPrzedmiot());
    }

    @Test
    void testAddOcena() {
        OcenaRepository ocenaRepository = mock(OcenaRepository.class);
        OcenaController ocenaController = new OcenaController(ocenaRepository);

        Ocena ocena = new Ocena("uczen2", "nauczyciel2", "Historia", 3, LocalDate.now());
        when(ocenaRepository.save(Mockito.any(Ocena.class))).thenReturn(ocena);

        Ocena result = ocenaController.addOcena(ocena);

        assertEquals("Historia", result.getPrzedmiot());
        assertEquals(3, result.getWartosc());
    }

    @Test
    void testUpdateOcena() {
        OcenaRepository ocenaRepository = mock(OcenaRepository.class);
        OcenaController ocenaController = new OcenaController(ocenaRepository);

        Ocena existing = new Ocena("uczen3", "nauczyciel3", "Geografia", 2, LocalDate.now());
        existing.setId("123");

        Ocena updated = new Ocena("uczen3", "nauczyciel3", "Geografia", 5, LocalDate.now());

        when(ocenaRepository.findById("123")).thenReturn(Optional.of(existing));
        when(ocenaRepository.save(Mockito.any(Ocena.class))).thenReturn(updated);

        Ocena result = ocenaController.updateOcena("123", updated);

        assertEquals(5, result.getWartosc());
    }

    @Test
    void testDeleteOcena() {
        OcenaRepository ocenaRepository = mock(OcenaRepository.class);
        OcenaController ocenaController = new OcenaController(ocenaRepository);

        Ocena ocena = new Ocena("uczen4", "nauczyciel4", "Fizyka", 6, LocalDate.now());
        ocena.setId("999");

        doNothing().when(ocenaRepository).deleteById("999");

        ocenaController.deleteOcena("999");

        verify(ocenaRepository, times(1)).deleteById("999");
    }
}