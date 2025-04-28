import { useEffect, useState } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";
import { jwtDecode } from "jwt-decode";

interface User {
  id: string;
  firstName: string;
  lastName: string;
}

interface Ocena {
  id: string;
  przedmiot: string;
  wartosc: number;
  data: string;
  uczenId: string;
}

export default function StudentDashboard() {
  const [student, setStudent] = useState<User | null>(null);
  const [oceny, setOceny] = useState<Ocena[]>([]);
  const [pogoda, setPogoda] = useState<any>(null);
  const [swieta, setSwieta] = useState<any[]>([]);
  const navigate = useNavigate();

  useEffect(() => {
    async function fetchData() {
      try {
        const token = localStorage.getItem("authToken");
        if (!token) {
          navigate("/login");
          return;
        }
        const config = { headers: { Authorization: `Bearer ${token}` } };
        const decoded: any = jwtDecode(token);
        const userId = decoded.sub;

        const resStudent = await axios.get(`http://localhost:8080/api/users/${userId}`, config);
        const studentData = Array.isArray(resStudent.data) ? resStudent.data[0] : resStudent.data;
        setStudent(studentData);

        const resOceny = await axios.get("http://localhost:8080/api/oceny", config);
        const mojeOceny = resOceny.data.filter((o: Ocena) => o.uczenId === studentData.id);
        setOceny(mojeOceny);

        const resWeather = await axios.get("http://localhost:8080/api/pogoda/Warszawa", config);
        setPogoda(resWeather.data);

        const resSwieta = await axios.get("http://localhost:8080/api/swieta/PL/2025", config);
        setSwieta(resSwieta.data);
      } catch (error) {
        console.error("Błąd ładowania danych:", error);
      }
    }

    fetchData();
  }, [navigate]);

  const handleLogout = () => {
    localStorage.removeItem("authToken");
    navigate("/login");
  };

  if (!student) return <div>Ładowanie danych...</div>;

  return (
    <div className="dashboard-layout">
      <button className="logout-button" onClick={handleLogout}>
        Wyloguj
      </button>

      {/* Lewa strona */}
      <div className="main-content">
        <h1>Panel ucznia</h1>

        <h2 className="student-name">
          {student.firstName} {student.lastName}
        </h2>

        <ul className="grades-list">
          {oceny.length > 0 ? (
            oceny.map((ocena) => (
              <li key={ocena.id} className="grade-item">
                <span className="grade-details">
                  <span className="subject">{ocena.przedmiot}:</span> <span className="value">{ocena.wartosc}</span> (<span className="date">{ocena.data}</span>)
                </span>
              </li>
            ))
          ) : (
            <li className="grade-item">Brak ocen do wyświetlenia.</li>
          )}
        </ul>
      </div>

{/* Prawa strona - widgety */}
<div className="widgets-container">
{pogoda && (
  <div className="weather-widget">
    <h2 className="widget-title">Pogoda w {pogoda.city_name}</h2>
    <div className="weather-main-info">
      <img
        src={`https://openweathermap.org/img/wn/${pogoda.weather_icon}@2x.png`}
        alt={pogoda.weather_description}
        className="weather-icon"
      />
      <div>
        <p className="weather-temp">Temperatura: {pogoda.temperature}°C</p>
        <p className="weather-desc">{pogoda.weather_description}</p>
      </div>
    </div>
    <p>Wilgotność: {pogoda.humidity}%</p>
    <p>Wiatr: {pogoda.wind_speed} m/s</p>
    <p>Ciśnienie: {pogoda.pressure} hPa</p>
    <div className="school-tip">
      <h3>Wskazówka dla uczniów:</h3>
      {pogoda.temperature < 5 ? (
        <p>❄️ Jest zimno! Pamiętaj o ciepłej kurtce przed wyjściem do szkoły.</p>
      ) : pogoda.temperature > 25 ? (
        <p>☀️ Gorąco! Zabierz ze sobą wodę na lekcje WF-u.</p>
      ) : pogoda.weather_main === "Rain" || pogoda.weather_main === "Drizzle" ? (
        <p>🌧️ Może padać! Zabierz parasol lub pelerynę.</p>
      ) : pogoda.weather_main === "Snow" ? (
        <p>❄️ Śnieg! Uważaj na śliskie chodniki w drodze do szkoły.</p>
      ) : (
        <p>🌤️ Idealna pogoda na lekcje WF na świeżym powietrzu!</p>
      )}
    </div>
  </div>
)}

  {swieta.length > 0 && (
    <div className="holidays-widget">
      <h2 className="widget-title">Nadchodzące Święta (2025)</h2>
      <ul className="holidays-list">
        {swieta.slice(0, 5).map((s, i) => (
          <li key={i} className="holiday-item">
            <span className="holiday-date">{s.date}</span>
            <span className="holiday-name">{s.name}</span>
          </li>
        ))}
        {swieta.length > 5 && (
          <li className="more-holidays">...i więcej</li>
        )}
      </ul>
    </div>
  )}
</div>
</div> // <- Zamknięcie głównego kontenera!
  );
}