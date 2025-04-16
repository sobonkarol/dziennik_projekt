const API_BASE = "http://localhost:8080/api";

export const registerUser = async (username: string, password: string) => {
  const response = await fetch(`${API_BASE}/auth/register`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify({ username, password }),
  });

  if (!response.ok) {
    throw new Error("Rejestracja nie powiodła się");
  }

  return await response.json();
};