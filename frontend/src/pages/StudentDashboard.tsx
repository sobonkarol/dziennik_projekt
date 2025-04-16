import { useEffect, useState } from "react";
import axios from "axios";
import { useNavigate } from 'react-router-dom';
import { jwtDecode } from 'jwt-decode';

interface Ocena {
  id: string;
  przedmiot: string;
  wartosc: number;
  data: string;
  uczenId: string;
}

interface User {
  id: string;
  firstName: string;
  lastName: string;
}

export default function StudentDashboard() {
  const [student, setStudent] = useState<User | null>(null);
  const [oceny, setOceny] = useState<Ocena[]>([]);
  const [pogoda, setPogoda] = useState<any>(null);
  const [swieta, setSwieta] = useState<any[]>([]);
  const navigate = useNavigate();

  useEffect(() => {
    const token = localStorage.getItem('authToken');
    if (!token) {
      navigate('/login');
      return;
    }

    const decoded: any = jwtDecode(token);
    const userId = decoded.sub;

    async function fetchData() {
      try {
        const [userRes, ocenyRes, pogodaRes, swietaRes] = await Promise.all([
          axios.get(`http://localhost:8080/api/users/${userId}`),
          axios.get("http://localhost:8080/api/oceny"),
          axios.get("http://localhost:8080/api/pogoda/Warszawa"),
          axios.get("http://localhost:8080/api/swieta/PL/2025"),
        ]);

        setStudent(userRes.data);
        const uczenId = userRes.data.id;
        const wszystkieOceny = ocenyRes.data;
        const mojeOceny = wszystkieOceny.filter((o: Ocena) => o.uczenId === uczenId);
        setOceny(mojeOceny);
        setPogoda(pogodaRes.data);
        setSwieta(swietaRes.data);
      } catch (error) {
        console.error("Błąd ładowania danych:", error);
      }
    }

    fetchData();
  }, [navigate]);

  const handleLogout = () => {
    localStorage.removeItem('authToken');
    navigate('/login');
  };

  if (!student) return <div>Ładowanie...</div>;

  return (
    <div className="dashboard-layout">
      <button className="logout-button" onClick={handleLogout}>
        Wyloguj
      </button>
      <div className="main-content">
        <h1>Panel ucznia</h1>
        <h2>Oceny dla: {student.firstName} {student.lastName}</h2>
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
      <div className="widgets-container">
        {pogoda && (
          <div className="weather-widget">
            <h2 className="widget-title">Pogoda w Warszawie</h2>
            <p className="weather-temp">Temperatura: {pogoda.main.temp}°C</p>
            <p className="weather-desc">{pogoda.weather[0].description}</p>
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
              {swieta.length > 5 && <li className="more-holidays">...i więcej</li>}
            </ul>
          </div>
        )}
      </div>
    </div>
  );
}