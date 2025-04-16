package org.example.backend.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUczenId() {
        return uczenId;
    }

    public void setUczenId(String uczenId) {
        this.uczenId = uczenId;
    }

    public String getNauczycielId() {
        return nauczycielId;
    }

    public void setNauczycielId(String nauczycielId) {
        this.nauczycielId = nauczycielId;
    }

    public String getPrzedmiot() {
        return przedmiot;
    }

    public void setPrzedmiot(String przedmiot) {
        this.przedmiot = przedmiot;
    }

    public int getWartosc() {
        return wartosc;
    }

    public void setWartosc(int wartosc) {
        this.wartosc = wartosc;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }
}