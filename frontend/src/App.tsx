import React, { useEffect, useState } from "react";
import { BrowserRouter as Router, Route, Routes, Navigate } from "react-router-dom";
import LoginPage from "./pages/LoginPage";
import RegisterPage from "./pages/RegisterPage";
import StudentDashboard from "./pages/StudentDashboard";
import TeacherDashboard from "./pages/TeacherDashboard";
import { jwtDecode } from "jwt-decode";

// Interfejs do dekodowania tokenu
interface DecodedToken {
  role: "NAUCZYCIEL" | "UCZEN";
}

function App() {
  return (
    <Router>
      <Routes>
        {/* Strony logowania i rejestracji */}
        <Route path="/login" element={<LoginPage />} />
        <Route path="/register" element={<RegisterPage />} />

        {/* Ochrona tras na podstawie roli */}
        <Route path="/teacher-dashboard" element={<TeacherRoute />} />
        <Route path="/student-dashboard" element={<StudentRoute />} />

        {/* Przekierowanie domyślne, jeśli użytkownik nie jest zalogowany */}
        <Route path="*" element={<Navigate to="/login" />} />
      </Routes>
    </Router>
  );
}

// Komponent strażnika dla nauczyciela
function TeacherRoute() {
  const [role, setRole] = useState<"NAUCZYCIEL" | "UCZEN" | null>(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const token = localStorage.getItem("authToken");
    if (token) {
      try {
        const decoded: DecodedToken = jwtDecode(token);
        setRole(decoded.role);
      } catch (error) {
        console.error("Błędny token");
        localStorage.removeItem("authToken");
        setRole(null);
      }
    } else {
      setRole(null);
    }
    setLoading(false);
  }, []);

  if (loading) {
    return <div>Ładowanie...</div>;
  }

  return role === "NAUCZYCIEL" ? <TeacherDashboard /> : <Navigate to="/login" />;
}

// Komponent strażnika dla ucznia
function StudentRoute() {
  const [role, setRole] = useState<"NAUCZYCIEL" | "UCZEN" | null>(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const token = localStorage.getItem("authToken");
    if (token) {
      try {
        const decoded: DecodedToken = jwtDecode(token);
        setRole(decoded.role);
      } catch (error) {
        console.error("Błędny token");
        localStorage.removeItem("authToken");
        setRole(null);
      }
    } else {
      setRole(null);
    }
    setLoading(false);
  }, []);

  if (loading) {
    return <div>Ładowanie...</div>;
  }

  return role === "UCZEN" ? <StudentDashboard /> : <Navigate to="/login" />;
}

export default App;