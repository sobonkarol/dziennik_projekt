package org.example.backend.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Setter
@Getter
@Document(collection = "oceny")
public class Ocena {
    @Id
    private String id;

    private String uczenId;
    private String nauczycielId;
    private String przedmiot;
    private int wartosc;
    private LocalDate data;

    public Ocena() {}

    public Ocena(String uczenId, String nauczycielId, String przedmiot, int wartosc, LocalDate data) {
        this.uczenId = uczenId;
        this.nauczycielId = nauczycielId;
        this.przedmiot = przedmiot;
        this.wartosc = wartosc;
        this.data = data;
    }

}