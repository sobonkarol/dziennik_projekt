package org.example.backend;

import org.example.backend.controller.WeatherController;
import org.example.backend.service.WeatherService;
import org.junit.jupiter.api.Test;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class WeatherControllerTest {

    @Test
    public void testGetWeather() {
        WeatherService service = mock(WeatherService.class);
        WeatherController controller = new WeatherController(service);

        when(service.getWeather("Warszawa")).thenReturn(Map.of("temp", "10"));
        assertEquals("10", controller.getWeather("Warszawa").get("temp"));
    }
}