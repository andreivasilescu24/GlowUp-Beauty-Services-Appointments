export type UserRole = "USER" | "OWNER";

export interface RegisterRequest {
  email: string;
  username: string;
  password: string;
  role: UserRole;
}

export interface LoginRequest {
  email: string;
  password: string;
}

export interface LoginResponse {
  access_token: string;
  token_type: string;
  expires_in: number;
}

export interface RegisterResponse {
  message: string;
}

export interface User {
  id: number;
  name: string;
  email: string;
  username: string;
  role: UserRole;
}
