import React, { useState } from 'react';
import axios from 'axios';
import { Link } from 'react-router-dom'; // Import Link

export default function RegisterForm() {
  const [formData, setFormData] = useState({
    username: '',
    password: '',
    firstName: '',
    lastName: '',
    role: 'UCZEN',
  });

  const handleChange = (
    e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>
  ) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      await axios.post('http://localhost:8080/api/auth/register', formData);
      alert('Rejestracja zakończona sukcesem');
      // Można dodać przekierowanie po udanej rejestracji, np. na stronę logowania
    } catch (error) {
      alert('Błąd podczas rejestracji');
      console.error('Błąd rejestracji:', error);
    }
  };

  return (
    <form onSubmit={handleSubmit} className="register-form">
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