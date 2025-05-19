import type { Category } from "../types/category";
import { authService } from "./authService";

const API_URL = "http://localhost:8090/api/v1/categories";

export const categoryService = {
  getCategories: async (): Promise<Category[]> => {
    const token = authService.getToken();

    const response = await fetch(API_URL, {
      headers: {
        Authorization: `Bearer ${token}`,
        "Content-Type": "application/json",
      },
    });

    if (!response.ok) {
      const error = await response.json();
      throw new Error(error.message || "Failed to fetch categories");
    }

    return response.json();
  },
};
