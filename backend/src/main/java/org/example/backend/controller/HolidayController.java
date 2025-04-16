package org.example.backend.controller;

import org.example.backend.service.HolidayService;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;

import java.util.List;
import java.util.Map;

@Tag(name = "Święta", description = "Kalendarz świąt")
@RestController
@RequestMapping("/api/swieta")
public class HolidayController {

    private final HolidayService service;

    public HolidayController(HolidayService service) {
        this.service = service;
    }

    @Operation(summary = "Pobierz święta dla kraju i roku")
    @GetMapping("/{country}/{year}")
    public List<Map<String, String>> getHolidays(@PathVariable String country, @PathVariable int year) {
        return service.getHolidays(country, year);
    }
}