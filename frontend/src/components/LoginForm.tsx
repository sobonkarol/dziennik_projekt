import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';
import { jwtDecode } from 'jwt-decode';
import { Link } from 'react-router-dom'; // Import Link

export default function LoginForm() {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const navigate = useNavigate();

  const handleLogin = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      const response = await axios.post('http://localhost:8080/api/auth/login', {
        username,
        password,
      });

      const token = response.data.token;
      console.log('Received Token:', token);
      localStorage.setItem('authToken', token); // Zmieniono nazwę klucza na 'authToken' dla spójności

      const decodedToken: any = jwtDecode(token);
      console.log('Decoded Token:', decodedToken);

      if (decodedToken.role === 'NAUCZYCIEL') {
        navigate('/teacher-dashboard');
      } else if (decodedToken.role === 'UCZEN') {
        navigate('/student-dashboard');
      } else {
        alert('Nieznana rola użytkownika');
        localStorage.removeItem('authToken');
      }
    } catch (error) {
      alert('Nieprawidłowe dane logowania');
      console.error('Błąd logowania:', error);
    }
  };

  return (
    <form onSubmit={handleLogin} className="login-form">
      <div className="form-group">
        <label htmlFor="username">Login:</label>
        <input
          type="text"
          id="username"
          className="input-field"
          value={username}
          onChange={(e) => setUsername(e.target.value)}
          placeholder="Login"
          required
        />
      </div>
      <div className="form-group">
        <label htmlFor="password">Hasło:</label>
        <input
          type="password"
          id="password"
          className="input-field"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
          placeholder="Hasło"
          required
        />
      </div>
      <button type="submit" className="login-button">
        Zaloguj
      </button>
      <div className="register-link">
        <Link to="/register" className="register-button-link">
          Zarejestruj się
        </Link>
      </div>
    </form>
  );
}