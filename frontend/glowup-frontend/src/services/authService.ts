import type {
  LoginRequest,
  LoginResponse,
  RegisterRequest,
  RegisterResponse,
  UserRole,
} from "../types/auth";

const API_URL = "http://localhost:8090/api/v1/auth"; // Updated to match your Spring Boot API path

export const authService = {
  register: async (data: RegisterRequest): Promise<RegisterResponse> => {
    const response = await fetch(`${API_URL}/register`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(data),
    });

    const responseData = await response.json();

    if (!response.ok) {
      throw new Error(responseData.message || "Registration failed");
    }

    return responseData;
  },

  login: async (data: LoginRequest): Promise<LoginResponse> => {
    const response = await fetch(`${API_URL}/login`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(data),
    });

    const responseData = await response.json();

    if (!response.ok) {
      throw new Error(responseData.message || "Login failed");
    }

    // Store the token and user info in localStorage
    localStorage.setItem("token", responseData.access_token);
    localStorage.setItem("token_type", responseData.token_type);
    localStorage.setItem("token_expires", String(responseData.expires_in));
    localStorage.setItem("user_role", responseData.role);
    return responseData;
  },

  logout: () => {
    localStorage.removeItem("token");
    localStorage.removeItem("token_type");
    localStorage.removeItem("token_expires");
    localStorage.removeItem("user_role");
  },

  getToken: (): string | null => {
    return localStorage.getItem("token");
  },

  getAuthHeader: (): string => {
    const token = localStorage.getItem("token");
    const tokenType = localStorage.getItem("token_type");
    return token ? `${tokenType} ${token}` : "";
  },

  getUserRole: (): UserRole | null => {
    return localStorage.getItem("user_role") as UserRole | null;
  },
};
