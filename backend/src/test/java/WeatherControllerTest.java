import org.example.backend.controller.WeatherController;
import org.example.backend.service.WeatherService;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class WeatherControllerTest {

    private final WeatherService service = mock(WeatherService.class);
    private final WeatherController controller = new WeatherController(service);

    @Test
    void getWeather_ShouldReturnWeatherInfo() {
        when(service.getWeather("Warsaw")).thenReturn(Map.of("temp", "20°C"));

        Map result = controller.getWeather("Warsaw");

        assertEquals("20°C", result.get("temp"));
    }
}