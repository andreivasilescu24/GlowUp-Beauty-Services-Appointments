import type { BeautySalon } from "../types/beautySalon";

const API_URL = "http://localhost:8090/api/v1";

const getAuthHeaders = () => {
  const token = localStorage.getItem("token");
  return {
    Authorization: `Bearer ${token}`,
    "Content-Type": "application/json",
  };
};

export const beautySalonService = {
  async getBeautySalons(): Promise<BeautySalon[]> {
    const response = await fetch(`${API_URL}/beautySalon`, {
      headers: getAuthHeaders(),
    });
    if (!response.ok) {
      throw new Error("Failed to fetch beauty salons");
    }
    return response.json();
  },

  async getMyBeautySalons(): Promise<BeautySalon[]> {
    const response = await fetch(`${API_URL}/beautySalon/me`, {
      headers: getAuthHeaders(),
    });
    if (!response.ok) {
      throw new Error("Failed to fetch your beauty salons");
    }
    return response.json();
  },
};
