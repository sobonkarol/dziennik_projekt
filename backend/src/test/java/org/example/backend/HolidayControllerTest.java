package org.example.backend;

import org.example.backend.controller.HolidayController;
import org.example.backend.service.HolidayService;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class HolidayControllerTest {

    @Test
    public void testGetHolidays() {
        HolidayService service = mock(HolidayService.class);
        HolidayController controller = new HolidayController(service);

        when(service.getHolidays("PL", 2025)).thenReturn(List.of(Map.of("name", "Nowy Rok", "date", "2025-01-01")));
        assertEquals(1, controller.getHolidays("PL", 2025).size());
    }
}