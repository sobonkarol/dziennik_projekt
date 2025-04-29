import React, { useState } from 'react';
import axios from 'axios';
import { Link } from 'react-router-dom';

export default function RegisterForm() {
  const [formData, setFormData] = useState({
    username: '',
    password: '',
    firstName: '',
    lastName: '',
    role: 'UCZEN',
  });

  const [error, setError] = useState<string | null>(null);
  const [success, setSuccess] = useState<string | null>(null);
  const [usernameError, setUsernameError] = useState<string | null>(null);

  const handleChange = (
    e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>
  ) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
    setError(null);
    setSuccess(null);

    // Jeśli użytkownik zmienia login, od razu weryfikujemy
    if (e.target.name === 'username') {
      validateUsername(e.target.value);
    }
  };

  const validateUsername = (username: string) => {
    if (username.length < 4) {
      setUsernameError('Login musi mieć co najmniej 4 znaki.');
    } else if (username.includes(' ')) {
      setUsernameError('Login nie może zawierać spacji.');
    } else {
      setUsernameError(null);
    }
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();

    // Ostateczna walidacja loginu przed wysłaniem
    if (formData.username.length < 4 || formData.username.includes(' ')) {
      validateUsername(formData.username);
      return;
    }

    try {
      await axios.post('http://localhost:8080/api/auth/register', formData);
      setSuccess('Rejestracja zakończona sukcesem.');
      setError(null);
      setUsernameError(null);
    } catch (error: any) {
      console.error('Błąd rejestracji:', error);
      if (axios.isAxiosError(error)) {
        const responseMessage = error.response?.data?.message || error.response?.data || '';
        if (responseMessage.includes('Login jest już zajęty')) {
          setError('Podany login jest już zajęty. Wybierz inny.');
        } else {
          setError('Wystąpił błąd podczas rejestracji.');
        }
      } else {
        setError('Wystąpił nieznany błąd.');
      }
      setSuccess(null);
    }
  };

  return (
    <form onSubmit={handleSubmit} className="register-form">
      <h2>Rejestracja</h2>

      {error && <p style={{ color: 'red', fontWeight: 'bold' }}>{error}</p>}
      {success && <p style={{ color: 'green', fontWeight: 'bold' }}>{success}</p>}

      <div className="form-group">
        <label htmlFor="username">Login:</label>
        <input
          type="text"
          id="username"
          name="username"
          placeholder="Login"
          value={formData.username}
          onChange={handleChange}
          className="input-field"
          required
        />
        {usernameError && (
          <small style={{ color: 'red' }}>{usernameError}</small>
        )}
      </div>

      <div className="form-group">
        <label htmlFor="password">Hasło:</label>
        <input
          type="password"
          id="password"
          name="password"
          placeholder="Hasło"
          value={formData.password}
          onChange={handleChange}
          className="input-field"
          required
        />
      </div>

      <div className="form-group">
        <label htmlFor="firstName">Imię:</label>
        <input
          type="text"
          id="firstName"
          name="firstName"
          placeholder="Imię"
          value={formData.firstName}
          onChange={handleChange}
          className="input-field"
          required
        />
      </div>

      <div className="form-group">
        <label htmlFor="lastName">Nazwisko:</label>
        <input
          type="text"
          id="lastName"
          name="lastName"
          placeholder="Nazwisko"
          value={formData.lastName}
          onChange={handleChange}
          className="input-field"
          required
        />
      </div>

      <div className="form-group">
        <label htmlFor="role">Rola:</label>
        <select
          id="role"
          name="role"
          value={formData.role}
          onChange={handleChange}
          className="select-input"
        >
          <option value="UCZEN">Uczeń</option>
          <option value="NAUCZYCIEL">Nauczyciel</option>
        </select>
      </div>

      <button type="submit" className="register-button">
        Zarejestruj się
      </button>

      <div className="login-link">
        <Link to="/login" className="login-button-link">
          Zaloguj się
        </Link>
      </div>
    </form>
  );
}