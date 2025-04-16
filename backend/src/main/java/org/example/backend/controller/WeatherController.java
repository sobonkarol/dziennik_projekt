package org.example.backend.controller;

import org.example.backend.service.WeatherService;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;

import java.util.Map;

@Tag(name = "Pogoda", description = "Informacje pogodowe")
@RestController
@RequestMapping("/api/pogoda")
public class WeatherController {

    private final WeatherService service;

    public WeatherController(WeatherService service) {
        this.service = service;
    }

    @Operation(summary = "Pobierz pogodę dla danego miasta")
    @GetMapping("/{miasto}")
    public Map getWeather(@PathVariable String miasto) {
        return service.getWeather(miasto);
    }
}