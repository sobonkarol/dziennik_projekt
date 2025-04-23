# 📘 Dziennik ocen

Aplikacja webowa typu dziennik elektroniczny dla nauczycieli i uczniów. Umożliwia zarządzanie ocenami, podgląd wyników, autoryzację użytkowników oraz integrację z zewnętrznymi API pogodowymi i świątecznymi.

## 📌 Funkcje

### 👨‍🏫 Nauczyciel
- Przegląd listy uczniów
- Dodawanie, edytowanie i usuwanie ocen
- Widok ocen uporządkowany alfabetycznie
- Widget pogodowy (OpenWeatherMap)
- Widget dni wolnych (Nager.Date)

### 👨‍🎓 Uczeń
- Przegląd własnych ocen
- Sortowanie i filtrowanie ocen według przedmiotu

### 🔐 Autoryzacja
- Rejestracja z wyborem roli (UCZEN / NAUCZYCIEL)
- Logowanie z JWT
- Przekierowania w zależności od roli

---

## ⚙️ Stack technologiczny

| Frontend                      | Backend                        |
|------------------------------|--------------------------------|
| React 19                     | Spring Boot 3.2.4              |
| TypeScript                   | MongoDB                        |
| Vite                         | Spring Security + JWT          |
| Mantine + Emotion (UI)       | Spring Data MongoDB            |
| Axios                        | Swagger (springdoc-openapi)    |

---

## 🧪 Testy

- Testy jednostkowe dla kontrolerów REST (`@WebMvcTest`)
- Wykorzystanie `MockMvc`, `Mockito`, `JUnit 5`

---

## 🛠️ Instalacja

### Backend

```bash
cd backend
./mvnw spring-boot:run
```

Wymagania:
- Java 17+
- MongoDB (lokalnie lub w chmurze)

### Frontend

```bash
cd frontend
npm install
npm run dev
```

Wymagania:
- Node.js 18+
- Vite

---

## 📄 Dokumentacja

- [Swagger UI](http://localhost:8080/swagger-ui/index.html)
- Endpointy REST dla: `auth`, `users`, `oceny`, `pogoda`, `swieta`

---


## 🧑‍💻 Autor

Projekt wykonany w ramach zaliczenia — Karol Soboń, Weronika Szostek

