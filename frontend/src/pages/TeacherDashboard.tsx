import { useEffect, useState } from "react";
import axios from "axios";
import { useNavigate } from 'react-router-dom';

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

const mozliweOceny = [1, 2, 3, 4, 5, 6];

export default function TeacherDashboard() {
  const [students, setStudents] = useState<User[]>([]);
  const [oceny, setOceny] = useState<Ocena[]>([]);
  const [nowaOcena, setNowaOcena] = useState({
    przedmiot: "",
    wartosc: mozliweOceny[0],
    uczenId: "",
  });
  const [editingOcenaId, setEditingOcenaId] = useState<string | null>(null);
  const [editedOcena, setEditedOcena] = useState<{
    przedmiot?: string;
    wartosc?: number;
  } | null>(null);
  const [pogoda, setPogoda] = useState<any>(null);
  const [swieta, setSwieta] = useState<any[]>([]);
  const [expandedStudentId, setExpandedStudentId] = useState<string | null>(null);
  const navigate = useNavigate();

  useEffect(() => {
    async function fetchData() {
      try {
        const resStudents = await axios.get("http://localhost:8080/api/users?role=UCZEN");
        const resOceny = await axios.get("http://localhost:8080/api/oceny");
        const resWeather = await axios.get("http://localhost:8080/api/pogoda/Warszawa");
        const resSwieta = await axios.get("http://localhost:8080/api/swieta/PL/2025");

        setStudents(
          resStudents.data.sort((a: User, b: User) =>
            `${a.lastName} ${a.firstName}`.localeCompare(`${b.lastName} ${b.firstName}`)
          )
        );
        setOceny(resOceny.data);
        setPogoda(resWeather.data);
        setSwieta(resSwieta.data);
      } catch (error) {
        console.error("Błąd ładowania danych:", error);
      }
    }

    fetchData();
  }, []);

  const handleAddOcena = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      const response = await axios.post("http://localhost:8080/api/oceny", {
        ...nowaOcena,
        data: new Date().toISOString().split("T")[0],
      });
      setOceny([...oceny, response.data]);
      setNowaOcena({ przedmiot: "", wartosc: mozliweOceny[0], uczenId: "" });
    } catch (error) {
      console.error("Błąd przy dodawaniu oceny:", error);
    }
  };

  const handleDelete = async (id: string) => {
    await axios.delete(`http://localhost:8080/api/oceny/${id}`);
    setOceny(oceny.filter((o) => o.id !== id));
  };

  const handleEdit = (ocena: Ocena) => {
    setEditingOcenaId(ocena.id);
    setEditedOcena({ przedmiot: ocena.przedmiot, wartosc: ocena.wartosc });
  };

  const handleCancelEdit = () => {
    setEditingOcenaId(null);
    setEditedOcena(null);
  };

  const handleInputChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>) => {
    const { name, value } = e.target;
    setEditedOcena((prevEditedOcena) => ({
      ...prevEditedOcena,
      [name]: name === 'wartosc' ? Number(value) : value,
    }));
  };

  const handleOcenaWybrana = (ocena: number) => {
    setNowaOcena((prevNowaOcena) => ({ ...prevNowaOcena, wartosc: ocena }));
  };

  const handleEdytowanaOcenaWybrana = (ocena: number) => {
    setEditedOcena((prevEditedOcena) => ({ ...prevEditedOcena, wartosc: ocena }));
  };

  const handleSaveEdit = async (id: string) => {
    if (editedOcena) {
      try {
        const response = await axios.put(`http://localhost:8080/api/oceny/${id}`, editedOcena);
        setOceny(oceny.map((o) => (o.id === id ? response.data : o)));
        setEditingOcenaId(null);
        setEditedOcena(null);
      } catch (error) {
        console.error("Błąd podczas edycji oceny:", error);
      }
    }
  };

  const handleLogout = () => {
    localStorage.removeItem('authToken');
    navigate('/login');
  };

  const toggleStudentGrades = (studentId: string) => {
    setExpandedStudentId((prevId) => (prevId === studentId ? null : studentId));
  };

  return (
    <div className="dashboard-layout">
      <button className="logout-button" onClick={handleLogout}>
        Wyloguj
      </button>
      {/* Lewa strona - główna treść */}
      <div className="main-content">
        <h1>Panel nauczyciela</h1>

        {/* Formularz dodawania oceny */}
        <form onSubmit={handleAddOcena} className="add-grade-form">
          <select
            value={nowaOcena.uczenId}
            onChange={(e) => setNowaOcena({ ...nowaOcena, uczenId: e.target.value })}
            required
            className="select-input"
          >
            <option value="">Wybierz ucznia</option>
            {students.map((u) => (
              <option key={u.id} value={u.id}>
                {u.firstName} {u.lastName}
              </option>
            ))}
          </select>
          <select
            value={nowaOcena.przedmiot}
            onChange={(e) => setNowaOcena({ ...nowaOcena, przedmiot: e.target.value })}
            required
            className="select-input"
          >
            <option value="">Rodzaj oceny</option>
            <option value="Sprawdzian">Sprawdzian</option>
            <option value="Kartkówka">Kartkówka</option>
            <option value="Odpowiedź">Odpowiedź</option>
            <option value="Aktywność">Aktywność</option>
          </select>
          <div className="grade-options">
            {mozliweOceny.map((ocena) => (
              <button
                key={ocena}
                type="button"
                className={`grade-option ${nowaOcena.wartosc === ocena ? 'selected' : ''}`}
                onClick={() => handleOcenaWybrana(ocena)}
              >
                {ocena}
              </button>
            ))}
          </div>
          <button type="submit" className="primary-button">Dodaj ocenę</button>
        </form>

        {/* Lista uczniów z rozwijanymi ocenami i numeracją */}
        {students.map((student, index) => (
          <div key={student.id} className="student-grades-container">
            <div className="student-header" onClick={() => toggleStudentGrades(student.id)}>
              <h2 className="student-name">
                {index + 1}. {student.firstName} {student.lastName}
              </h2>
              <span className="expand-arrow">{expandedStudentId === student.id ? '▲' : '▼'}</span>
            </div>
            {expandedStudentId === student.id && (
              <ul className="grades-list">
                {oceny
                  .filter((o) => o.uczenId === student.id)
                  .map((ocena) => (
                    <li key={ocena.id} className="grade-item">
                      {editingOcenaId === ocena.id ? (
                        <>
                          <select
                            name="przedmiot"
                            value={editedOcena?.przedmiot}
                            onChange={handleInputChange}
                            required
                            className="edit-select"
                          >
                            <option value="">Wybierz przedmiot</option>
                            <option value="Sprawdzian">Sprawdzian</option>
                            <option value="Kartkówka">Kartkówka</option>
                            <option value="Odpowiedź">Odpowiedź</option>
                            <option value="Aktywność">Aktywność</option>
                          </select>
                          <div className="grade-options">
                            {mozliweOceny.map((ocenaValue) => (
                              <button
                                key={ocenaValue}
                                type="button"
                                className={`grade-option ${editedOcena?.wartosc === ocenaValue ? 'selected' : ''}`}
                                onClick={() => handleEdytowanaOcenaWybrana(ocenaValue)}
                              >
                                {ocenaValue}
                              </button>
                            ))}
                          </div>
                          <div className="actions">
                            <button onClick={() => handleSaveEdit(ocena.id)} className="save-button">Zapisz</button>
                            <button onClick={handleCancelEdit} className="cancel-button">Anuluj</button>
                          </div>
                        </>
                      ) : (
                        <>
                          <span className="grade-details">
                            <span className="subject">{ocena.przedmiot}:</span> <span className="value">{ocena.wartosc}</span> (<span className="date">{ocena.data}</span>)
                          </span>
                          <div className="actions">
                            <button onClick={() => handleEdit(ocena)} className="edit-button">Edytuj</button>
                            <button onClick={() => handleDelete(ocena.id)} className="delete-button">Usuń</button>
                          </div>
                        </>
                      )}
                    </li>
                  ))}
              </ul>
            )}
          </div>
        ))}
      </div>

      {/* Prawa strona - widgety */}
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