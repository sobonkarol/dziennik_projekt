package org.example.backend.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class WeatherService {

    @Value("${weather.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate = new RestTemplate();

    public Map<String, Object> getWeather(String city) {
        String url = String.format(
                "https://api.openweathermap.org/data/2.5/weather?q=%s&appid=%s&units=metric&lang=pl",
                city, apiKey
        );

        Map response = restTemplate.getForObject(url, Map.class);

        if (response == null || response.isEmpty()) {
            return Collections.emptyMap();
        }

        Map<String, Object> result = new HashMap<>();

        // Bezpieczne pobieranie danych
        Map<String, Object> main = (Map<String, Object>) response.get("main");
        List<Map<String, Object>> weatherList = (List<Map<String, Object>>) response.get("weather");
        Map<String, Object> wind = (Map<String, Object>) response.get("wind");

        if (main != null) {
            result.put("temperature", main.getOrDefault("temp", "Brak danych"));
            result.put("feels_like", main.getOrDefault("feels_like", "Brak danych"));
            result.put("pressure", main.getOrDefault("pressure", "Brak danych"));
            result.put("humidity", main.getOrDefault("humidity", "Brak danych"));
        }

        if (weatherList != null && !weatherList.isEmpty()) {
            Map<String, Object> weather = weatherList.get(0);
            result.put("weather_main", weather.getOrDefault("main", "Brak danych"));
            result.put("weather_description", weather.getOrDefault("description", "Brak danych"));
            result.put("weather_icon", weather.getOrDefault("icon", "Brak danych"));
        }

        if (wind != null) {
            result.put("wind_speed", wind.getOrDefault("speed", "Brak danych"));
            result.put("wind_deg", wind.getOrDefault("deg", "Brak danych"));
        }

        result.put("city_name", response.getOrDefault("name", city));
        result.put("country", ((Map<String, Object>) response.get("sys")).getOrDefault("country", "Brak danych"));

        return result;
    }
}