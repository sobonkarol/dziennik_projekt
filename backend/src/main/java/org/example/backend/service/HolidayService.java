package org.example.backend.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class HolidayService {

    private final RestTemplate restTemplate = new RestTemplate();

    private static final Map<String, String> HOLIDAY_TRANSLATIONS = Map.ofEntries(
            Map.entry("New Year's Day", "Nowy Rok"),
            Map.entry("Epiphany", "Trzech Króli"),
            Map.entry("Good Friday", "Wielki Piątek"),
            Map.entry("Easter Sunday", "Niedziela Wielkanocna"),
            Map.entry("Easter Monday", "Poniedziałek Wielkanocny"),
            Map.entry("International Workers' Day", "Święto Pracy"),
            Map.entry("Ascension Day", "Wniebowstąpienie Pańskie"),
            Map.entry("Whit Sunday", "Zesłanie Ducha Świętego"),
            Map.entry("Whit Monday", "Poniedziałek po Zesłaniu Ducha Świętego"),
            Map.entry("Corpus Christi", "Boże Ciało"),
            Map.entry("Assumption of Mary", "Wniebowzięcie NMP"),
            Map.entry("All Saints' Day", "Wszystkich Świętych"),
            Map.entry("Independence Day", "Święto Niepodległości"),
            Map.entry("Christmas Day", "Boże Narodzenie"),
            Map.entry("Second Christmas Day", "Drugi dzień Świąt Bożego Narodzenia"),
            Map.entry("Constitution Day", "Święto Konstytucji 3 Maja")
    );

    public List<Map<String, String>> getHolidays(String countryCode, int year) {
        String url = String.format("https://date.nager.at/api/v3/PublicHolidays/%d/%s", year, countryCode);
        Map[] response = restTemplate.getForObject(url, Map[].class);

        if (response == null) {
            return Collections.emptyList();
        }

        return Arrays.stream(response)
                .map(item -> {
                    String englishName = (String) item.get("localName");
                    String translatedName = HOLIDAY_TRANSLATIONS.getOrDefault(englishName, englishName);

                    return Map.of(
                            "name", translatedName,
                            "date", (String) item.get("date")
                    );
                })
                .collect(Collectors.toList());
    }
}