import { useState } from "react";
import axios from "axios";

export default function GradeForm({ onGradeAdded }: { onGradeAdded: () => void }) {
  const [student, setStudent] = useState("");
  const [value, setValue] = useState("");

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      await axios.post(
        "/api/grades",
        { student, value },
        {
          headers: {
            Authorization: `Bearer ${localStorage.getItem("token")}`,
          },
        }
      );
      setStudent("");
      setValue("");
      onGradeAdded();
    } catch (error) {
      console.error("Błąd dodawania oceny:", error);
    }
  };

  return (
    <form onSubmit={handleSubmit}>
      <input
        value={student}
        onChange={(e) => setStudent(e.target.value)}
        placeholder="Uczeń"
        required
      />
      <input
        value={value}
        onChange={(e) => setValue(e.target.value)}
        placeholder="Ocena"
        required
      />
      <button type="submit">Dodaj ocenę</button>
    </form>
  );
}